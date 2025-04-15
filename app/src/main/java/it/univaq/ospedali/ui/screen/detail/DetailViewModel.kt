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

data class DetailUiState(
    val ospedali: List<Ospedale> = emptyList()
)

sealed class DetailEvent{
    data class OnOspedaleSelected( val comune: String?, val provincia: String?, val regione: String?): DetailEvent()
}

@HiltViewModel // dice a Hilt (sistema del dependency injection) che deve creare automatica il view model
class DetailViewModel @Inject constructor(
    private val localRepository: OspedaleLocalRepository
) : ViewModel() {

    // private set: le variazioni della UI possono essere solamente lette dall'esterno, solo il view model può modificare lo UI state
    var uiState by mutableStateOf(DetailUiState())
        private set

    fun onEvent(event: DetailEvent){   // funzione tramite la quale il view model riceve eventi
        when(event){
            is DetailEvent.OnOspedaleSelected -> {
                viewModelScope.launch {   // fa parte di androidx.lifecycle e serve a lanciare coroutines quando il view model viene distrutto (es. l'utente cambia la schermata)
                    // codice asincrono che non blocca il thread principale (la UI)
                    localRepository.getOspedaliByComune(
                        event.comune ?: "",
                        event.provincia ?: "",
                        event.regione ?: ""
                    ).collect {   // racccoglie i dati dal flow (se cambiano in tempo locale)
                        uiState = uiState.copy(  // aggiorna lo stato della UI con la nuova lista di ospedali
                            ospedali = it
                        )
                    }
                }
            }
        }
    }
}