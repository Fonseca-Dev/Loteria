package co.tiagoaguiar.loteriacomposedev.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BetDao {

    @Query("SELECT * FROM bet WHERE type = :betType")
    fun getNumbersByType(betType: String):List<Bet>

    @Insert
    fun insert(bet: Bet)
}