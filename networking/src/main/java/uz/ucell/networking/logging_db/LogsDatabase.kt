package uz.ucell.networking.logging_db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LogInfo::class], version = 1)
abstract class LogsDatabase : RoomDatabase() {
    abstract fun logsDao(): LogsDao
}
