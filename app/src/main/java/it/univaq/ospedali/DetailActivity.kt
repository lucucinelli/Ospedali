package it.univaq.ospedali

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import it.univaq.ospedali.ui.screen.detail.DetailScreen
import it.univaq.ospedali.ui.theme.OspedaliTheme

@AndroidEntryPoint // permette di utilizzare il dependency injection
class DetailActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OspedaliTheme {
                // recupera i dati passati tramite l'intent che ha startato l'activity
                val comune = intent.getStringExtra("comune")
                val provincia = intent.getStringExtra("provincia")
                val regione = intent.getStringExtra("regione")

                // mostra la schermata di dettaglio con i dati passati
                DetailScreen(
                    modifier = Modifier,
                    comune = comune,
                    provincia = provincia,
                    regione = regione
                )
            }
        }
    }
}