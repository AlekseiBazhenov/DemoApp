package uz.ucell.feature_authorization.pin_auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import uz.ucell.core.BaseFragment
import uz.ucell.core.dialogs.AlertDialogFragment
import uz.ucell.core.dialogs.FullScreenDialogFragment
import uz.ucell.feature_authorization.R
import uz.ucell.feature_authorization.pin_auth.contract.PinAuthSideEffect
import uz.ucell.navigation.Authorization
import uz.ucell.navigation.Main
import uz.ucell.networking.exceptions.B2B_CLIENT
import uz.ucell.ui_kit.common_ui.PinCodeScreen
import uz.ucell.ui_kit.theme.setUcellContent
import uz.ucell.utils.BiometricPromptUtils
import uz.ucell.utils.VibrationUtils

@AndroidEntryPoint
class AuthFragment : BaseFragment() {

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parentFragmentManager.setFragmentResultListener(
            FullScreenDialogFragment.REQUEST_KEY,
            this
        ) { key, bundle ->
            when (bundle.getSerializable(FullScreenDialogFragment.RESULT_KEY)) {
                FullScreenDialogFragment.DialogResult.BUTTON -> openEnterPhoneScreen()
            }
        }

        childFragmentManager.setFragmentResultListener(
            AlertDialogFragment.REQUEST_KEY,
            this
        ) { key, bundle ->
            when (bundle.getSerializable(AlertDialogFragment.RESULT_KEY)) {
                AlertDialogFragment.DialogResult.PRIMARY -> viewModel.onLogoutClick()
            }
        }
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
                PinCodeScreen(
                    title = stringResource(R.string.auth_pin_title),
                    input = state.pinInput,
                    isError = state.pinError,
                    errorMessage = state.pinErrorMessage,
                    showBiometry = state.showBiometry,
                    showLogoutButton = true,
                    onCodeValueChanged = {
                        viewModel.onPinInputChanged(it)
                    },
                    removeSymbolClick = {
                        viewModel.onRemoveSymbol()
                    },
                    logoutClick = {
                        showLogoutDialog()
                    },
                    biometryClick = {
                        showBiometricPrompt()
                    }
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onScreenEvent()
    }

    private fun handleSideEffect(sideEffect: PinAuthSideEffect) {
        when (sideEffect) {
            is PinAuthSideEffect.ShowError -> showError(sideEffect.text, sideEffect.code)
            is PinAuthSideEffect.ShowNeedAuthAgain -> showFullscreenError(
                title = getString(R.string.auth_need_opt_again),
                message = sideEffect.text,
                button = getString(R.string.button_reauthorize),
            )
            is PinAuthSideEffect.PinError -> VibrationUtils.vibrateOnError(requireContext())
            is PinAuthSideEffect.OpenBiometryScreen -> showBiometricPrompt()
            is PinAuthSideEffect.OpenMyTariffScreen -> openMainScreen()
            is PinAuthSideEffect.OpenEnterPhoneScreen -> openEnterPhoneScreen()
            is PinAuthSideEffect.ShowAuthDeniedError -> {
                showFullscreenError(
                    title = if (sideEffect.code == B2B_CLIENT) {
                        null
                    } else {
                        getString(R.string.auth_denied_error_title)
                    },
                    message = sideEffect.text,
                    button = getString(R.string.button_ok),
                )
            }
        }
    }

    private fun openEnterPhoneScreen() {
        navigateTo(Authorization.ToEnterPhoneFromPinCode)
    }

    private fun openMainScreen() {
        navigateTo(Main.ToMainFromPinCode)
    }

    private fun showBiometricPrompt() {
        val biometricPrompt = BiometricPromptUtils.createBiometricPrompt(
            requireActivity(),
            success = {
                viewModel.onBiometricAuthenticationSuccess()
            },
            failure = {
                viewModel.onBiometricAuthenticationFailure()
            }
        )
        val promptInfo = BiometricPromptUtils.createPromptInfo(requireContext())
        biometricPrompt.authenticate(promptInfo)
    }

    private fun showLogoutDialog() {
        AlertDialogFragment.newInstance(
            title = getString(R.string.logout_dialog_title),
            message = getString(R.string.logout_dialog_description),
            primaryButton = getString(R.string.button_logout),
            secondaryButton = getString(R.string.button_cancel_action)
        ).show(childFragmentManager, AlertDialogFragment.TAG)
    }
}
