package uz.ucell.smsretriever_consent

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SmsRetrieverModule {

    @Singleton
    @Provides
    fun providerSmsManager(@ApplicationContext context: Context): SmsConsentManager {
        return SmsConsentManagerImpl(context)
    }
}
