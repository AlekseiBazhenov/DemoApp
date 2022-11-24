package uz.ucell.networking_storage

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkingStorageModule {
    @Singleton
    @Provides
    fun providerNetworking(@ApplicationContext context: Context): NetworkStorage {
        val sharedPreferences = context.getSharedPreferences(BuildConfig.NET_PREFS_NAME, Context.MODE_PRIVATE)
        return NetworkStorageImpl(sharedPreferences)
    }
}
