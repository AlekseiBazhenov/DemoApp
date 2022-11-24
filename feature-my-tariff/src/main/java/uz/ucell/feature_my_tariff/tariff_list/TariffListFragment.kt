package uz.ucell.feature_my_tariff.tariff_list

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
import uz.ucell.feature_my_tariff.tariff_list.model.TariffListSideEffect
import uz.ucell.feature_my_tariff.tariff_list.ui.TariffListScreen
import uz.ucell.ui_kit.theme.setUcellContent

@AndroidEntryPoint
class TariffListFragment : BaseFragment() {

    private val viewModel: TariffListViewModel by viewModels()

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

                // TODO: обработчик для кликов
                TariffListScreen(
                    uiState = state,
                    onTariffClicked = {},
                    onBackClick = ::navigateBack,
                    onAdvantagesClicked = {},
                    onErrorButtonClicked = {}
                )
            }
        }
    }

    private fun handleSideEffect(sideEffect: TariffListSideEffect) {
        when (sideEffect) {
            is TariffListSideEffect.Error -> showError(
                message = sideEffect.message,
                code = sideEffect.code
            )
        }
    }
}
