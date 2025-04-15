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


data class ListUiState(   // stati iniziali della schermata List
    val ospedali: List<Ospedale> = emptyList(),
    val loadingMsg: String? = null,
    val error: String? = null
)
@HiltViewModel  // dice a Hilt (sistema del dependency injection) che deve creare automatica il view model
class ListViewModel @Inject constructor(
    private val getOspedaliUseCase: GetOspedaliUseCase // essendo OspedaliUseCase instanziato tramite dependency injection
): ViewModel() {

    var uiState by mutableStateOf(ListUiState())  // private set: le variazioni della UI possono essere solamente lette dall'esterno, solo il view model può modificare lo UI state
        private set

    // implementiamo l'init del view model, cioè quello che accade quando viene creato la prima volta

    init {
        downloadOspedali()
    }

    private fun downloadOspedali() {
        viewModelScope.launch {  // fa parte di androidx.lifecycle e serve a lanciare coroutines quando il view model viene distrutto (es. l'utente cambia la schermata)
            // codice asincrono che non blocca il thread principale (la UI)
            getOspedaliUseCase().collect { resource ->  // la resource viene presa da collect
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