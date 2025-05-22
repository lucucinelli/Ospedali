package it.univaq.ospedali.ui.screen.detail

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import it.univaq.ospedali.domain.model.Ospedale


@SuppressLint("ContextCastToActivity") // significa che vengono ignorate le accortezze che precedono la conversione di un Context in un Activity
@OptIn(ExperimentalMaterial3Api::class) // il codice sta utilizzando funzionalità sperimentali  della libreria Jetpack Compose Material 3
@Composable
fun DetailScreen(
    modifier: Modifier,
    viewModel: DetailViewModel = hiltViewModel(),
    comune: String?,
    provincia: String?,
    regione: String?
){
    val activity = LocalContext.current as Activity // .current restituisce un Context che viene convertito in Activity tramite la dicitura as

    val uiState = viewModel.uiState  // uiState acquisisce dal viewmodel lo stato corrente della UI

    LifecycleEventEffect(Lifecycle.Event.ON_CREATE) {  // quando crea la schermata Detail la prima volta
        viewModel.onEvent(    // chiama la funzione onEvent del viewModel
            DetailEvent.OnOspedaleSelected(   // printa i valori dell'ospedale selezionato
                comune = comune,
                provincia = provincia,
                regione = regione
            )
        )
    }

    Scaffold (
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = "Dettagli") },
                navigationIcon = {
                    IconButton(onClick = {activity.finish()}) {  // quando viene cliccato il pulsante di indietro viene chiusa la schermata di Detail
                        Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ){ padding ->  // indice in ingresso ripreso dallo scaffold

        if (uiState.ospedali.isEmpty()){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ){
                Text(text = "Nessun ospedale trovato")
            }
            return@Scaffold
        }
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            items(uiState.ospedali.size){
                OspedaleItem(
                    modifier = Modifier.fillMaxWidth(),
                    ospedale = uiState.ospedali[it]
                )
            }
        }
    }
}


@Composable
fun OspedaleItem(
    modifier: Modifier = Modifier,
    ospedale: Ospedale
){
    Card(
        modifier = modifier
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp)) // crea bordo card
    ) {
        Column(
            modifier = Modifier
                .background(Color(0xFFE0E0E0))
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Id: ${ospedale.id}")
            Text(text = "Comune: ${ospedale.comune}")
            Text(text = "Provincia: ${ospedale.provincia}")
            Text(text = "Regione: ${ospedale.regione}")
        }
    }
}