package it.univaq.ospedali.data.remote

import it.univaq.ospedali.data.remote.model.RemoteOspedale
import it.univaq.ospedali.domain.model.Ospedale
import it.univaq.ospedali.domain.repository.OspedaleRemoteRepository
import javax.inject.Inject

// la seguente classe implementa l'interfaccia OspedaleRemoteRepository
// per implementare il metodo getOspedali abbiamo bisogno di portare nel costruttore l'ospedaleservice istanziato dal dependency injection

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

class OspedaleRetrofitRepository @Inject constructor(
    private val ospedaleService: OspedaleService
): OspedaleRemoteRepository {

    // sovrascrittura della funzione getOspedali nell'interfaccia
    override suspend fun getOspedali(): List<Ospedale> {
        // ricorda che la funzione downloadData() di ospedaleService restituisce una lista di remoteospedali e a noi serve una lista di ospedali
        // quindi mappiamo i remoteodpedali in ospedali tramite la funzione map()

        return ospedaleService.downloadData()
            .map(RemoteOspedale::toModel) // prende in ingresso il remoteospedale da convertire con la function
    }
}
