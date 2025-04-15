package it.univaq.ospedali.data.local

import it.univaq.ospedali.data.local.dao.OspedaleDao
import it.univaq.ospedali.data.local.entity.LocalOspedale
import it.univaq.ospedali.domain.model.Ospedale
import it.univaq.ospedali.domain.repository.OspedaleLocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

fun LocalOspedale.toModel(): Ospedale = Ospedale(
    id = id,
    nome = nome,
    comune = comune,
    provincia = provincia,
    regione = regione,
    latitudine = latitudine,
    longitudine = longitudine
)

// creiamo una funzione che mappa un ospedale in localospedale
fun Ospedale.toLocal(): LocalOspedale = LocalOspedale(
    id = id,
    nome = nome,
    comune = comune,
    provincia = provincia,
    regione = regione,
    latitudine = latitudine,
    longitudine = longitudine
)

// implementazione dell'interfaccia OspedaleLocalRepository
class OspedaleRoomRepository @Inject constructor(
    private val ospedaleDao: OspedaleDao  // instanziato tramite dependency injection
): OspedaleLocalRepository {

    // sovrascriviamo le funzioni dell'interfaccia
    override suspend fun insert(ospedale: Ospedale) {
        ospedaleDao.insert(ospedale.toLocal()) // mappiamo l'ospedale in localospedale tramite la funzione toLocal() che quindi esegue la conversione
    }

    // implementazione della funzione che inserisce una lista di ospedali nel database
    override suspend fun insert(ospedali: List<Ospedale>) {
        ospedaleDao.insert(ospedali.map(Ospedale::toLocal))
    }

    // implementazione della funzione che cancella i dati dal database
    override suspend fun clearAll(){
        ospedaleDao.deleteAll()
    }

    // implementazione della funzione che recupera i dati dal database
    override fun getAll(): Flow<List<Ospedale>>{
        return ospedaleDao.getAll()
            .map{list ->
                list.map(LocalOspedale::toModel)
            }
    }

    override fun getOspedaliByComune(
        comune: String,
        provincia: String,
        regione: String
    ): Flow<List<Ospedale>> {
        return ospedaleDao.getOspedaliByComune(comune, provincia, regione)
            .map { list ->
                list.map(LocalOspedale::toModel)
            }
    }
}