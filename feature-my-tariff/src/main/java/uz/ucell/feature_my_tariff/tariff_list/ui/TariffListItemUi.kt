package uz.ucell.feature_my_tariff.tariff_list.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import uz.ucell.core.constatns.RU
import uz.ucell.feature_my_tariff.tariff_list.mock.MockBadgeProvider
import uz.ucell.feature_my_tariff.tariff_list.mock.MockRatePlanProvider
import uz.ucell.feature_my_tariff.tariff_list.mock.MockTariffCellProvider
import uz.ucell.feature_my_tariff.tariff_list.model.Badge
import uz.ucell.feature_my_tariff.tariff_list.model.TariffRatePlanPrice
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.components.badges.BadgePromo
import uz.ucell.ui_kit.components.buttons.ButtonPrimary
import uz.ucell.ui_kit.components.buttons.ButtonSize
import uz.ucell.ui_kit.extentions.ucellShadow
import uz.ucell.ui_kit.theme.UcellTheme

@Composable
fun TitleWithBadge(
    title: String,
    badge: Badge?,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Texts.H1(
            color = colorResource(R.color.rich_purple),
            title = title
        )
        Spacer(modifier = Modifier.weight(1f))
        if (badge != null) {
            BadgePromo(
                text = badge.title,
                badgeBackground = badge.color,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun RatePlanBox(
    ratePlanPrice: TariffRatePlanPrice?,
    onTariffClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = colorResource(R.color.sky0),
                RoundedCornerShape(16.dp)
            ),
    ) {
        Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
            if (ratePlanPrice != null) {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 12.dp),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Texts.H2(title = "${ratePlanPrice.value} ${ratePlanPrice.unit}")
                    Texts.ParagraphText(
                        modifier = Modifier.padding(bottom = 1.dp),
                        title = "/${ratePlanPrice.unitPeriod}"
                    )
                }
            }
            ButtonPrimary(
                buttonSize = ButtonSize.SMALL,
                text = stringResource(id = R.string.label_show_more_info),
                onClick = onTariffClick
            )
        }
    }
}

@Composable
fun TariffItem(
    cell: TariffCell,
    onTariffClick: () -> Unit,
    onAdvantagesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = 12.dp,
                horizontal = 16.dp
            )
            .ucellShadow(),
        shape = RoundedCornerShape(12.dp)
    ) {
        val columnModifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp)

        Column(modifier = columnModifier) {
            TitleWithBadge(
                title = cell.title,
                badge = cell.badge,
                Modifier.padding(bottom = 24.dp)
            )

            if (!cell.limits.isNullOrEmpty()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    cell.limits.forEach { limit ->
                        LimitCell(cell = limit)
                    }
                }
            }

            if (!cell.advantages.isNullOrEmpty()) {
                Texts.DashedUnderline(
                    modifier = Modifier.padding(bottom = 24.dp),
                    text = stringResource(id = R.string.tariff_advantages),
                    onClick = onAdvantagesClick
                )
            }

            RatePlanBox(
                ratePlanPrice = cell.ratePlanPrice,
                onTariffClick = onTariffClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(
    name = "Badge preview",
    showBackground = true,
    locale = RU
)
@Composable
fun TitleBadgePreview(
    @PreviewParameter(MockBadgeProvider::class) mockBadge: Badge?
) {
    UcellTheme {
        TitleWithBadge(
            title = "Sof 30",
            badge = mockBadge
        )
    }
}

@Preview(
    name = "Rate plan preview",
    showBackground = true,
    locale = RU
)
@Composable
fun RatePlanBoxPreview(
    @PreviewParameter(MockRatePlanProvider::class) mockRatePlan: TariffRatePlanPrice?
) {
    UcellTheme {
        RatePlanBox(
            ratePlanPrice = mockRatePlan,
            onTariffClick = { }
        )
    }
}

@Preview(
    name = "Item full preview",
    showBackground = true,
    locale = RU
)
@Composable
fun ItemFullPreview(
    @PreviewParameter(MockTariffCellProvider::class) mockCell: TariffCell
) {
    UcellTheme {
        TariffItem(
            cell = mockCell,
            onTariffClick = { },
            onAdvantagesClick = { }
        )
    }
}
