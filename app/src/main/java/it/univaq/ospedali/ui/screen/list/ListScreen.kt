package it.univaq.ospedali.ui.screen.list

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import it.univaq.ospedali.DetailActivity
import it.univaq.ospedali.domain.model.Ospedale

@Composable
fun ListScreen(
    modifier: Modifier = Modifier,
    viewModel: ListViewModel = hiltViewModel()  // dovendo utilizzare il view model che implementa il dependency injection devo utilizzare il metodo hiltViewModel
) {
    val uiState = viewModel.uiState

    val context = LocalContext.current // uso Compose per creare un contesto locale che muore quando chiudo la schermaat
    // serve per poter utilizzare il metodo startActivity

    if(uiState.loadingMsg != null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text(text = uiState.loadingMsg)  // mostro il messaggio di loading quando sono in fase di caricamento
        }
        return //esco dal List Screen
    }

    if (uiState.error != null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = uiState.error)  // mostro il messaggio di errore quando ho un errore
        }
        return
    }

    // se ho tutti i dati

    Column (modifier = modifier) {
        Text(
            modifier = Modifier.padding( 10.dp)
                .fillMaxWidth(),
            text = "Ospedali italiani",

            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold

        )

        LazyColumn( // lista ospedali
            modifier = Modifier.fillMaxWidth()
        ){
            items(uiState.ospedali.size){ index ->

                val ospedale = uiState.ospedali[index]
                OspedaleItem(
                    modifier = Modifier.fillMaxWidth(),
                    ospedale = ospedale,
                    onItemClick = {    // cosa accade quando clicco su un ospedale della lista
                        // ::class è un oggetto KClass di Kotlin che fa riferimento ad una classe
                        // .java fa in modo che il KClass sia convertito in una classe Java
                        context.startActivity(Intent(context, DetailActivity::class.java)
                            .also { // altri dati da considerare al passaggio da una schermata all'altra
                                it.putExtra("comune", ospedale.comune)
                                it.putExtra("provincia", ospedale.provincia)
                                it.putExtra("regione", ospedale.regione)
                            })
                    }
                )
            }
        }
    }
}


@Composable
fun OspedaleItem(   // mostro l'ospedale
    ospedale: Ospedale,
    modifier: Modifier = Modifier,
    onItemClick: (Ospedale) -> Unit = {}   // cliccando su un oggetto non viene restituito nulla
) {
    Column (  // layout verticale
        modifier = modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp)) // stonda i bordi
            .clickable {    // fa in modo che gli oggetti nella colonna siano cliccabili
                onItemClick(ospedale)  // su click fa quello specificato nel costruttore
            }
    ){
        Text(
            modifier = Modifier.padding(top = 6.dp, bottom = 0.dp, start = 6.dp, end = 6.dp),
            text = ospedale.id.toString(),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            modifier = Modifier.padding(top = 0.dp, bottom = 6.dp, start = 6.dp, end = 6.dp),
            text = "${ospedale.nome}, ${ospedale.comune}, ${ospedale.provincia}, ${ospedale.regione}",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}