package uz.ucell.networking.exceptions

const val INTERNAL_ERROR = "internal_error"
const val CLIENT_ERROR = "client_error"
const val INVALID_CLIENT = "invalid_client"

const val INVALID_TOKEN = "invalid_token"

// otp, authorization
const val NEED_CAPTCHA = "ac409"
const val INCORRECT_CODE = "ac400"
const val NOT_UCELL_CLIENT = "au404"
const val TOO_EARLY_OTP_CODE_REQUEST = "ao429"
const val INCORRECT_CREDENTIALS = "ao400"
const val TOO_MUCH_OTP_REQUESTS = "ao404"
const val INCORRECT_BIOMETRY_CODE = "ab404"
const val TOO_MUCH_INCORRECT_PIN_AUTH = "ap404"
const val INCORRECT_PIN_CODE = "ap400"

// client status
const val B2B_CLIENT = "a403_b2b"
const val FRAUD = "a403_fraud"
const val FREZZ = "a403_frezz"
const val FINANCIAL_BLOCKING = "a403_s4"
const val SCRATCH = "a403_scratch"
const val DOCS = "a403_docs"
const val MANUAL = "a403_manual"
const val MNP = "a403_mnp"
const val CONTRACT = "a403_contract"
const val OUT = "a403_out"
const val PREDISCONNECT = "a403_predisconnect"
