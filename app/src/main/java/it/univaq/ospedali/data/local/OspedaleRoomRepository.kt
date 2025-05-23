package it.univaq.ospedali.data.local

import it.univaq.ospedali.data.local.dao.OspedaleDao
import it.univaq.ospedali.data.local.entity.LocalOspedale
import it.univaq.ospedali.domain.model.Ospedale
import it.univaq.ospedali.domain.repository.OspedaleLocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// funzione che converte un oggetto localospedale in un oggetto ospedale
fun LocalOspedale.toModel(): Ospedale = Ospedale(
    id = id,
    nome = nome,
    comune = comune,
    provincia = provincia,
    regione = regione,
    latitudine = latitudine,
    longitudine = longitudine
)

// creiamo una funzione che mappa un ospedale in un localospedale
fun Ospedale.toLocal(): LocalOspedale = LocalOspedale(
    id = id,
    nome = nome,
    comune = comune,
    provincia = provincia,
    regione = regione,
    latitudine = latitudine,
    longitudine = longitudine
)

// classe per la manipolazione di dati su db locale, infatti prende in ingresso l'oggetto Dao
class OspedaleRoomRepository @Inject constructor(
    private val ospedaleDao: OspedaleDao  // instanziato tramite dependency injection
): OspedaleLocalRepository {

    // sovrascriviamo le funzioni dell'interfaccia mappando il localospedale in un ospedale

    // adesso prende un ospedale
    override suspend fun insert(ospedale: Ospedale) {
        ospedaleDao.insert(ospedale.toLocal()) // mappiamo l'ospedale in localospedale tramite la funzione toLocal()
    }

    // implementazione della funzione che inserisce una lista di ospedali nel database
    override suspend fun insert(ospedali: List<Ospedale>) {
        ospedaleDao.insert(ospedali.map(Ospedale::toLocal)) //casting
    }

    // implementazione della funzione che cancella i dati dal database
    override suspend fun clearAll(){
        ospedaleDao.deleteAll()
    }

    // implementazione della funzione che recupera i dati dal database
    override fun getAll(): Flow<List<Ospedale>>{
        return ospedaleDao.getAll()
            .map{list ->  // mappa la lista di local ospedali in una lista di ospedali
                list.map(LocalOspedale::toModel)  // mappa ogni local ospedale in un ospedale
            }
    }

    override fun getOspedaliByComune(
        comune: String,
        provincia: String,
        regione: String
    ): Flow<List<Ospedale>> {
        return ospedaleDao.getOspedaliByComune(comune, provincia, regione)
            .map { list -> // mappa la lista di local ospedali in una lista di ospedali
                list.map(LocalOspedale::toModel) // mappa ogni local ospedale in un ospedale
            }
    }
}