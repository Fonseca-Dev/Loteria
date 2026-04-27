package co.tiagoaguiar.loteriacomposedev.compose.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.tiagoaguiar.loteriacomposedev.R
import co.tiagoaguiar.loteriacomposedev.model.MainItem
import co.tiagoaguiar.loteriacomposedev.ui.component.LoItemType
import co.tiagoaguiar.loteriacomposedev.ui.theme.Blue
import co.tiagoaguiar.loteriacomposedev.ui.theme.Green
import co.tiagoaguiar.loteriacomposedev.ui.theme.LoteriaTheme

@Composable
fun HomeScreen(onClick: (MainItem) -> Unit) {
    val mainItems = mutableListOf(
        MainItem(1, "Mega Sena", Green, R.drawable.trevo),
        MainItem(2, "Quina", Blue, R.drawable.trevo_blue)
    )
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyVerticalGrid(
            verticalArrangement = Arrangement.SpaceAround,
            columns = GridCells.Fixed(2)
        ) {
            items(mainItems) {
                LotteryItem(it) {
                    onClick(it)
                }
            }
        }
    }
}

@Composable
fun LotteryItem(item: MainItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .wrapContentSize()
            .clickable {
                onClick()
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
        ) {
            LoItemType(
                item.name,
                icon = item.icon,
                color = Color.White,
                bgColor = item.color
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    LoteriaTheme {
        HomeScreen(onClick = {})
    }
}