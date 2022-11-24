package uz.ucell.networking.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.ucell.networking.logging_db.LogsDao
import uz.ucell.networking.network.AuthInterceptor
import uz.ucell.networking.network.CommonInterceptor
import uz.ucell.networking.network.HttpLoggingForQAInterceptor
import uz.ucell.networking.network.UcellTokenApi
import uz.ucell.networking_storage.NetworkStorage
import javax.inject.Singleton

@Module(includes = [LoggingDatabaseModule::class])
@InstallIn(SingletonComponent::class)
internal object InterceptorModule {

    @Provides
    @Singleton
    internal fun provideAuthInterceptor(
        storage: NetworkStorage,
        api: UcellTokenApi
    ): AuthInterceptor =
        AuthInterceptor(storage, api)

    @Provides
    @Singleton
    internal fun provideCommonInterceptor(storage: NetworkStorage): CommonInterceptor =
        CommonInterceptor(storage)

    @Singleton
    @Provides
    fun provideUcellHttpLoggingInterceptor(
        storage: NetworkStorage,
        logsDao: LogsDao
    ): HttpLoggingForQAInterceptor =
        HttpLoggingForQAInterceptor(storage, logsDao)
}
