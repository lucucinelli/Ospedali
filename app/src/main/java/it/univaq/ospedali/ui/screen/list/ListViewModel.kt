package it.univaq.ospedali.ui.screen.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.univaq.ospedali.common.Resource
import it.univaq.ospedali.domain.model.Ospedale
import it.univaq.ospedali.domain.use_case.GetOspedaliUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

// rappresenta lo stato corrente degli oggetti della schermata
data class ListUiState(   // stati iniziali della schermata List
    val ospedali: List<Ospedale> = emptyList(),
    val loadingMsg: String? = null,
    val error: String? = null
)


@HiltViewModel  // serve ad implementare il DI
class ListViewModel @Inject constructor(
    private val getOspedaliUseCase: GetOspedaliUseCase
): ViewModel() {


    // stato osservabile della UI, quando cambia la mappa si aggiorna
    // mutableStateOf rende osservabile quella variabile da Jetpack Compose
    // MapUiState è il valore iniziale, quindi lista vuota di ospedali
    // private set vuol dire che solo da dentro il ViewModel si può modificare uiState, da fuori lo si può solo leggere (es. UI)
    var uiState by mutableStateOf(ListUiState())
        private set

    // processo eseguito quando il ViewModel viene creato, cioè ogni volta che avvio l'app quando mi sposto sul map screen
    // per tutta l'app viene instanziato una sola volta
    // quando chiudpo l'app e la riapro riparte l'init
    init {
        downloadOspedali()
    }

    // aggiorna lo uiState in base all'esito dell'operazione di download
    private fun downloadOspedali() {
        viewModelScope.launch {  // fa parte di androidx.lifecycle e serve a lanciare coroutines eseguite fino a quando il view model non viene distrutto (es. l'utente cambia la schermata)
            // coroutine: codice asincrono che non blocca il thread principale (la UI)
            getOspedaliUseCase().collect { resource ->  // raccoglie dati dal flow emesso da getOspedaliByComune (equivalente al foreach)
                when (resource) {
                    is Resource.Loading -> {
                        uiState = uiState.copy(
                            loadingMsg = resource.message,
                            error = null
                        )
                    }
                    is Resource.Success -> {
                        uiState = uiState.copy(
                            ospedali = resource.data,
                            loadingMsg = null,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        uiState = uiState.copy(
                            loadingMsg = null,
                            error = resource.message
                        )
                    }
                }
            }
        }
    }
}