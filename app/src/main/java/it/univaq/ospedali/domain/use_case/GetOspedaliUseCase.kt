package it.univaq.ospedali.domain.use_case

import it.univaq.ospedali.common.Resource
import it.univaq.ospedali.domain.model.Ospedale
import it.univaq.ospedali.domain.repository.OspedaleLocalRepository
import it.univaq.ospedali.domain.repository.OspedaleRemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject


class GetOspedaliUseCase @Inject constructor(
    private val remoteRepo: OspedaleRemoteRepository, // ottiene così dati da internet tramite Retrofit
    private val localRepo: OspedaleLocalRepository  // accede ai dati locali tramite Room
) {
    // invoke permette di chiamare la classe come se fosse una funzione, in questo caso restituisce un flow di resource ospedale
    operator fun invoke(): Flow<Resource<List<Ospedale>>> {

        return flow{
            emit(Resource.Loading("Caricamento..."))


            // verifichiamo che nel db ci siano i nostri dati
            // con il catch catturiamo errori nel flow, se presenti
            // restituiamo come flow Resource.error
            localRepo.getAll()
                .catch{
                emit(Resource.Error(message = "Errore: dati non trovati nel database locale"))
            }
                // raccolgo i dati se tutto va a buon fine
                .collect{ list ->
                    // se la lista di ospedali è vuota vado su internet a recuperare i dati
                    if (list.isEmpty()){

                        // Remote request
                        // stiamo usando retrofit di cui non stiamo gestendo gli errori
                        // quindi usiamo il costrutto try catch
                        try{
                            // proviamo a reperire i dati dal datasource remoto
                            val data = remoteRepo.getOspedali()

                            // li inseriamo nel database locale
                            localRepo.insert(data)
                            // restituiamo i dati
                            emit(Resource.Success(data = data))

                        } catch (e: HttpException){
                            e.printStackTrace() // stampa l'errore su console
                            emit(Resource.Error(message = "Error from server"))

                        }
                    } else{
                        // altrimenti ci sono già dati nel database locale
                        emit(Resource.Success(data = list))
                    }

                }
        }
    }

}