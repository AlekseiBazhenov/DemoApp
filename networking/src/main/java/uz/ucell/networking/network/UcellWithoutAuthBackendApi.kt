package uz.ucell.networking.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query
import uz.ucell.networking.network.response.OtpResponse
import uz.ucell.networking.network.response.TokenResponse

internal interface UcellWithoutAuthBackendApi {
    private companion object {
        const val AUTH_PATH = "/api/oauth2/token"
        const val LOGOUT = "/api/oauth2/revoke"
        const val OTP_REQUEST = "/api/auth/credentials/otpRequest"
        const val CAPTCHA_REQUEST = "/api/auth/captcha/next"

        const val KEY_MSISDN_FIELD = "msisdn"
        const val KEY_CLIENT_ID = "client_id"
        const val KEY_CLIENT_SECRET = "client_secret"
        const val KEY_GRANT_TYPE = "grant_type"
        const val KEY_SCOPE = "scope"
        const val KEY_USERNAME = "username"
        const val KEY_PASSWORD = "password"
        const val KEY_PASSWORD_TYPE = "password_type"
        const val KEY_CAPTCHA = "captcha"
        const val KEY_TOKEN = "token"
    }

    @FormUrlEncoded
    @POST(AUTH_PATH)
    suspend fun auth(
        @Field(KEY_CLIENT_ID) clientId: String,
        @Field(KEY_CLIENT_SECRET) clientSecret: String,
        @Field(KEY_GRANT_TYPE) grantType: String,
        @Field(KEY_SCOPE) scope: String,
        @Field(KEY_USERNAME) username: String,
        @Field(KEY_PASSWORD) password: String,
        @Field(KEY_PASSWORD_TYPE) passwordType: String
    ): TokenResponse

    @FormUrlEncoded
    @POST(LOGOUT)
    suspend fun logout(
        @Field(KEY_CLIENT_ID) clientId: String,
        @Field(KEY_CLIENT_SECRET) clientSecret: String,
        @Field(KEY_TOKEN) refreshToken: String
    )

    @POST(OTP_REQUEST)
    suspend fun otpRequest(
        @Query(KEY_MSISDN_FIELD) msisdn: String,
        @Query(KEY_CAPTCHA) captcha: String? = null
    ): OtpResponse

    @POST(CAPTCHA_REQUEST)
    suspend fun captchaRequest(): Response<ResponseBody>
}
