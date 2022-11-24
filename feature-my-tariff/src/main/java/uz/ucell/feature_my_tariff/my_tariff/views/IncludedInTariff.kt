package uz.ucell.feature_my_tariff.my_tariff.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.components.divider.SmallDivider
import uz.ucell.ui_kit.components.expandable.ExpandableCaption
import uz.ucell.ui_kit.components.progress.TariffProgressIndicator

@Composable
fun IncludedInTariff(isFinancialBlock: Boolean, limits: List<TariffLimit>) {
    Column(modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)) {
        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
            Image(
                modifier = Modifier.padding(end = 16.dp),
                painter = painterResource(R.drawable.ic_phone), contentDescription = null
            )
            Texts.H3(
                title = stringResource(
                    if (isFinancialBlock)
                        R.string.my_tariff_include_blocked
                    else
                        R.string.my_tariff_include
                )
            )
        }
        limits.forEach {
            TariffProgressItem(isFinancialBlock, it)
        }
    }
}

@Composable
private fun TariffProgressItem(isFinancialBlock: Boolean, limit: TariffLimit) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        SmallDivider(modifier = Modifier.padding(bottom = 16.dp))
        Row {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            ) {
                Texts.ParagraphText(
                    modifier = Modifier.padding(bottom = 8.dp),
                    title = limit.name,
                    color = colorResource(if (isFinancialBlock) R.color.sky3 else R.color.base_black)
                )
                TariffProgressIndicator(
                    totalValue = limit.totalValue,
                    availableValue = limit.availableValue,
                    isEnabled = !isFinancialBlock
                )
            }
            Column(modifier = Modifier.width(136.dp)) {
                Texts.Caption(
                    modifier = Modifier.padding(bottom = 6.dp),
                    title = stringResource(
                        if (isFinancialBlock)
                            R.string.my_tariff_remaining_blocked
                        else
                            R.string.my_tariff_remaining
                    )
                )
                Texts.H4(
                    title = limit.remain,
                    color = colorResource(if (isFinancialBlock) R.color.sky3 else R.color.base_black)
                )
            }
        }
        if (!limit.description.isNullOrEmpty()) {
            ExpandableCaption(
                modifier = Modifier.padding(top = 8.dp),
                text = limit.description,
                initialStateExpanded = limit.isDescriptionExpanded
            )
        }
    }
}

data class TariffLimit(
    val name: String,
    val description: String?,
    val isDescriptionExpanded: Boolean,
    val remain: String,
    val totalValue: Float,
    val availableValue: Float,
)
