package it.univaq.ospedali.data.remote

import it.univaq.ospedali.data.remote.model.RemoteOspedale
import it.univaq.ospedali.domain.model.Ospedale
import it.univaq.ospedali.domain.repository.OspedaleRemoteRepository
import javax.inject.Inject



// funzione che mappa una lista di remoteospedali in una lista di ospedali
fun RemoteOspedale.toModel(): Ospedale = Ospedale(
    id = null,

    nome = cnome,
    comune = ccomune,
    provincia = cprovincia,
    regione = cregione,
    latitudine = clatitudine.toDoubleOrNull() ?: 0.0, // se fosse nullo nel json
    longitudine = clongitudine.toDoubleOrNull() ?: 0.0
)

// la seguente classe implementa l'interfaccia OspedaleRemoteRepository
// per implementare il metodo getOspedali abbiamo bisogno di portare nel costruttore l'ospedaleservice istanziato dal dependency injection
class OspedaleRetrofitRepository @Inject constructor(
    private val ospedaleService: OspedaleService
): OspedaleRemoteRepository {

    // sovrascrittura della funzione getOspedali nell'interfaccia OspedaleRemoteRepository
    override suspend fun getOspedali(): List<Ospedale> {

        // restituisce gli oggetti presi da Internet tramite la funzione downloadData()
        // ricorda che la funzione downloadData() di ospedaleService( che contiene il GET)
        // restituisce una lista di remoteospedali e a noi serve una lista di ospedali
        return ospedaleService.downloadData()
            // quindi mappiamo i remoteospedali in ospedali tramite la funzione map()
            .map(RemoteOspedale::toModel)
    }
}
