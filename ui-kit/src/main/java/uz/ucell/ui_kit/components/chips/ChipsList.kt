package uz.ucell.ui_kit.components.chips

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChipsList(
    modifier: Modifier = Modifier,
    chipsList: List<ChipsData>,
    select: String,
    onChipsItemClicked: (String) -> Unit
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(chipsList) { itemData ->
            ChipsItem(
                text = itemData.text,
                isSelected = itemData.text == select,
                onClick = { onChipsItemClicked(it) }
            )
        }
    }
}

data class ChipsData(
    val text: String,
)
