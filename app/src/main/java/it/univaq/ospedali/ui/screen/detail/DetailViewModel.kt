package it.univaq.ospedali.ui.screen.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.univaq.ospedali.domain.model.Ospedale
import it.univaq.ospedali.domain.repository.OspedaleLocalRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

// rappresenta lo stato corrente degli oggetti della schermata
data class DetailUiState(
    val ospedali: List<Ospedale> = emptyList()
)

// classe che definisce tutti i possibili eventi
sealed class DetailEvent{
    // selezione di un ospedale
    data class OnOspedaleSelected( val comune: String?, val provincia: String?, val regione: String?): DetailEvent()
}

@HiltViewModel // dice a Hilt (sistema del dependency injection) che deve creare automatica il view model
class DetailViewModel @Inject constructor(
    private val localRepository: OspedaleLocalRepository  // database locale
) : ViewModel() {

    // stato osservabile della UI, quando cambia la mappa si aggiorna
    // mutableStateOf rende osservabile quella variabile da Jetpack Compose
    // MapUiState è il valore iniziale, quindi lista vuota di ospedali
    // private set vuol dire che solo da dentro il ViewModel si può modificare uiState, da fuori lo si può solo leggere (es. UI)
    var uiState by mutableStateOf(DetailUiState())
        private set

    // funzione che gestisce l'evento di selezione dell'ospedale
    fun onEvent(event: DetailEvent){
        when(event){
            // alla selezione di un ospedale
            is DetailEvent.OnOspedaleSelected -> {
                // il viewmodel fa partire una coroutine
                // codice asincrono che non blocca il thread principale (la UI)
                viewModelScope.launch {
                   // richiamo la funzione getOspedaliByComune del repository locale che mi restituisce uan lista di ospedali
                    localRepository.getOspedaliByComune(
                        event.comune ?: "",
                        event.provincia ?: "",
                        event.regione ?: ""
                        // la lista di ospedali è flow, quindi può essere riscattata solo con metodo collect
                        // dato che la lista è flow può cambiare nel tempo
                    ).collect {
                        // aggiorna lo stato della UI con la nuova lista di ospedali
                        uiState = uiState.copy(
                            ospedali = it
                        )
                    }
                }
            }
        }
    }
}