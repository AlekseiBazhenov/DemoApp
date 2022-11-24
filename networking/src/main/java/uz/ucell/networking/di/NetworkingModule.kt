package uz.ucell.networking.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.ucell.networking.NetworkingInterface
import uz.ucell.networking.SetupNetworkInterface
import uz.ucell.networking.network.NetworkImpl
import uz.ucell.networking.network.NetworkSetupImp
import uz.ucell.networking.network.UcellBackendApi
import uz.ucell.networking.network.UcellTokenApi
import uz.ucell.networking.network.UcellWithoutAuthBackendApi
import uz.ucell.networking_storage.NetworkStorage
import javax.inject.Singleton

@Module(includes = [RetorofitModule::class])
@InstallIn(SingletonComponent::class)
internal object NetworkingModule {

    @Singleton
    @Provides
    internal fun provideNetworking(
        @ApplicationContext context: Context,
        authApi: UcellBackendApi,
        withoutAuthApi: UcellWithoutAuthBackendApi,
        refreshTokenApi: UcellTokenApi,
        storage: NetworkStorage
    ): NetworkingInterface =
        NetworkImpl(context, authApi, withoutAuthApi, refreshTokenApi, storage)

    @Singleton
    @Provides
    internal fun provideSetupNetwork(
        storage: NetworkStorage
    ): SetupNetworkInterface = NetworkSetupImp(storage)
}
