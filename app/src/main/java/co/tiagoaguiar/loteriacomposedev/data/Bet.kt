package co.tiagoaguiar.loteriacomposedev.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "bet")
data class Bet(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: String,
    val numbers: String,
    val date: Date = Date()
)
