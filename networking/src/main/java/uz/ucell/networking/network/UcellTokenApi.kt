package uz.ucell.networking.network

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import uz.ucell.networking.network.response.TokenResponse

interface UcellTokenApi {
    companion object {
        private const val REQUEST_REFRESH_TOKEN = "/oauth2/token"

        const val KEY_CLIENT_ID = "client_id"
        const val KEY_CLIENT_SECRET = "client_secret"
        const val KEY_GRANT_TYPE = "grant_type"
        const val KEY_SCOPE = "scope"
        const val KEY_REFRESH_TOKEN = "refresh_token"
    }

    @FormUrlEncoded
    @POST(REQUEST_REFRESH_TOKEN)
    fun refreshAccessToken(
        @Field(KEY_CLIENT_ID) clientId: String,
        @Field(KEY_CLIENT_SECRET) clientSecret: String,
        @Field(KEY_GRANT_TYPE) grantType: String,
        @Field(KEY_SCOPE) scope: String,
        @Field(KEY_REFRESH_TOKEN) refreshtoken: String,
    ): Call<TokenResponse>
}
