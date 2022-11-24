package uz.ucell.feature_select_language

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
import uz.ucell.feature_select_language.contract.SelectLanguageSideEffect
import uz.ucell.navigation.Authorization
import uz.ucell.ui_kit.theme.setUcellContent
import uz.ucell.utils.LocaleUtils

@AndroidEntryPoint
class SelectLanguageFragment : BaseFragment() {

    private val viewModel: SelectLanguageViewModel by viewModels()

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
                SelectLanguageScreen(state) {
                    LocaleUtils.changeAppLanguage(requireContext(), it.code)
                    viewModel.saveSelectedLanguage(it.code, getString(it.title))
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onScreenEvent()
    }

    private fun handleSideEffect(sideEffect: SelectLanguageSideEffect) {
        when (sideEffect) {
            is SelectLanguageSideEffect.OpenAuthScreen -> openAuthorization()
            is SelectLanguageSideEffect.ShowError -> showError(
                sideEffect.text,
                sideEffect.code
            )
        }
    }

    private fun openAuthorization() {
        navigateTo(Authorization.ToEnterPhoneFromSelectLanguage)
    }
}
