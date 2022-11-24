package uz.ucell.feature_my_tariff.tariff_list.mock

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import uz.ucell.feature_my_tariff.tariff_list.model.Badge
import uz.ucell.feature_my_tariff.tariff_list.model.LimitType
import uz.ucell.feature_my_tariff.tariff_list.model.TariffLimit
import uz.ucell.feature_my_tariff.tariff_list.model.TariffListState
import uz.ucell.feature_my_tariff.tariff_list.model.TariffRatePlanPrice
import uz.ucell.feature_my_tariff.tariff_list.ui.Advantage
import uz.ucell.feature_my_tariff.tariff_list.ui.TariffCell
import uz.ucell.ui_kit.R

class MockBadgeProvider : PreviewParameterProvider<Badge?> {
    override val values: Sequence<Badge?> = sequenceOf(
        Badge("Vip", R.color.rich_purple),
        null
    )
}

class MockTariffListStateProvider : PreviewParameterProvider<TariffListState> {
    override val values: Sequence<TariffListState> = sequenceOf(
        TariffListState(isError = true),
        TariffListState(isLoading = true)
    )
}

class MockRatePlanProvider : PreviewParameterProvider<TariffRatePlanPrice?> {
    override val values: Sequence<TariffRatePlanPrice?> = sequenceOf(
        TariffRatePlanPrice(
            18000,
            "сум",
            "мес"
        ),
        null
    )
}

class MockTariffCellProvider : PreviewParameterProvider<TariffCell> {
    override val values: Sequence<TariffCell> = sequenceOf(
        TariffCell(
            title = "Sof 30",
            badge = Badge("Vip", R.color.rich_purple),
            ratePlanPrice = TariffRatePlanPrice(
                18000,
                "сум",
                "мес"
            ),
            advantages = listOf(
                Advantage("", "")
            )
        ),
        TariffCell(
            title = "Sof 30",
            badge = Badge("Vip", R.color.rich_purple),
            advantages = listOf(
                Advantage("", "")
            )
        ),
        TariffCell(
            title = "Sof 30",
            badge = Badge("Vip", R.color.rich_purple),
            ratePlanPrice = TariffRatePlanPrice(
                18000,
                "сум",
                "мес"
            ),
            limits = listOf(
                TariffLimit.Limited(1500, "SMS", LimitType.SMS),
                TariffLimit.Unlimited("Безлимит", LimitType.SMS)
            ),
            advantages = listOf(
                Advantage("", "")
            )
        ),
        TariffCell(
            title = "Sof 30",
            badge = Badge("Vip", R.color.rich_purple),
            ratePlanPrice = TariffRatePlanPrice(
                18000,
                "сум",
                "мес"
            ),
            limits = listOf(
                TariffLimit.Limited(1500, "SMS", LimitType.SMS),
                TariffLimit.Unlimited("Безлимит", LimitType.SMS)
            )
        )
    )
}
