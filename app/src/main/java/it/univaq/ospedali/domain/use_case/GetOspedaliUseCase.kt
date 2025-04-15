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
    private val remoteRepo: OspedaleRemoteRepository,
    private val localRepo: OspedaleLocalRepository
) {
    // invoke permette di chiamare la classe come se fosse una funzione, in questo caso restituisce un flow di resource ospedale
    operator fun invoke(): Flow<Resource<List<Ospedale>>> {

        return flow{
            emit(Resource.Loading("Loading..."))

            // verifichiamo che nel db ci siano i nostri dati
            // con il catch andiamo a gestire gli errori nel flow

            localRepo.getAll()
                .catch{
                emit(Resource.Error(message = "Error: data not found in local database"))
            }
                // raccolgo i dati se tutto va a buon fine
                .collect{ list ->
                    // se la lista di ospedali è vuota vado su internet a recuperare i dati
                    if (list.isEmpty()){
                        // Remote request
                        // stiamo usando retrofit di cui non stiamo gestendo gli errori quindi usiamo il costrutto try catch

                        try{
                            val data = remoteRepo.getOspedali()  // otteniamo i dati

                            localRepo.insert(data)  // li inseriamo nel database
                            emit(Resource.Success(data = data)) // restituiamo i dati

                        } catch (e: HttpException){
                            e.printStackTrace() // stampa l'errore su console
                            emit(Resource.Error(message = "Error from server"))

                        }


                    } else{
                        // data in db
                        emit(Resource.Success(data = list))
                    }

                }
        }
    }

}