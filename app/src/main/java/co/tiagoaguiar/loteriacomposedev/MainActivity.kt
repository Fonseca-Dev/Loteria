package co.tiagoaguiar.loteriacomposedev

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import co.tiagoaguiar.loteriacomposedev.compose.LoteriaApp
import co.tiagoaguiar.loteriacomposedev.data.AppDatabase
import co.tiagoaguiar.loteriacomposedev.data.Bet
import co.tiagoaguiar.loteriacomposedev.ui.theme.LoteriaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getInstance(this)
        val bet = Bet(type = "mega", numbers = "1,2,3,4,5,6")
        Thread {
            db.betDao().insert(bet)
        }.start()

        setContent {
            LoteriaTheme {
                LoteriaApp()
            }
        }
    }
}
