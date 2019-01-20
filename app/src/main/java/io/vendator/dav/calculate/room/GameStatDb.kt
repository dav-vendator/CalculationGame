package io.vendator.dav.calculate.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GameStat::class], version = 1, exportSchema = false)
abstract class GameStatDb : RoomDatabase() {
    abstract fun gameStatDao(): CalculationDao
}