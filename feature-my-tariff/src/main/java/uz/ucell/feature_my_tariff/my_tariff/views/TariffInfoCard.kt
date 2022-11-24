package uz.ucell.feature_my_tariff.my_tariff.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.common_ui.ErrorWithRetry
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.components.divider.SmallDivider
import uz.ucell.ui_kit.extentions.ucellShadow

@Composable
fun TariffInfoCard(
    isFinancialBlock: Boolean,
    tariffInfo: TariffInfoData,
    onRetryRequestClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp)
            .ucellShadow(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp)) {
            Texts.H2(
                modifier = Modifier.padding(bottom = 2.dp),
                title = tariffInfo.title,
                color = colorResource(R.color.rich_purple)
            )
            tariffInfo.description?.let {
                Texts.ParagraphText(
                    modifier = Modifier.padding(bottom = 16.dp),
                    title = it,
                    color = colorResource(R.color.sky3)
                )
            }

            tariffInfo.ratePlanInfo?.let {
                Column {
                    Texts.ParagraphText(
                        modifier = Modifier.padding(bottom = 2.dp),
                        title = stringResource(R.string.my_tariff_monthly_fee),
                    )
                    Texts.H2(title = it)
                    if (!isFinancialBlock) {
                        SmallDivider(modifier = Modifier.padding(vertical = 16.dp))
                        Row {
                            Texts.ParagraphText(
                                modifier = Modifier.padding(bottom = 2.dp),
                                title = stringResource(R.string.my_tariff_next_payment)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            tariffInfo.nextPaymentDate?.let { Texts.H4(title = it) }
                        }
                    }
                }
            } ?: ErrorWithRetry(stringResource(R.string.error_rate_plan_info_loading), onRetryRequestClick)
        }
    }
}

data class TariffInfoData(
    val title: String,
    val description: String?,
    val ratePlanInfo: String?,
    val nextPaymentDate: String?
)
