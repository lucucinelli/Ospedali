package it.univaq.ospedali.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import it.univaq.ospedali.common.BASE_URL
import it.univaq.ospedali.data.local.OspedaleDatabase
import it.univaq.ospedali.data.local.OspedaleRoomRepository
import it.univaq.ospedali.data.remote.OspedaleRetrofitRepository
import it.univaq.ospedali.data.remote.OspedaleService
import it.univaq.ospedali.domain.repository.OspedaleLocalRepository
import it.univaq.ospedali.domain.repository.OspedaleRemoteRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module // contiene la logica per la creazione delle dipendenze sulla base delle quali si creano oggetti che usano @inject contructor
@InstallIn(SingletonComponent::class)  // la logica vale durante tutta la durata dell'app
object RetrofitModule { // il modulo è un singleton

    // generiamo un Client Retrofit
    @Provides // serve per il dependency injection
    @Singleton // in questo modo abbiamo un'unica istanza del nostro client
    // in grado di convertire stringhe del json in oggetti kotlin
    fun retrofitClient(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // realizzazione dell'ospedaleservice tramite il dependency injection
    @Provides
    @Singleton
    // ospedaleService rappresenta l'interfaccia di Retrofit che
    // effettua delle richieste al server per ricava dati che tipizza
    fun ospedaleService(retrofit: Retrofit): OspedaleService =
        // ::class, l'interfaccia viene trasformata in una classe da Retrofit
        // .java fa in modo che il KClass sia convertito in una classe Java
        retrofit.create(OspedaleService::class.java)

}


@Module // definisce la logica sulla base della quale crea le dipendenze
@InstallIn(SingletonComponent::class) // fa valere la logica per tutta la durata dell'app
//
abstract class RepositoryModule{

    // creo una funzione che restituisca un oggetto della classe indicata sulla base della classe OspedaleRetrofitRepository
    @Binds // anzichè Provides, dato che la classe è astratta
    @Singleton
    abstract fun remoteRepository(repository: OspedaleRetrofitRepository): OspedaleRemoteRepository

    @Binds // anzichè Provides, dato che la classe è astratta
    @Singleton
    abstract fun localRepository(repository: OspedaleRoomRepository): OspedaleLocalRepository
}

@Module // logica sulla base della quale si crea il database
@InstallIn(SingletonComponent::class) // logica valida per tutta la durata dell'app
object DatabaseModule { // object quindi è singleton

    @Provides // implementa il DI
    @Singleton // in questo modo abbiamo un'unica istanza del nostro database
    // restituisce un database di tipo ospedaledatabase e Applicationcontext mi permette di ottenere il contesto dell'applicazione
    fun database(@ApplicationContext context: Context): OspedaleDatabase =
        Room.databaseBuilder( // metodo che permette di costruire un database con i seguenti valori in ingresso
            context,
            OspedaleDatabase::class.java,  // classe astratta definita con @Database
            "ospedali_database"   // nome del database da creare in memoria
        )
            .build() // metodo che crea il database

    @Provides // implementa il DI
    @Singleton // dao singleton
    // restituisce un ospedaledao ricevendo in ingresso il database creato
    // la creazione la effettua la libreria room insieme a hilt per le dipendenze
    fun ospedaleDao(database: OspedaleDatabase) = database.getOspedaleDao()
}