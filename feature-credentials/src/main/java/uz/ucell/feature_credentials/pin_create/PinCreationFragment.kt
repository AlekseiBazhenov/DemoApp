package uz.ucell.feature_credentials.pin_create

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
import uz.ucell.core.dialogs.ErrorDialogFragment
import uz.ucell.feature_credentials.R
import uz.ucell.feature_credentials.pin_create.contract.PinCreationSideEffect
import uz.ucell.navigation.Authorization
import uz.ucell.ui_kit.common_ui.PinCodeScreen
import uz.ucell.ui_kit.theme.setUcellContent
import uz.ucell.utils.VibrationUtils

@AndroidEntryPoint
class PinCreationFragment : BaseFragment() {

    private val viewModel: PinCreationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        childFragmentManager.setFragmentResultListener(
            ErrorDialogFragment.DEFAULT_REQUEST_KEY,
            this
        ) { key, bundle ->
            when (bundle.getSerializable(ErrorDialogFragment.RESULT_KEY)) {
                ErrorDialogFragment.DialogResult.PRIMARY -> {
                    viewModel.onEnterPinAgain()
                }
                ErrorDialogFragment.DialogResult.SECONDARY -> {
                    viewModel.onContinueWithSoftPin()
                }
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
                viewModel.checkIsPinValid(state.pinInput)
                PinCodeScreen(
                    isFirstAuthorization = true,
                    title = stringResource(R.string.auth_create_pin_title),
                    input = state.pinInput,
                    isError = state.pinError,
                    showLogoutButton = false,
                    onBackClick = {
                        navigateBack()
                    },
                    onCodeValueChanged = {
                        viewModel.onPinInputChanged(it)
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

    private fun handleSideEffect(sideEffect: PinCreationSideEffect) {
        when (sideEffect) {
            is PinCreationSideEffect.ShowError -> showError(sideEffect.text, sideEffect.code)
            is PinCreationSideEffect.PinError -> VibrationUtils.vibrateOnError(requireContext())
            is PinCreationSideEffect.OpenPinConfirmationScreen -> openPinConfirmation(sideEffect.code)
            is PinCreationSideEffect.SoftPinWarning -> showError(
                title = getString(R.string.auth_pin_warning_title),
                message = getString(R.string.auth_pin_warning_message),
                primaryButton = getString(R.string.auth_pin_warning_button_change),
                secondaryButton = getString(R.string.auth_pin_warning_button_continue),
            )
        }
    }

    private fun openPinConfirmation(code: String) {
        navigateTo(Authorization.ToPinConfirmFromPinCreate(code))
    }
}
