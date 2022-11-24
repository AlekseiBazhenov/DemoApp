package uz.ucell.networking.network

import okhttp3.Headers

internal object NetworkConstants {
    val commonHeaders: Headers = Headers.headersOf(
        "Content-Type", "application/json",
        "Accept", "application/json"
    )

    enum class HeadersName(val value: String) {
        USER_AGENT("User-Agent"),
        DEVICE_ID("X-Cabinet-Device-Id"),
        ACCESS_TOKEN("access_token"),
        REFRESH_TOKEN("refresh_token"),
        LANGUAGE("X-Cabinet-Localization"),
        REQUEST_ID("X-Cabinet-Request-Id"),
        DEV_TOKEN("X-Cabinet-Dev-Token")
    }

    const val CONNECTION_TIMEOUT = 5L
    const val READ_TIMEOUT = 30L

    const val CLIENT_ID = "ucell_mobile_app"
    const val CLIENT_SECRET_DEBUG = "secret"

    enum class GrantType(val value: String) {
        PASSWORD("password")
    }

    enum class Scope(val value: String) {
        LK_WRITE("lk:write")
    }
}

enum class PasswordType(val value: String) {
    OTP("otp"),
    PIN("pin"),
    BIOMETRY("biometry")
}

enum class PaymentOtpScenario(val value: String) {
    ADD_NEW_CARD("otp_add_new_card"),
    PAY_BY_CARD("otp_pay_by_card")
}
