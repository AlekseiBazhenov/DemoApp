package uz.ucell.ui_kit.components.chips

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.Texts

val chipsShape = RoundedCornerShape(32.dp)

@Composable
fun ChipsItem(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .then(modifier)
            .clip(chipsShape)
            .background(
                color = colorResource(if (isSelected) R.color.fresh_turquoise else R.color.sky0)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onClick(text) }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Texts.H4(
            modifier = Modifier.padding(vertical = 7.dp, horizontal = 16.dp),
            title = text,
            color = colorResource(if (isSelected) R.color.white else R.color.sky3)
        )
    }
}

@Preview
@Composable
private fun ChipsPreview() {
    ChipsItem(
        isSelected = true,
        text = "Text",
        onClick = {}
    )
}
