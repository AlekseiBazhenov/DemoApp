package uz.ucell.networking.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.ucell.networking.logging_db.LogsDao
import uz.ucell.networking.logging_db.LogsDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LoggingDatabaseModule {

    @Provides
    @Singleton
    fun provideLogsDatabase(@ApplicationContext context: Context): LogsDatabase {
        return Room.databaseBuilder(
            context,
            LogsDatabase::class.java,
            "logs.db"
        ).build()
    }

    @Provides
    fun provideLogsDao(appDatabase: LogsDatabase): LogsDao {
        return appDatabase.logsDao()
    }
}
