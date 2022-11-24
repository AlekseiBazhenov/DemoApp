package uz.ucell.feature_my_tariff.my_tariff.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import uz.ucell.feature_my_tariff.R
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.components.divider.BlockDivider
import uz.ucell.ui_kit.components.divider.SmallDivider
import uz.ucell.ui_kit.components.expandable.ExpandableCaption
import uz.ucell.ui_kit.components.expandable.ExpandableList

@Composable
fun MyTariffAdditionalTerms(
    title: String,
    info: TariffTerm,
) {
    BlockDivider()
    ExpandableList(
        modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
        title = title,
        initialStateExpanded = info.isExpanded
    ) {
        Column(Modifier.padding(top = 16.dp)) {
            info.items.forEach {
                ExpandableListItem(
                    it.name,
                    it.value,
                    it.description,
                    it.isDescriptionExpanded
                )
            }
        }
    }
}

@Composable
private fun ExpandableListItem(
    title: String,
    value: String,
    description: String?,
    isExpanded: Boolean
) {
    SmallDivider()
    Column {
        Row(modifier = Modifier.padding(vertical = 12.dp)) {
            Texts.ParagraphText(
                modifier = Modifier.weight(1f),
                title = title
            )
            Box(
                modifier = Modifier
                    .background(
                        color = colorResource(R.color.sky0),
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Texts.CaptionMedium(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    title = value
                )
            }
        }
        if (!description.isNullOrEmpty()) {
            ExpandableCaption(
                modifier = Modifier.padding(bottom = 12.dp),
                text = description,
                initialStateExpanded = isExpanded
            )
        }
    }
}

data class TariffTerm(
    val isExpanded: Boolean,
    val items: List<Item>,
) {
    data class Item(
        val name: String,
        val value: String,
        val description: String?,
        val isDescriptionExpanded: Boolean
    )
}
