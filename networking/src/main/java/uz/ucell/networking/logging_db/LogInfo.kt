package uz.ucell.networking.logging_db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "logs")
data class LogInfo(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    @ColumnInfo(name = "base")
    val base: String? = null,
    @ColumnInfo(name = "detail")
    val detail: String? = null
)
