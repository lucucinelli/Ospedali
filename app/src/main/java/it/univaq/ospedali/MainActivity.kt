package it.univaq.ospedali

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import it.univaq.ospedali.ui.screen.list.ListScreen
import it.univaq.ospedali.ui.screen.map.MapScreen
import it.univaq.ospedali.ui.theme.OspedaliTheme
import kotlinx.serialization.Serializable

@AndroidEntryPoint  // serve per utilizzare il dependency injection
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{
            OspedaliTheme{

                // tiene memoria del bottone selezionato
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {      // aggiungiamo il bottone
                        // elemento composable
                        BottomNavigationBar(navController)
                    }
                ) { innerPadding ->

                    NavHost (
                        modifier = Modifier.padding(innerPadding),// aggiunge il padding alla schermata
                        navController = navController,
                        startDestination = Screen.List // schermata iniziale
                    ){
                        composable <Screen.List> {  // all'interno del composable scriviamo l'elemento da visualizzare
                            ListScreen(  // schermata lista ospedali
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        composable <Screen.Map> {
                            MapScreen(   // schermata mappa ospedali
                                modifier = Modifier.fillMaxSize()

                            )
                        }
                    }
                }
            }
        }
    }
}

// realizziamo la schermata main activity con i bottoni list e map

@Composable
fun BottomNavigationBar(
    navController: NavHostController
){
    // definiamo gli elementi
    val items = remember {    // remember ricorda di non aggiornare in continuazione la variabile
        listOf(
            BottomNavigationItem(
                title = "List",
                icon = Icons.AutoMirrored.Default.List,  // icona del bottone
                route = Screen.List  // schermata da passare
            ),  // bottone per la Lista
            BottomNavigationItem(
                title = "Map",
                icon = Icons.Default.LocationOn,  // non c'è in automirrored, così usiamo il marker
                route = Screen.Map  // schermata da passare
            )  // bottone per la mappa
        )
    }
    NavigationBar (containerColor = Color(0xFFA0F77A)) {   // ha elementi di tipo NavigationBarItem

        val navBackStackEntry by navController.currentBackStackEntryAsState()  // il nav si aggiorna ad ogni cambio di pagina
        val currentRoute = navBackStackEntry?.destination?.route  // permette di sapere dove si trova l'utente recuperando il nome della schermata dallo stack di navigazione
        // per stack di navigazione si intende la memoria delle schermate visitate, permette di tornare indietro
        items.forEach {
            NavigationBarItem(
                selected = currentRoute == it.route.javaClass.canonicalName, // restituisce la stringa package + nome classe + list + map che è uguale alla schermata corrente
                onClick = {
                    navController.navigate(it.route) { // passa alla schermata selezionata
                        popUpTo(navController.graph.startDestinationId) {  // elimina gli altri bottoni
                            saveState = true
                        }
                        launchSingleTop = true   // non ristanzia la schermata ma prende sempre la stessa
                        restoreState = true // ripristina lo stato della schermata
                    }
                },
                icon = {
                    Icon(it.icon, contentDescription = it.title)
                },
                label = {
                    Text(text = it.title)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFFFFFFF),
                    selectedTextColor = Color(0xFF000000),
                    indicatorColor = Color(0xFF000000),
                    unselectedIconColor = Color(0xFF000000),
                    unselectedTextColor = Color(0xFF000000)

                )
            )
        }
    }
}

data class BottomNavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: Screen  // al click del bottone si passa ad una nuova schermata
)

sealed class  Screen {

    // un data object è un singleton che contiene dati; facciamo riferimento ad oggetti di cui esiste una sola istanza
    // permette di convertire l'oggetto in json, fa funzionare il plugin di navigazione
    @Serializable
    data object List: Screen()

    @Serializable
    data object Map: Screen()
}
