package co.tiagoaguiar.loteriacomposedev.compose.detail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import co.tiagoaguiar.loteriacomposedev.ui.theme.LoteriaTheme

@Composable
fun BetListDetailScreen(type: String){
    Text(text = "Tipo de dado: $type")
}

@Preview(showBackground = true)
@Composable
fun BetListDetailScreenPreview(){
    LoteriaTheme{
        BetListDetailScreen(type = "megasena")
    }
}