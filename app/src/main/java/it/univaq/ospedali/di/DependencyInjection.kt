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
@InstallIn(SingletonComponent::class)  // come si comporta il modulo in tutta l'app, in questo caso come singleton
object RetrofitModule {

    // generiamo un Client Retrofit

    @Provides // serve per il dependency injection
    @Singleton // in questo modo abbiamo un'unica istanza del nostro client
    // in grado di convertire da stringhe del json a oggetti kotlin
    fun retrofitClient(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // realizzazione dell'ospedaleservice tramite il dependency injection



    @Provides
    @Singleton
    // tramite Retrofit ospedaleService effettua delle richieste al server da cui ricava dati per l'app
    fun ospedaleService(retrofit: Retrofit): OspedaleService =
        retrofit.create(OspedaleService::class.java)

}

// per far coincidere le istanze dell'interfaccia con quelle di retrofit, essendo essa stessa una implementazione dell'altra
// dobbiamo utilizzare un modulo astratto

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule{

    // creo una funzione che restituisca un oggetto della classe indicata sulla base della classe OspedaleRetrofitRepository
    @Binds // anzichè Provides, dato che la classe è astratta
    @Singleton
    abstract fun remoteRepository(repository: OspedaleRetrofitRepository): OspedaleRemoteRepository

    @Binds // anzichè Provides, dato che la classe è astratta
    @Singleton
    abstract fun localRepository(repository: OspedaleRoomRepository): OspedaleLocalRepository

}

@Module // modulo che permette di inserire le dipendenze
@InstallIn(SingletonComponent::class) // come si comporta il modulo in tutta l'app, in questo caso come singleton
object DatabaseModule {

    @Provides
    @Singleton
    // restituisce un database di tipo ospedaledatabase e Applicationcontext mi permette di ottenere il contesto dell'applicazione
    fun database(@ApplicationContext context: Context): OspedaleDatabase =
        Room.databaseBuilder( // metodo che permette di costruire un database con i seguenti valori in ingresso
            context,
            OspedaleDatabase::class.java,
            "ospedali_database"   // nome del database da creare in memoria
        )
            .build() // metodo che crea il database

    @Provides
    @Singleton
    // restituisce un ospedaledao ricevendo in ingresso il database creato
    fun ospedaleDao(database: OspedaleDatabase) = database.getOspedaleDao()
}