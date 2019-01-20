package io.vendator.dav.calculate.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CalculationDao {

    @Query("SELECT * FROM game_stat_table WHERE `difficulty`=:difficulty ORDER BY date ASC")
    fun getAllGamesWith(difficulty: Int): List<GameStat>

    @Insert
    fun insertAll(vararg games: GameStat)

    @Delete
    fun delete(game: GameStat): Int

    @Query("DELETE FROM game_stat_table WHERE `difficulty`=:difficulty")
    fun deleteAllWith(difficulty: Int): Int

}