package io.vendator.dav.calculate.room

import androidx.room.*
import java.util.*

@Entity(tableName = "game_stat_table")
@TypeConverters(DateConverter::class)
data class GameStat(
    @PrimaryKey(autoGenerate = true) var uid: Int,
    @ColumnInfo(name = "difficulty") var difficultyLevel: Int,
    @ColumnInfo(name = "date") var date: Date,
    @ColumnInfo(name = "accuracy") var accuracy: Float,
    @ColumnInfo(name = "average_time") var averageTime: Long,
    @ColumnInfo(name = "totalProblem") var totalProblem: Int
)

class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}