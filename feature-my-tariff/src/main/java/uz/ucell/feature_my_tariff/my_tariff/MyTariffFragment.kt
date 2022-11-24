package uz.ucell.feature_my_tariff.my_tariff

import android.content.Intent
import android.net.Uri
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
import uz.ucell.feature_my_tariff.my_tariff.contract.MyTariffSideEffect
import uz.ucell.navigation.Tariffs
import uz.ucell.ui_kit.theme.setUcellContent

@AndroidEntryPoint
class MyTariffFragment : BaseFragment() {

    private val viewModel: MyTariffViewModel by viewModels()

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

                MyTariffScreen(
                    state = state,
                    onMoreAboutTariffClicked = { openBrowser(state.tariffInfoLink) },
                    onBackClick = ::navigateBack,
                    onChangeTariffClick = { navigateTo(Tariffs.ToTariffListFromMyTariff) },
                    faqLinkClicked = ::openBrowser,
                    onRetryRequestClick = { viewModel.onRetryRequest() }
                )
            }
        }
    }

    private fun openBrowser(link: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(link)
        startActivity(i)
    }

    private fun handleSideEffect(sideEffect: MyTariffSideEffect) {
        when (sideEffect) {
            is MyTariffSideEffect.ShowError -> showError(
                message = sideEffect.text,
                code = sideEffect.code
            )
        }
    }
}
