package uz.ucell.appmetrica.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.ucell.appmetrica.api.AppMetricEvents
import uz.ucell.appmetrica.api.AppMetricProfile
import uz.ucell.appmetrica.api.impl.AppMetricEventsImpl
import uz.ucell.appmetrica.api.impl.AppMetricProfileImpl
import uz.ucell.appmetrica.utils.NavigationHandler
import uz.ucell.appmetrica.utils.NavigationHandlerImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingletonScopeModule {
    @Singleton
    @Provides
    fun provideProfileMetric(): AppMetricProfile {
        return AppMetricProfileImpl()
    }

    @Singleton
    @Provides
    fun provideEventMetric(): AppMetricEvents {
        return AppMetricEventsImpl()
    }

    @Singleton
    @Provides
    fun provideNavigationHandler(): NavigationHandler {
        return NavigationHandlerImpl()
    }
}
