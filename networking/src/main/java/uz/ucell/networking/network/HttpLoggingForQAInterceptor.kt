package uz.ucell.networking.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import uz.ucell.networking.logging_db.LogInfo
import uz.ucell.networking.logging_db.LogsDao
import uz.ucell.networking_storage.NetworkStorage
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

internal class HttpLoggingForQAInterceptor(
    private val storage: NetworkStorage,
    private val logsDao: LogsDao
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            throw e
        }

        if (!storage.getRequestsLoggingEnabled()) {
            return response
        }

        val strBuilderBase = StringBuilder()
        val strBuilderDetail = StringBuilder()
        val headers = request.headers

        //region header info
        val responseBody = response.body!!
        val contentLength = responseBody.contentLength()

        val method = request.method
        val url = request.url.pathSegments.joinToString(separator = "/")
        val code = "${response.code}"
        val source = responseBody.source()
        source.request(Long.MAX_VALUE) // Buffer the entire body.
        val buffer = source.buffer

        val contentType = responseBody.contentType()
        val charset: Charset =
            contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8

        strBuilderBase.append(method)
        strBuilderBase.append(" ")
        strBuilderBase.append(url)
        strBuilderBase.append("\n")
        strBuilderBase.append(code)
        strBuilderBase.append(" ")
        val body = if (contentLength != 0L) {
            buffer.clone().readString(charset)
        } else ""
        strBuilderBase.append(body)
        //endregion

        //region request
        strBuilderDetail.append("\n")
        strBuilderDetail.append("========== Request")
        val requestMethod = request.method
        val requestUrl = "${request.url.toUrl()}"
        val requestHeaders =
            headers.joinToString(separator = "\n") { (headerName, value) -> "$headerName: $value" }
        strBuilderDetail.append("\n")
        strBuilderDetail.append(requestMethod)
        strBuilderDetail.append(" ")
        strBuilderDetail.append(requestUrl)
        strBuilderDetail.append("\n")
        strBuilderDetail.append(requestHeaders)
        //endregion

        //region response
        strBuilderDetail.append("\n")
        strBuilderDetail.append("========== Response")
        strBuilderDetail.append("\n")
        strBuilderDetail.append(code)
        strBuilderDetail.append("\n")
        val responseHeaders =
            response.headers.joinToString(separator = "\n") { (headerName, value) -> "$headerName: $value" }
        strBuilderDetail.append(responseHeaders)
        strBuilderDetail.append("\n")
        strBuilderDetail.append("Body:")
        strBuilderDetail.append("\n")
        strBuilderDetail.append(body)
        //endregion

        CoroutineScope(Dispatchers.IO).launch {
            logsDao.insert(
                LogInfo(
                    base = strBuilderBase.toString(),
                    detail = strBuilderDetail.toString()
                )
            )
        }
        return response
    }
}
