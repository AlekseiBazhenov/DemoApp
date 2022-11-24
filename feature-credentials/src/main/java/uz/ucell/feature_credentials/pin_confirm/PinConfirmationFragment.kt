package uz.ucell.feature_credentials.pin_confirm

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
import uz.ucell.feature_credentials.R
import uz.ucell.feature_credentials.pin_confirm.contract.PinConfirmationSideEffect
import uz.ucell.navigation.Main
import uz.ucell.ui_kit.common_ui.PinCodeScreen
import uz.ucell.ui_kit.theme.setUcellContent
import uz.ucell.utils.BiometricPromptUtils
import uz.ucell.utils.VibrationUtils

private const val ARG_PIN_CODE = "code"

@AndroidEntryPoint
class PinConfirmationFragment : BaseFragment() {

    private val viewModel: PinConfirmationViewModel by viewModels()

    private val enteredPin by lazy {
        requireArguments().getString(ARG_PIN_CODE, "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        childFragmentManager.setFragmentResultListener(
            AlertDialogFragment.REQUEST_KEY,
            this
        ) { key, bundle ->
            when (bundle.getSerializable(AlertDialogFragment.RESULT_KEY)) {
                AlertDialogFragment.DialogResult.PRIMARY -> showBiometricPrompt()
                AlertDialogFragment.DialogResult.SECONDARY -> viewModel.onDismissBiometryUsage()
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
                    isFirstAuthorization = true,
                    title = stringResource(R.string.auth_create_pin_confirmation_title),
                    input = state.pinInput,
                    isError = state.pinError,
                    showLogoutButton = false,
                    onBackClick = {
                        navigateBack()
                    },
                    onCodeValueChanged = {
                        viewModel.onPinInputChanged(it, enteredPin)
                    },
                    removeSymbolClick = {
                        viewModel.onRemoveSymbol()
                    }
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onScreenEvent()
    }

    private fun handleSideEffect(sideEffect: PinConfirmationSideEffect) {
        when (sideEffect) {
            is PinConfirmationSideEffect.ShowError -> showError(
                message = sideEffect.text,
                code = sideEffect.code
            )
            is PinConfirmationSideEffect.PinError -> VibrationUtils.vibrateOnError(requireContext())
            is PinConfirmationSideEffect.OpenPreviousScreen -> navigateBack()
            is PinConfirmationSideEffect.OpenBiometryScreen -> openBiometryRequestDialog()
            is PinConfirmationSideEffect.OpenMyTariffScreen -> openMainScreen()
            is PinConfirmationSideEffect.ShowBiometryError -> openBiometryError()
        }
    }

    private fun showBiometricPrompt() {
        val biometricPrompt = BiometricPromptUtils.createBiometricPrompt(
            requireActivity(),
            success = {
                viewModel.onBiometricAuthenticationSuccess()
            },
            failure = {
                viewModel.publishFailureBiometryEvent()
            }
        )
        val promptInfo = BiometricPromptUtils.createPromptInfo(requireContext())
        biometricPrompt.authenticate(promptInfo)
    }

    private fun openBiometryRequestDialog() {
        AlertDialogFragment.newInstance(
            title = getString(R.string.auth_biometry_request_title),
            primaryButton = getString(R.string.button_yes),
            secondaryButton = getString(R.string.button_cancel)
        ).show(childFragmentManager, AlertDialogFragment.TAG)
        viewModel.onAuthBiometryDialogEvent()
    }

    private fun openBiometryError() {
        AlertDialogFragment.newInstance(
            title = getString(R.string.auth_biometry_link_error),
            secondaryButton = getString(R.string.button_ok),
        ).show(childFragmentManager, AlertDialogFragment.TAG)
    }

    private fun openMainScreen() {
        navigateTo(Main.ToMainFromPinConfirmation)
    }
}
