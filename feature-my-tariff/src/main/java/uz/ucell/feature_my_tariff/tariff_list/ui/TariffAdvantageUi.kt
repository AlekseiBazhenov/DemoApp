package uz.ucell.feature_my_tariff.tariff_list.ui

import androidx.compose.runtime.Composable

@Composable
fun TariffAdvantages(advantages: List<Advantage>) {
    // TODO: bottomsheet для плюсов тарифа
}

data class Advantage(
    val name: String,
    val icon: String
)
