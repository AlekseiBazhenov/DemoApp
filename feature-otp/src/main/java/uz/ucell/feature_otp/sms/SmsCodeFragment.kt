package uz.ucell.feature_otp.sms

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import uz.ucell.core.BaseFragment
import uz.ucell.core.dialogs.ErrorDialogFragment
import uz.ucell.core.dialogs.FullScreenDialogFragment
import uz.ucell.feature_otp.R
import uz.ucell.feature_otp.sms.contract.EnterSmsCodeSideEffect
import uz.ucell.navigation.Authorization
import uz.ucell.networking.exceptions.B2B_CLIENT
import uz.ucell.smsretriever_consent.SmsConsentManager
import uz.ucell.ui_kit.theme.setUcellContent
import javax.inject.Inject

private const val ARG_PHONE_NUMBER = "phone_number"
private const val ARG_TIMEOUT = "timeout"
private const val ARG_SHOW_CAPTCHA = "show_captcha"

private const val KEY_ERROR_TOO_MUCH_OTP_REQUESTS = "key_error_too_much_otp_requests"

@AndroidEntryPoint
class SmsCodeFragment : BaseFragment() {

    @Inject
    lateinit var smsConsentManager: SmsConsentManager

    private val viewModel: SmsCodeViewModel by viewModels()

    private val phoneNumber by lazy {
        requireArguments().getString(ARG_PHONE_NUMBER, "")
    }

    private val timeout by lazy {
        requireArguments().getLong(ARG_TIMEOUT)
    }

    private val showCaptcha by lazy {
        requireArguments().getBoolean(ARG_SHOW_CAPTCHA)
    }

    private val smsUserConsentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        if (result.resultCode == Activity.RESULT_OK && data != null) {
            val code = smsConsentManager.parseSmsCode(data)
            viewModel.onSmsCodeInputChanged(code)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        childFragmentManager.setFragmentResultListener(
            KEY_ERROR_TOO_MUCH_OTP_REQUESTS,
            this
        ) { key, bundle ->
            when (bundle.getSerializable(ErrorDialogFragment.RESULT_KEY)) {
                ErrorDialogFragment.DialogResult.PRIMARY -> {
                    viewModel.sendSms()
                    viewModel.toMuchRequestOtpPrimaryEvent()
                }
                ErrorDialogFragment.DialogResult.SECONDARY -> {
                    viewModel.toMuchRequestOtpSecondaryEvent()
                }
            }
        }

        childFragmentManager.setFragmentResultListener(
            FullScreenDialogFragment.REQUEST_KEY,
            this
        ) { key, bundle ->
            when (bundle.getSerializable(FullScreenDialogFragment.RESULT_KEY)) {
                FullScreenDialogFragment.DialogResult.BUTTON -> {
                    navigateBack()
                }
            }
        }

        with(smsConsentManager) {
            setListener {
                smsUserConsentLauncher.launch(it)
            }
            start()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onScreenEvent()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        if (showCaptcha) {
            viewModel.showCaptchaBlock()
            viewModel.loadCaptcha()
        }
        viewModel.setInitialState(phoneNumber, timeout)

        return ComposeView(requireContext()).apply {
            setUcellContent {
                LaunchedEffect(viewModel) {
                    viewModel.container.sideEffectFlow.collect(::handleSideEffect)
                }

                val state = viewModel.container.stateFlow.collectAsState().value
                SmsCodeScreen(
                    state = state,
                    onBackClick = {
                        navigateBack()
                    },
                    onCodeValueChanged = {
                        viewModel.onSmsCodeInputChanged(it)
                    },
                    onRepeatSmsClicked = {
                        viewModel.onSmsRepeatClick()
                    },
                    onCaptchaValueChanged = {
                        viewModel.onCaptchaInputChanged(it)
                    },
                    onRefreshCaptchaClick = {
                        viewModel.sendRefreshCaptchaClick()
                        viewModel.loadCaptcha()
                    }
                )
            }
        }
    }

    private fun handleSideEffect(sideEffect: EnterSmsCodeSideEffect) {
        when (sideEffect) {
            is EnterSmsCodeSideEffect.OpenPinScreen -> openCreatePinScreen()
            is EnterSmsCodeSideEffect.ShowOptError -> {
                showError(
                    title = getString(R.string.error_dialog_new_sms_code_title),
                    message = sideEffect.text,
                    primaryButton = getString(R.string.error_dialog_new_sms_code_button),
                    secondaryButton = getString(R.string.button_cancel),
                    requestKey = KEY_ERROR_TOO_MUCH_OTP_REQUESTS
                )
            }
            is EnterSmsCodeSideEffect.ShowError -> showError(
                message = sideEffect.text,
                code = sideEffect.code
            )
            is EnterSmsCodeSideEffect.ShowAuthDeniedError -> {
                showFullscreenError(
                    title = if (sideEffect.code == B2B_CLIENT)
                        null
                    else
                        getString(R.string.auth_denied_error_title),
                    message = sideEffect.text,
                    button = getString(R.string.button_ok)
                )
            }
        }
    }

    private fun openCreatePinScreen() {
        navigateTo(Authorization.ToPinCreateFromSmsCode)
    }
}
