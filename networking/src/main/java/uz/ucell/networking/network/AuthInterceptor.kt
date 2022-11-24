package uz.ucell.networking.network

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import uz.ucell.networking.BuildConfig
import uz.ucell.networking.exceptions.RefreshTokenExpired
import uz.ucell.networking_storage.NetworkStorage

internal class AuthInterceptor(private val storage: NetworkStorage, private val api: UcellTokenApi) :
    Authenticator {
    override fun authenticate(route: Route?, response: Response): Request {
        val updatedToken = getUpdatedToken()
        return response.request.newBuilder()
            .header(NetworkConstants.HeadersName.ACCESS_TOKEN.value, updatedToken)
            .build()
    }

    @Throws(RefreshTokenExpired::class)
    private fun getUpdatedToken(): String {
        val authTokenResponse = api.refreshAccessToken(
            clientId = NetworkConstants.CLIENT_ID,
            clientSecret = if (BuildConfig.DEBUG) NetworkConstants.CLIENT_SECRET_DEBUG else "", // todo в build.gradle
            grantType = NetworkConstants.GrantType.PASSWORD.value,
            scope = NetworkConstants.Scope.LK_WRITE.value,
            refreshtoken = NetworkConstants.HeadersName.REFRESH_TOKEN.value
        ).execute()
        val newToken = authTokenResponse.body()?.let {
            "${it.tokenType} ${it.accessToken}"
        }
        if (!authTokenResponse.isSuccessful || newToken.isNullOrEmpty()) {
            throw RefreshTokenExpired(
                authTokenResponse.code().toString(),
                authTokenResponse.message()
            )
        }
        storage.setAccessToken(newToken)
        return newToken
    }
}
