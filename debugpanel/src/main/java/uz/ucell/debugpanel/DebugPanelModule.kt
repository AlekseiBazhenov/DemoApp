package uz.ucell.debugpanel

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DebugPanelModule {

    @Singleton
    @Provides
    fun provideShakeListener(@ApplicationContext context: Context): ShakeListener =
        ShakeListenerImpl(context)
}
