package uz.ucell.debugpanel.logging_panel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import uz.ucell.core.BaseFragment
import uz.ucell.ui_kit.theme.setUcellContent

@AndroidEntryPoint
class NetworkLogsFragment : BaseFragment() {

    private val viewModel: NetworkLogsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setUcellContent {
                val state = viewModel.container.stateFlow.collectAsState().value
                ErrorPanelScreen(
                    state,
                    onClearClicked = { viewModel.onClearLogsClicked() },
                    onBackClicked = { navigateBack() }
                )
            }
        }
    }
}
