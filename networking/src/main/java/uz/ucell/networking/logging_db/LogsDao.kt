package uz.ucell.networking.logging_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LogsDao {

    @Query("SELECT * FROM logs")
    suspend fun getAll(): List<LogInfo>

    @Insert
    suspend fun insert(log: LogInfo)

    @Query("DELETE from logs")
    suspend fun deleteAll()
}
