package uz.ucell.networking.network

import androidx.core.net.toUri
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import uz.ucell.networking_storage.NetworkStorage
import java.util.UUID

internal class CommonInterceptor(val storage: NetworkStorage) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val headers = Headers.Builder()
            .addAll(NetworkConstants.commonHeaders)
            .add(NetworkConstants.HeadersName.REQUEST_ID.value, UUID.randomUUID().toString())
            .add(NetworkConstants.HeadersName.LANGUAGE.value, storage.getLanguage())
            .add(NetworkConstants.HeadersName.DEVICE_ID.value, storage.getDeviceId())
            .add(NetworkConstants.HeadersName.USER_AGENT.value, storage.getUserAgent())
            .add(NetworkConstants.HeadersName.DEV_TOKEN.value, storage.getDevToken())
            .build()

        val newUrl = request.url.newBuilder()
            .apply {
                storage.getHost()?.toUri()?.host?.let { host ->
                    host(host)
                }
            }
            .build()

        request = request.newBuilder()
            .headers(headers)
            .url(newUrl)
            .build()
        return chain.proceed(request)
    }

    private fun Headers.Builder.add(name: String, value: String?) = apply {
        if (value.isNullOrEmpty()) return@apply
        add(name, value)
    }
}
