package io.vendator.dav.calculate.room

import android.app.Application
import android.content.Context
import androidx.room.Room

class DatabaseProvider(var context: Context) {
    val db by lazy {
        Room.databaseBuilder(
            context,
            GameStatDb::class.java, "game-stat-db"
        ).build()
    }
}