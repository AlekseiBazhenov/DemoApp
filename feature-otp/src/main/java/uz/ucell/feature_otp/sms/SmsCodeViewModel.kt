package uz.ucell.feature_otp.sms

import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.ucell.appmetrica.api.useCase.EnterSmsAnalyticsUseCase
import uz.ucell.appmetrica.api.useCase.SendProfileMetricUseCase
import uz.ucell.appmetrica.model.ContentStatus
import uz.ucell.core.constatns.CAPTCHA_LENGTH
import uz.ucell.core.constatns.SMS_CODE_LENGTH
import uz.ucell.feature_otp.sms.contract.EnterSmsCodeSideEffect
import uz.ucell.feature_otp.sms.contract.EnterSmsCodeState
import uz.ucell.networking.NetworkingInterface
import uz.ucell.networking.data.ApiResponse
import uz.ucell.networking.data.ErrorBody
import uz.ucell.networking.exceptions.INCORRECT_CODE
import uz.ucell.networking.exceptions.INCORRECT_CREDENTIALS
import uz.ucell.networking.exceptions.NEED_CAPTCHA
import uz.ucell.networking.exceptions.TOO_MUCH_OTP_REQUESTS
import uz.ucell.networking.network.PasswordType
import uz.ucell.utils.PhoneFormatter
import uz.ucell.utils.timerFlow
import javax.inject.Inject

@HiltViewModel
class SmsCodeViewModel @Inject constructor(
    private val api: NetworkingInterface,
    private val analyticsUseCase: EnterSmsAnalyticsUseCase,
    private val sendProfileUseCase: SendProfileMetricUseCase
) : ContainerHost<EnterSmsCodeState, EnterSmsCodeSideEffect>, ViewModel() {

    override val container =
        container<EnterSmsCodeState, EnterSmsCodeSideEffect>(EnterSmsCodeState())

    fun setInitialState(phoneNumber: String, timeout: Long) = intent {
        if (timeout > 0) {
            startTimer(timeout)
        }

        reduce {
            state.copy(phoneNumber = PhoneFormatter.formatPhone(phoneNumber))
        }
    }

    fun onScreenEvent() = intent {
        analyticsUseCase.sendScreenMetric(ContentStatus.Available)
    }

    fun onSmsCodeInputChanged(enteredCode: String) = intent {
        reduce {
            state.copy(
                codeInput = enteredCode,
                hasCodeInputError = false
            )
        }
        if (enteredCode.length == SMS_CODE_LENGTH) {
            authorize(PhoneFormatter.formatPhoneToNumeric(state.phoneNumber), state.codeInput)
        }
    }

    private fun authorize(phoneNumber: String, enteredCode: String) = intent {
        loading(true)
        when (
            val response = api.authorization(
                username = phoneNumber,
                password = enteredCode,
                passwordType = PasswordType.OTP
            )
        ) {
            is ApiResponse.Success -> {
                // TODO сделать автообновление access токена до истечения срока действия
                analyticsUseCase.sendClickAuthInputSms(isCodeCorrect = true)
                sendProfileUseCase.sendProfileMetric()
                postSideEffect(EnterSmsCodeSideEffect.OpenPinScreen)
            }
            is ApiResponse.Error -> {
                analyticsUseCase.sendClickAuthInputSms(isCodeCorrect = false)
                response.error?.let { processError(it) }
            }
        }
        loading(false)
    }

    fun onCaptchaInputChanged(enteredCaptcha: String) = intent {
        reduce {
            state.copy(captchaInput = enteredCaptcha)
        }

        if (enteredCaptcha.length == CAPTCHA_LENGTH) {
            sendSms(captcha = enteredCaptcha)
        }
    }

    fun onSmsRepeatClick() = intent {
        analyticsUseCase.sendClickAuthOtpRepeat()
        sendSms()
    }

    fun sendSms(captcha: String? = null) = intent {
        if (!state.isTimerFinished) {
            return@intent
        }

        loading(true)
        when (
            val response = api.otpRequest(
                msisdn = PhoneFormatter.formatPhoneToNumeric(state.phoneNumber),
                captcha = captcha
            )
        ) {
            is ApiResponse.Success -> {
                reduce {
                    state.copy(
                        needShowCaptcha = false,
                        hasCodeInputError = false,
                        codeInput = "",
                        captchaInput = ""
                    )
                }
                startTimer(response.value.timeout)
            }
            is ApiResponse.Error -> {
                response.error?.let { processError(it) }
            }
        }
        loading(false)
    }

    fun showCaptchaBlock() = intent {
        showCaptcha(true)
    }

    fun loadCaptcha() = intent {
        captchaLoading(true)
        when (val response = api.getCaptcha()) {
            is ApiResponse.Success -> {
                reduce {
                    state.copy(
                        captchaLoading = false,
                        captcha = BitmapFactory.decodeByteArray(
                            response.value.image,
                            0,
                            response.value.image.size
                        )
                    )
                }
            }
            is ApiResponse.Error -> {
                response.error?.let { processError(it) }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun startTimer(initialTimeoutSeconds: Long) = intent {
        reduce {
            state.copy(isTimerFinished = false)
        }

        timerFlow(initCountDownSeconds = initialTimeoutSeconds)
            .onEach {
                reduce {
                    state.copy(timeoutSeconds = it)
                }
            }
            .onCompletion {
                reduce {
                    state.copy(isTimerFinished = true)
                }
            }
            .launchIn(viewModelScope)
    }

    private suspend fun SimpleSyntax<EnterSmsCodeState, EnterSmsCodeSideEffect>.loading(
        loading: Boolean
    ) {
        reduce {
            state.copy(isLoading = loading)
        }
    }

    private suspend fun SimpleSyntax<EnterSmsCodeState, EnterSmsCodeSideEffect>.captchaLoading(
        show: Boolean
    ) {
        reduce {
            state.copy(captchaLoading = show)
        }
    }

    private suspend fun SimpleSyntax<EnterSmsCodeState, EnterSmsCodeSideEffect>.showCaptcha(
        show: Boolean
    ) {
        reduce {
            state.copy(needShowCaptcha = show)
        }
    }

    private suspend fun SimpleSyntax<EnterSmsCodeState, EnterSmsCodeSideEffect>.processError(
        error: ErrorBody
    ) {
        when (error.code) {
            INCORRECT_CREDENTIALS -> {
                reduce {
                    state.copy(
                        codeInput = "",
                        hasCodeInputError = true,
                        codeInputError = error.message
                    )
                }
            }
            TOO_MUCH_OTP_REQUESTS -> {
                analyticsUseCase.sendScreenMetric(ContentStatus.Error(error.code))
                postSideEffect(EnterSmsCodeSideEffect.ShowOptError(error.message))
            }
            INCORRECT_CODE -> {
                analyticsUseCase.sendScreenMetric(ContentStatus.Error(error.code))
                reduce {
                    state.copy(
                        captchaInput = "",
                        hasCaptchaInputError = true,
                        captchaInputError = error.message
                    )
                }
                loadCaptcha()
            }
            NEED_CAPTCHA -> {
                reduce {
                    state.copy(
                        needShowCaptcha = true,
                    )
                }
                loadCaptcha()
            }
            else -> {
                if (error.authorizationDenied) {
                    postSideEffect(
                        EnterSmsCodeSideEffect.ShowAuthDeniedError(
                            error.message,
                            error.code
                        )
                    )
                } else {
                    postSideEffect(EnterSmsCodeSideEffect.ShowError(error.message, error.code))
                }
            }
        }
    }

    fun toMuchRequestOtpSecondaryEvent() = intent {
        analyticsUseCase.sendClickAuthOtpLimit(true)
    }

    fun toMuchRequestOtpPrimaryEvent() = intent {
        analyticsUseCase.sendClickAuthOtpLimit(false)
    }

    fun sendRefreshCaptchaClick() = intent {
        analyticsUseCase.sendClickAuthUpdateCaptcha()
    }

    fun sendOtpExpiredPrimaryEvent() = intent {
        analyticsUseCase.sendClickAuthOtpExpired(true)
    }

    fun sendOtpExpiredSecondaryEvent() = intent {
        analyticsUseCase.sendClickAuthOtpExpired(false)
    }
}
