package uz.ucell.feature_otp.enter_phone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import uz.ucell.core.BaseFragment
import uz.ucell.core.dialogs.FullScreenDialogFragment
import uz.ucell.core_storage.api.CoreStorage
import uz.ucell.feature_otp.R
import uz.ucell.feature_otp.enter_phone.contract.EnterPhoneSideEffect
import uz.ucell.navigation.Authorization
import uz.ucell.networking.exceptions.B2B_CLIENT
import uz.ucell.ui_kit.theme.setUcellContent
import uz.ucell.utils.LocaleUtils
import javax.inject.Inject

@AndroidEntryPoint
class EnterPhoneFragment : BaseFragment() {

    private val viewModel: EnterPhoneViewModel by viewModels()

    @Inject
    lateinit var storage: CoreStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        return ComposeView(requireContext()).apply {
            setUcellContent {
                LaunchedEffect(viewModel) {
                    viewModel.container.sideEffectFlow.collect(::handleSideEffect)
                }

                val state = viewModel.container.stateFlow.collectAsState().value
                EnterPhoneScreen(
                    state = state,
                    languages = viewModel.getLanguagesList(),
                    onPhoneValueChanged = {
                        viewModel.onNewPhoneValue(it)
                    },
                    onButtonClicked = {
                        viewModel.onSendSmsClicked()
                    },
                    onOfferLinkClicked = {
                        viewModel.onOfferLinkClicked(it)
                    },
                    onLanguageClicked = {
                        viewModel.onLanguageChanged(it)
                    },
                    onAvailableLanguageClick = {
                        viewModel.onChoiceLanguageClick()
                    },
                    onModalBottomSheetExpanded = {
                        viewModel.onExpandLanguageModal()
                    }
                )
            }
        }
    }

    private fun handleSideEffect(sideEffect: EnterPhoneSideEffect) {
        when (sideEffect) {
            is EnterPhoneSideEffect.OpenSmsCode -> openEnterSmsCode(
                sideEffect.phoneNumber,
                sideEffect.timeout
            )
            is EnterPhoneSideEffect.OpenSmsCodeWithCaptcha -> openEnterSmsCodeWithCaptcha(
                sideEffect.phoneNumber,
            )
            is EnterPhoneSideEffect.OpenOffer -> openOffer()
            is EnterPhoneSideEffect.ShowError -> showError(
                message = sideEffect.text,
                code = sideEffect.code
            )
            is EnterPhoneSideEffect.ShowAuthDeniedError -> {
                showFullscreenError(
                    title = if (sideEffect.code == B2B_CLIENT)
                        null
                    else
                        getString(R.string.auth_denied_error_title),
                    message = sideEffect.text,
                    button = getString(R.string.button_ok)
                )
            }
            is EnterPhoneSideEffect.ChangeLanguage -> LocaleUtils.changeAppLanguage(requireContext(), sideEffect.newLanguage)
        }
    }

    private fun openEnterSmsCode(phoneNumber: String, timeout: Long) {
        navigateTo(Authorization.ToSmsCodeFromEnterPhone(phone = phoneNumber, timeout = timeout))
    }

    private fun openEnterSmsCodeWithCaptcha(phoneNumber: String) {
        navigateTo(Authorization.ToSmsCodeFromEnterPhone(phone = phoneNumber, showCaptcha = true))
    }

    private fun openOffer() {
        // TODO
    }
}
