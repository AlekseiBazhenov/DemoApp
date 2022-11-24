package uz.ucell.appmetrica.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uz.ucell.appmetrica.api.useCase.AuthAnalyticsUseCase
import uz.ucell.appmetrica.api.useCase.AuthAnalyticsUseCaseImpl
import uz.ucell.appmetrica.api.useCase.ChoiceLangAnalyticsUseCase
import uz.ucell.appmetrica.api.useCase.ChoiceLangAnalyticsUseCaseImpl
import uz.ucell.appmetrica.api.useCase.EnterPhoneAnalyticsUseCase
import uz.ucell.appmetrica.api.useCase.EnterPhoneAnalyticsUseCaseImpl
import uz.ucell.appmetrica.api.useCase.EnterSmsAnalyticsUseCase
import uz.ucell.appmetrica.api.useCase.EnterSmsAnalyticsUseCaseImpl
import uz.ucell.appmetrica.api.useCase.PinConfAnalyticsUseCase
import uz.ucell.appmetrica.api.useCase.PinConfAnalyticsUseCaseImpl
import uz.ucell.appmetrica.api.useCase.PinCreationAnalyticsUseCase
import uz.ucell.appmetrica.api.useCase.PinCreationAnalyticsUseCaseImpl
import uz.ucell.appmetrica.api.useCase.SendProfileMetricUseCase
import uz.ucell.appmetrica.api.useCase.SendProfileMetricUseCaseImpl
import uz.ucell.appmetrica.utils.ProfileMapper

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelScopeBinder {
    @Binds
    abstract fun bindSendProfileUserCase(impl: SendProfileMetricUseCaseImpl): SendProfileMetricUseCase

    @Binds
    abstract fun bindAuthAnalytics(impl: AuthAnalyticsUseCaseImpl): AuthAnalyticsUseCase

    @Binds
    abstract fun bindChoiceLangAnalytics(impl: ChoiceLangAnalyticsUseCaseImpl): ChoiceLangAnalyticsUseCase

    @Binds
    abstract fun bindEnterPhoneAnalytics(impl: EnterPhoneAnalyticsUseCaseImpl): EnterPhoneAnalyticsUseCase

    @Binds
    abstract fun bindEnterSmsAnalytics(impl: EnterSmsAnalyticsUseCaseImpl): EnterSmsAnalyticsUseCase

    @Binds
    abstract fun bindPinConfAnalytics(impl: PinConfAnalyticsUseCaseImpl): PinConfAnalyticsUseCase

    @Binds
    abstract fun bindPinCreationAnalytics(impl: PinCreationAnalyticsUseCaseImpl): PinCreationAnalyticsUseCase
}

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelScopeProvider {
    @Provides
    fun provideProfileMapper(): ProfileMapper = ProfileMapper()
}
