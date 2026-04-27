package co.tiagoaguiar.loteriacomposedev.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

data class MainItem(
    val id: Int,
    val name: String,
    val color: Color,
    @DrawableRes val icon: Int
)
