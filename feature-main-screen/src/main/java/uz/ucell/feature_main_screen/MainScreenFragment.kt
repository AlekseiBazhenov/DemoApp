package uz.ucell.feature_main_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import uz.ucell.core.BaseFragment
import uz.ucell.navigation.Refill
import uz.ucell.navigation.Tariffs
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.components.buttons.ButtonPrimary
import uz.ucell.ui_kit.theme.setUcellContent

class MainScreenFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setUcellContent {
                Column(
                    modifier = Modifier
                        .systemBarsPadding()
                        .padding(16.dp)
                ) {
                    Texts.H2(modifier = Modifier.padding(bottom = 16.dp), title = "Главный экран")
                    ButtonPrimary(modifier = Modifier.padding(bottom = 16.dp), text = "Мой тариф") {
                        navigateTo(Tariffs.ToMyTariffFromMain)
                    }
                    ButtonPrimary(text = "Пополнить баланс") {
                        navigateTo(Refill.ToRefillBalanceFromMain)
                    }
                }
            }
        }
    }
}
