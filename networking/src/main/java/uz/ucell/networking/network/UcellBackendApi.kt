package uz.ucell.networking.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
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
import uz.ucell.networking.network.response.TariffListResponse
import uz.ucell.networking.network.response.TariffResponse

internal interface UcellBackendApi {

    private companion object {
        const val PIN_REQUEST = "/api/auth/credentials/pin"
        const val BIOMETRY_REQUEST = "/api/auth/credentials/biometry"
        const val MY_TARIFF_REQUEST = "/api/telecom/ratePlan/current"
        const val AVAILABLE_TARIFF_REQUEST = "/api/telecom/ratePlan/available/list"
        const val BALANCE_REQUEST = "/api/telecom/ratePlan/balance"
        const val USER_PROFILE_REQUEST = "api/telecom/user/profile"
        const val PAYMENT_RECOMMENDATIONS_REQUEST = "/api/payments/recommendations/balance"
        const val PAYMENT_LIMITS_REQUEST = "/api/payments/limits"
        const val LINKED_CARDS_REQUEST = "/api/payments/cards/linked"
        const val CHECK_CARD_REQUEST = "/api/payments/cards/new/check"
        const val OTP_REQUEST = "/api/payments/otpRequest"
        const val ADD_NEW_CARD_REQUEST = "/api/payments/cards/new"
        const val PAY_ORDER_REQUEST = "/api/payments/cards/pay/order"
        const val PAY_REQUEST = "/api/payments/cards/pay"

        const val QUERY_IS_OTHER_MSISDN = "isOtherMsisdn"
        const val QUERY_ID = "id"
        const val SCENARIO = "scenario"
    }

    @POST(PIN_REQUEST)
    suspend fun createPin(
        @Body requestBody: PinCode
    )

    @POST(BIOMETRY_REQUEST)
    suspend fun createBiometryToken(
        @Body requestBody: BiometryToken
    )

    @GET(MY_TARIFF_REQUEST)
    suspend fun myTariff(): TariffResponse

    @GET(BALANCE_REQUEST)
    suspend fun getBalance(): BalanceResponse

    @GET(USER_PROFILE_REQUEST)
    suspend fun getProfile(): ProfileResponse

    @GET(AVAILABLE_TARIFF_REQUEST)
    suspend fun getAvailableTariffsList(): TariffListResponse

    @GET(PAYMENT_RECOMMENDATIONS_REQUEST)
    suspend fun getPaymentRecommendations(
        @Query(QUERY_IS_OTHER_MSISDN) isOtherMsisdn: Boolean
    ): PaymentRecommendationsResponse

    @GET(PAYMENT_LIMITS_REQUEST)
    suspend fun getPaymentLimits(
        @Query(QUERY_ID) id: String
    ): PaymentLimitsResponse

    @GET(LINKED_CARDS_REQUEST)
    suspend fun getLinkedCards(): LinkedCardsResponse

    @POST(CHECK_CARD_REQUEST)
    suspend fun checkNewCard(
        @Body requestBody: CheckNewCard
    ): CheckNewCardResponse

    @POST(OTP_REQUEST)
    suspend fun paymentOtpRequest(
        @Query(SCENARIO) scenario: String
    ): OtpResponse

    @POST(ADD_NEW_CARD_REQUEST)
    suspend fun addNewCard(
        @Body requestBody: AddNewCard
    )

    @POST(PAY_ORDER_REQUEST)
    suspend fun payOrder(
        @Body requestBody: PayOrder
    ): PayOrderResponse

    @POST(PAY_REQUEST)
    suspend fun pay(
        @Body requestBody: Pay
    ): PayResponse
}
