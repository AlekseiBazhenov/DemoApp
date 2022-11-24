package uz.ucell.networking.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.ucell.networking.BuildConfig
import uz.ucell.networking.network.AuthInterceptor
import uz.ucell.networking.network.CommonInterceptor
import uz.ucell.networking.network.HttpLoggingForQAInterceptor
import uz.ucell.networking.network.NetworkConstants
import uz.ucell.networking.network.UcellBackendApi
import uz.ucell.networking.network.UcellTokenApi
import uz.ucell.networking.network.UcellWithoutAuthBackendApi
import uz.ucell.networking_storage.NetworkStorage
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module(includes = [InterceptorModule::class])
@InstallIn(SingletonComponent::class)
internal object RetorofitModule {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class TokenApi

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CommonAuthApi

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CommonWithoutAuthApi

    @CommonAuthApi
    @Provides
    @Singleton
    internal fun getOkHttpClient(
        networkingStorage: NetworkStorage,
        authInterceptor: AuthInterceptor,
        commonInterceptor: CommonInterceptor,
        networkLoggingForQA: HttpLoggingForQAInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(commonInterceptor)
        .authenticator(authInterceptor)
        .connectTimeout(NetworkConstants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(NetworkConstants.READ_TIMEOUT, TimeUnit.SECONDS)
        .followRedirects(true)
        .followSslRedirects(true)
        .addInterceptor { chain ->
            val newRequest = chain.request().newBuilder().run {
                networkingStorage.getAccessToken().token?.let {
                    addHeader("Authorization", it)
                }
                this
            }.build()
            chain.proceed(newRequest)
        }
        .addInterceptor(loggingInterceptor)
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(networkLoggingForQA)
            }
        }
        .build()

    @CommonWithoutAuthApi
    @Provides
    @Singleton
    internal fun getOkHttpClientWithoutAuthToken(
        commonInterceptor: CommonInterceptor,
        networkLoggingForQA: HttpLoggingForQAInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(commonInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(NetworkConstants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(NetworkConstants.READ_TIMEOUT, TimeUnit.SECONDS)
        .followRedirects(true)
        .followSslRedirects(true)
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(networkLoggingForQA)
            }
        }
        .build()

    @TokenApi
    @Provides
    @Singleton
    internal fun getTokenOkHttpClient(
        commonInterceptor: CommonInterceptor,
        networkLoggingForQA: HttpLoggingForQAInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(commonInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(NetworkConstants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(NetworkConstants.READ_TIMEOUT, TimeUnit.SECONDS)
        .followRedirects(true)
        .followSslRedirects(true)
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(networkLoggingForQA)
            }
        }
        .build()

    @Singleton
    @Provides
    internal fun provideCommonRetrofitWithoutAuth(@CommonWithoutAuthApi client: OkHttpClient): UcellWithoutAuthBackendApi =
        Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UcellWithoutAuthBackendApi::class.java)

    @Singleton
    @Provides
    internal fun provideCommonRetrofit(@CommonAuthApi client: OkHttpClient): UcellBackendApi =
        Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UcellBackendApi::class.java)

    @Singleton
    @Provides
    internal fun provideTokenRetrofit(@TokenApi client: OkHttpClient): UcellTokenApi =
        Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UcellTokenApi::class.java)
}
