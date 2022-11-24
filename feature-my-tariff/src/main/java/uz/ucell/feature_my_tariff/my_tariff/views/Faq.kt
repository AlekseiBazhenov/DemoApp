package uz.ucell.feature_my_tariff.my_tariff.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.components.divider.BlockDivider
import uz.ucell.ui_kit.components.expandable.ExpandableItem
import uz.ucell.ui_kit.extentions.clickableWithoutIndication

@Composable
fun Faq(
    items: List<Faq>,
    onMoreAboutTariffClicked: () -> Unit,
    linkClicked: (String) -> Unit,
) {
    BlockDivider()
    Column(modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp)) {
        Texts.H3(
            modifier = Modifier.padding(bottom = 12.dp),
            title = stringResource(R.string.my_tariff_faq)
        )
        items.forEach {
            ExpandableItem(
                title = it.question,
                content = {
                    Texts.Html(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        title = it.answer,
                        linkClicked = linkClicked
                    )
                }
            )
        }
        MoreAboutTariff(onMoreAboutTariffClicked)
    }
}

@Composable
private fun MoreAboutTariff(onClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .clickableWithoutIndication {
                onClicked()
            }
            .padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Texts.ParagraphText(
            modifier = Modifier.padding(end = 12.dp),
            title = stringResource(R.string.my_tariff_more_about),
            color = colorResource(R.color.rich_purple)
        )
        Icon(
            painter = painterResource(R.drawable.ic_arrow_right_14),
            contentDescription = null,
            tint = colorResource(R.color.rich_purple)
        )
    }
}

data class Faq(
    val question: String,
    val answer: String,
)
