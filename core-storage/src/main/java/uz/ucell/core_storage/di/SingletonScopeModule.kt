package uz.ucell.core_storage.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.ucell.core_storage.CoreStorageImpl
import uz.ucell.core_storage.ProfileStorageImpl
import uz.ucell.core_storage.api.CoreStorage
import uz.ucell.core_storage.api.ProfileStorage

@Module
@InstallIn(SingletonComponent::class)
abstract class SingletonScopeModule {
    @Binds
    abstract fun bindCoreStorage(impl: CoreStorageImpl): CoreStorage

    @Binds
    abstract fun bindProfileStorage(impl: ProfileStorageImpl): ProfileStorage
}
