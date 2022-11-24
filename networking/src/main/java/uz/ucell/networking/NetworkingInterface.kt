package uz.ucell.networking

import uz.ucell.networking.data.ApiResponse
import uz.ucell.networking.network.PasswordType
import uz.ucell.networking.network.PaymentOtpScenario
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

interface NetworkingInterface {

    suspend fun getCaptcha(): ApiResponse<Captcha>

    suspend fun otpRequest(
        msisdn: String,
        captcha: String? = null,
        localization: String? = null
    ): ApiResponse<OtpResponse>

    suspend fun authorization(
        username: String,
        password: String,
        passwordType: PasswordType,
        localization: String? = null
    ): ApiResponse<TokenExpiresTime>

    suspend fun logout(): ApiResponse<Unit>

    suspend fun createPinCode(pin: PinCode): ApiResponse<Unit>

    suspend fun createBiometryToken(token: BiometryToken): ApiResponse<Unit>

    suspend fun myTariff(): ApiResponse<TariffResponse>

    suspend fun getBalance(): ApiResponse<BalanceResponse>

    suspend fun getPaymentRecommendations(isOtherMsisdn: Boolean): ApiResponse<PaymentRecommendationsResponse>

    suspend fun getPaymentLimits(): ApiResponse<PaymentLimitsResponse>

    suspend fun getAvailableTariffs(): ApiResponse<List<TariffResponse>>

    suspend fun getLinkedCards(): ApiResponse<LinkedCardsResponse>

    suspend fun checkNewCard(body: CheckNewCard): ApiResponse<CheckNewCardResponse>

    suspend fun paymentOtpRequest(scenario: PaymentOtpScenario): ApiResponse<OtpResponse>

    suspend fun getUserProfile(): ApiResponse<ProfileResponse>

    suspend fun addNewCard(body: AddNewCard): ApiResponse<Unit>

    suspend fun payOrder(body: PayOrder): ApiResponse<PayOrderResponse>

    suspend fun pay(body: Pay): ApiResponse<PayResponse>
}
