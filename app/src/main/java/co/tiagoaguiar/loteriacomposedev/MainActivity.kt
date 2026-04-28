package co.tiagoaguiar.loteriacomposedev

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import co.tiagoaguiar.loteriacomposedev.compose.LoteriaApp
import co.tiagoaguiar.loteriacomposedev.ui.theme.LoteriaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LoteriaTheme {
                LoteriaApp()
            }
        }
    }
}
