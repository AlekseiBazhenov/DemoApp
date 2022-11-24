package uz.ucell.networking.network

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import uz.ucell.networking.BuildConfig
import uz.ucell.networking.NetworkingInterface
import uz.ucell.networking.R
import uz.ucell.networking.data.ApiResponse
import uz.ucell.networking.data.ErrorBody
import uz.ucell.networking.exceptions.INTERNAL_ERROR
import uz.ucell.networking.network.dto.Captcha
import uz.ucell.networking.network.dto.TokenExpiresTime
import uz.ucell.networking.network.request_body.AddNewCard
import uz.ucell.networking.network.request_body.BiometryToken
import uz.ucell.networking.network.request_body.CheckNewCard
import uz.ucell.networking.network.request_body.Pay
import uz.ucell.networking.network.request_body.PayOrder
import uz.ucell.networking.network.request_body.PinCode
import uz.ucell.networking.network.response.BalanceResponse
import uz.ucell.networking.network.response.CheckNewCardResponse
import uz.ucell.networking.network.response.LinkedCardsResponse
import uz.ucell.networking.network.response.OtpResponse
import uz.ucell.networking.network.response.PayOrderResponse
import uz.ucell.networking.network.response.PayResponse
import uz.ucell.networking.network.response.PaymentLimitsResponse
import uz.ucell.networking.network.response.PaymentRecommendationsResponse
import uz.ucell.networking.network.response.ProfileResponse
import uz.ucell.networking.network.response.TariffResponse
import uz.ucell.networking_storage.NetworkStorage
import java.net.UnknownHostException

internal class NetworkImpl(
    private val context: Context,
    private val api: UcellBackendApi,
    private val withoutAuthApi: UcellWithoutAuthBackendApi,
    private val refreshTokenApi: UcellTokenApi,
    private val storage: NetworkStorage
) : NetworkingInterface {

    override suspend fun otpRequest(
        msisdn: String,
        captcha: String?,
        localization: String?
    ): ApiResponse<OtpResponse> =
        execute { withoutAuthApi.otpRequest(msisdn = msisdn, captcha = captcha) }

    override suspend fun authorization(
        username: String,
        password: String,
        passwordType: PasswordType,
        localization: String?
    ): ApiResponse<TokenExpiresTime> {
        return when (
            val response = execute {
                withoutAuthApi.auth(
                    clientId = NetworkConstants.CLIENT_ID,
                    clientSecret = if (BuildConfig.DEBUG) NetworkConstants.CLIENT_SECRET_DEBUG else "", // todo в build.gradle
                    grantType = NetworkConstants.GrantType.PASSWORD.value,
                    scope = NetworkConstants.Scope.LK_WRITE.value,
                    username = username,
                    password = password,
                    passwordType = passwordType.value
                )
            }
        ) {
            is ApiResponse.Success -> {
                storage.setAccessToken("${response.value.tokenType} ${response.value.accessToken}")
                storage.setRefreshToken(response.value.refreshToken)
                ApiResponse.Success(TokenExpiresTime(response.value.expireTime))
            }
            is ApiResponse.Error -> {
                response
            }
        }
    }

    override suspend fun logout(): ApiResponse<Unit> = execute {
        withoutAuthApi.logout(
            clientId = NetworkConstants.CLIENT_ID,
            clientSecret = if (BuildConfig.DEBUG) NetworkConstants.CLIENT_SECRET_DEBUG else "", // todo в build.gradle
            refreshToken = storage.getRefreshToken().token!!
        )
    }

    // TODO подумать, возможно стоит заменить на какой-нибудь image loader
    override suspend fun getCaptcha(): ApiResponse<Captcha> {
        return when (val response = execute { withoutAuthApi.captchaRequest() }) {
            is ApiResponse.Success -> {
                val byteArray = response.value.body()?.run { bytes() }
                byteArray?.let {
                    ApiResponse.Success(Captcha(it))
                } ?: ApiResponse.Error(true, null)
            }
            is ApiResponse.Error -> {
                response
            }
        }
    }

    override suspend fun createPinCode(pin: PinCode): ApiResponse<Unit> =
        execute { api.createPin(pin) }

    override suspend fun createBiometryToken(token: BiometryToken): ApiResponse<Unit> =
        execute { api.createBiometryToken(token) }

    override suspend fun myTariff(): ApiResponse<TariffResponse> = execute { api.myTariff() }

    override suspend fun getAvailableTariffs(): ApiResponse<List<TariffResponse>> = execute { api.getAvailableTariffsList().ratePlans }

    override suspend fun getBalance(): ApiResponse<BalanceResponse> = execute { api.getBalance() }

    override suspend fun getUserProfile(): ApiResponse<ProfileResponse> =
        execute { api.getProfile() }

    override suspend fun getPaymentRecommendations(isOtherMsisdn: Boolean): ApiResponse<PaymentRecommendationsResponse> =
        execute { api.getPaymentRecommendations(isOtherMsisdn) }

    override suspend fun getPaymentLimits(): ApiResponse<PaymentLimitsResponse> =
        execute { api.getPaymentLimits(DEFAULT_PROVIDER_ID) }

    override suspend fun getLinkedCards(): ApiResponse<LinkedCardsResponse> =
        execute { api.getLinkedCards() }

    override suspend fun checkNewCard(body: CheckNewCard): ApiResponse<CheckNewCardResponse> =
        execute { api.checkNewCard(body) }

    override suspend fun paymentOtpRequest(scenario: PaymentOtpScenario): ApiResponse<OtpResponse> =
        execute { api.paymentOtpRequest(scenario.value) }

    override suspend fun addNewCard(body: AddNewCard): ApiResponse<Unit> =
        execute { api.addNewCard(body) }

    override suspend fun payOrder(body: PayOrder): ApiResponse<PayOrderResponse> =
        execute { api.payOrder(body) }

    override suspend fun pay(body: Pay): ApiResponse<PayResponse> =
        execute { api.pay(body) }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun <T> execute(
        apiCall: suspend () -> T
    ): ApiResponse<T> = withContext(Dispatchers.IO) {
        try {
            ApiResponse.Success(apiCall.invoke())
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    val errorBody = e.response()?.errorBody()?.string() // todo try to use stream()
                    val error = errorGsonBuilder.create().fromJson(errorBody, ErrorBody::class.java)
                    ApiResponse.Error(false, error)
                }
                is UnknownHostException -> {
                    val error = ErrorBody("", context.resources.getString(R.string.internet_error_description))
                    ApiResponse.Error(false, error)
                }
                else -> {
                    val error = ErrorBody("", context.resources.getString(R.string.internet_error))
                    ApiResponse.Error(true, error)
                }
                // todo после смены языка старый контекст, язык не меняется
            }
        }
    }

    private val errorGsonBuilder = GsonBuilder().registerTypeAdapter(
        ErrorBody::class.java,
        JsonDeserializer { json, _, _ ->
            if (json == null || !json.asJsonObject.has("code")) {
                ErrorBody(
                    code = INTERNAL_ERROR,
                    message = context.resources.getString(R.string.internet_error)
                )
            } else {
                ErrorBody(
                    code = json.asJsonObject.get("code").asString,
                    message = json.asJsonObject.get("message").asString
                )
            }
        }
    )
}

private const val DEFAULT_PROVIDER_ID = "ucell"
