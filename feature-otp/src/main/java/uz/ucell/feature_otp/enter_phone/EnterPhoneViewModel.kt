package uz.ucell.feature_otp.enter_phone

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.ucell.appmetrica.api.useCase.EnterPhoneAnalyticsUseCase
import uz.ucell.appmetrica.model.ContentStatus
import uz.ucell.core.constatns.allLanguages
import uz.ucell.core.models.Language
import uz.ucell.core_storage.api.CoreStorage
import uz.ucell.feature_otp.enter_phone.contract.EnterPhoneSideEffect
import uz.ucell.feature_otp.enter_phone.contract.EnterPhoneState
import uz.ucell.networking.NetworkingInterface
import uz.ucell.networking.data.ApiResponse
import uz.ucell.networking.data.ErrorBody
import uz.ucell.networking.exceptions.NEED_CAPTCHA
import uz.ucell.networking.exceptions.NOT_UCELL_CLIENT
import uz.ucell.utils.PHONE_NUMBER_LENGTH
import uz.ucell.utils.PhoneFormatter
import uz.ucell.utils.PhoneUtils
import javax.inject.Inject

@HiltViewModel
class EnterPhoneViewModel @Inject constructor(
    private val coreStorage: CoreStorage,
    private val api: NetworkingInterface,
    private val phoneUtils: PhoneUtils,
    private val analyticsUseCase: EnterPhoneAnalyticsUseCase
) : ContainerHost<EnterPhoneState, EnterPhoneSideEffect>, ViewModel() {

    override val container =
        container<EnterPhoneState, EnterPhoneSideEffect>(EnterPhoneState())

    init {
        onLanguageChangedState()
        setDeviceId()
    }

    fun onNewPhoneValue(enteredPhone: String) = intent {
        reduce {
            state.copy(
                phoneInput = enteredPhone,
                isButtonEnabled = enteredPhone.length == PHONE_NUMBER_LENGTH,
                hasPhoneError = false,
                phoneErrorMessage = ""
            )
        }
    }

    fun onSendSmsClicked() = intent {
        analyticsUseCase.sendClickAuthNumberNext()
        val phone = PhoneFormatter.addCountryCodeToInput(state.phoneInput)
        coreStorage.saveMsisdn(phone)

        loading(true)
        when (val response = api.otpRequest(phone)) {
            is ApiResponse.Success -> {
                postSideEffect(
                    EnterPhoneSideEffect.OpenSmsCode(
                        state.phoneInput,
                        response.value.timeout
                    )
                )
            }
            is ApiResponse.Error -> {
                response.error?.let {
                    processOtpError(it)
                }
            }
        }
        loading(false)
    }

    fun onScreenEvent() = intent {
        analyticsUseCase.sendScreenMetric(ContentStatus.Available)
    }

    fun onOfferLinkClicked(link: String) = intent {
        analyticsUseCase.sendClickAuthOffer()
        postSideEffect(EnterPhoneSideEffect.OpenOffer(link))
    }

    fun getLanguagesList(): List<Language> =
        allLanguages.filter { it.value.enabled }.map { it.value }

    fun onLanguageChanged(language: Language) = intent {
        analyticsUseCase.sendClickAuthChangeLang(language.code)
        coreStorage.saveSelectedLanguage(language.code)
    }

    private suspend fun SimpleSyntax<EnterPhoneState, EnterPhoneSideEffect>.processOtpError(
        error: ErrorBody
    ) {
        analyticsUseCase.sendScreenMetric(ContentStatus.Error(error.code))
        when (error.code) {
            NOT_UCELL_CLIENT ->
                reduce {
                    state.copy(
                        hasPhoneError = true,
                        phoneErrorMessage = error.message,
                        isButtonEnabled = false
                    )
                }
            NEED_CAPTCHA -> {
                postSideEffect(EnterPhoneSideEffect.OpenSmsCodeWithCaptcha(state.phoneInput))
            }
            else -> {
                if (error.authorizationDenied) {
                    postSideEffect(
                        EnterPhoneSideEffect.ShowAuthDeniedError(
                            error.message,
                            error.code
                        )
                    )
                } else {
                    postSideEffect(EnterPhoneSideEffect.ShowError(error.message, error.code))
                }
            }
        }
    }

    private suspend fun SimpleSyntax<EnterPhoneState, EnterPhoneSideEffect>.loading(loading: Boolean) {
        reduce {
            state.copy(isLoading = loading)
        }
    }

    private fun onLanguageChangedState() = intent {
        coreStorage.getSelectedLanguage().collect { selected ->
            val languages = allLanguages.filter { it.value.enabled }
            val previousSelected = state.selectedLanguage
            reduce {
                state.copy(selectedLanguage = languages[selected])
            }
            if (previousSelected != null && selected != previousSelected.code) {
                postSideEffect(EnterPhoneSideEffect.ChangeLanguage(selected))
            }
        }
    }

    private fun setDeviceId() = intent {
        val savedDeviceId = coreStorage.getDeviceId().first()
        if (savedDeviceId.isEmpty()) {
            val deviceId = phoneUtils.getDeviceId()
            coreStorage.setDeviceId(deviceId)
        }
    }

    fun onExpandLanguageModal() = intent {
        analyticsUseCase.sendScreenParamsChangeLang()
    }

    fun onChoiceLanguageClick() = intent {
        analyticsUseCase.sendClickAuthNumberLang()
    }
}
