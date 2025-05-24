package it.univaq.ospedali

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
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

                // fa in modo che il navController venga instanziato una voolta sola
                // ogni volta che viene aperta l'applicazione
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
                        startDestination = Screen.List // schermata iniziale avviata all'avvio dell'app
                    ){
                        // si definiscono le schermate tra le quali si può navigare
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

// realizzo la Botton Bar
@Composable
fun BottomNavigationBar(
    navController: NavHostController
){
    // definiamo gli elementi
    val items = remember {    // remember fa in modo che i bottoni vengano "instanziati" una sola volta
        listOf(
            // bottone per la Lista
            BottomNavigationItem(
                title = "Lista",
                icon = Icons.AutoMirrored.Default.List,  // icona del bottone
                route = Screen.List  // schermata da passare
            ),
            // bottone per la mappa
            BottomNavigationItem(
                title = "Mappa",
                icon = Icons.Default.LocationOn,  // non c'è in automirrored, così usiamo il marker
                route = Screen.Map  // schermata da passare
            )
        )
    }
    NavigationBar (containerColor = Color(0xFFFF6666)) {   // ha elementi di tipo NavigationBarItem

        // metodo che restituisce l'oggetto rappresentativo dello stato del navController
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route  // permette di sapere dove si trova l'utente
        // per stack di navigazione si intende la memoria delle schermate visitate
        items.forEach {
            NavigationBarItem(
                // serve a selezionare il bottone nel momento in cui la schermata corrente prelevata da curretRoute
                // è uguale alla stringa qualificata ottenuta da it.route (tipo "it.univaq.ospedali.Screen$List")
                // in parole povere, illumina il bottone della schermata dove ti trovi
                selected = currentRoute == it.route.javaClass.canonicalName,
                onClick = {
                    navController.navigate(it.route) { // passa alla schermata indicata da it.route
                        // serve a cancellare la schermata in cui ti trovavi poco prima di cambiarla
                        popUpTo(navController.graph.startDestinationId) {
                            // salva lo stato della schermata da cui si sta uscendo (es. posizione scroll, input utente).
                            saveState = true
                        }
                        launchSingleTop = true   // non ristanzia la schermata (se si preme sul pulsante già abilitato) ma prende sempre la stessa
                        restoreState = true // ripristina lo stato della schermata, se è stata già aperta una volta
                    }
                },
                // icona dei pulsanti, colori e label
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

// generico oggetto della barra di navigazione (bottone)
data class BottomNavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: Screen  // al click del bottone si passa ad una nuova schermata
)

// sealed perchè l'interfaccia può essere implentata da un num. max di classi
sealed class  Screen {

    // un data object è un singleton che contiene dati; facciamo riferimento ad oggetti di cui esiste una sola istanza
    // permette di convertire l'oggetto json in un oggetto kotlin
    @Serializable
    data object List: Screen()

    @Serializable
    data object Map: Screen()
}
