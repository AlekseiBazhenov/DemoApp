package uz.ucell.debugpanel.presentation

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
import uz.ucell.debugpanel.presentation.contract.EnterDebugPanelSideEffect
import uz.ucell.navigation.NetworkingLogsPanel
import uz.ucell.ui_kit.theme.setUcellContent

@AndroidEntryPoint
class DebugPanelFragment : BaseFragment() {

    private val viewModel: DebugPanelViewModel by viewModels()

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
                DebugPanelScreen(
                    state = state,
                    onHostChanged = { viewModel.onHostChanged(it) },
                    onDeviceIdChanged = { viewModel.onDeviceIdChanged(it) },
                    onUserAgentChanged = { viewModel.onUserAgentChanged(it) },
                    onMsisdnChanged = { viewModel.onMsisdnChanged(it) },
                    onSaveClicked = { viewModel.onClick(DebugPanelViewModel.Event.ClickEvents.SAVE) },
                    onClearClicked = { viewModel.onClick(DebugPanelViewModel.Event.ClickEvents.CLEAR_TOKENS) },
                    onBackClicked = { navigateBack() },
                    onNetworkLoggingChecked = { viewModel.onNetworkLoggingChecked(it) },
                    onDevTokenChanged = { viewModel.onDevTokenChanged(it) },
                    onNetworkLogsButtonClicked = { navigateTo(NetworkingLogsPanel) }
                )
            }
        }
    }

    private fun handleSideEffect(sideEffect: EnterDebugPanelSideEffect) {
        when (sideEffect) {
            is EnterDebugPanelSideEffect.ShowError -> showError(sideEffect.text, sideEffect.code)
        }
    }
}
