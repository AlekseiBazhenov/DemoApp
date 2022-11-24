package uz.ucell.feature_initialization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.ucell.core.BaseFragment
import uz.ucell.feature_initialization.contract.InitializationSideEffect
import uz.ucell.navigation.Authorization
import uz.ucell.navigation.SelectLanguage
import uz.ucell.ui_kit.theme.setUcellContent
import uz.ucell.utils.LocaleUtils

@AndroidEntryPoint
class InitializationFragment : BaseFragment() {

    private val viewModel: InitializationViewModel by viewModels()

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
                SplashScreenUI {
                    lifecycleScope.launch {
                        delay(250)
                        viewModel.animationEnded()
                    }
                }
            }
        }
    }

    private fun handleSideEffect(sideEffect: InitializationSideEffect) {
        when (sideEffect) {
            is InitializationSideEffect.OpenSelectLanguageScreen -> navigateTo(SelectLanguage)
            is InitializationSideEffect.OpenPinAuthScreen -> navigateTo(Authorization.ToPinCodeFromInitialization)
            is InitializationSideEffect.OpenEnterPhoneScreen -> {
                LocaleUtils.changeAppLanguage(requireContext(), sideEffect.selectedLanguage)
                navigateTo(Authorization.ToEnterPhoneFromInitialization)
            }
        }
    }
}
