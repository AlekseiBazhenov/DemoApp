package uz.ucell.ui_kit.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.extentions.clickableWithoutIndication

@Composable
fun ActionLabel(
    modifier: Modifier = Modifier,
    text: String,
    painter: Painter? = null,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clickableWithoutIndication {
                if (isEnabled) {
                    onClick()
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        painter?.let {
            Icon(
                modifier = Modifier.padding(end = 7.dp),
                painter = painter,
                tint = if (isEnabled)
                    colorResource(R.color.rich_purple)
                else
                    colorResource(R.color.sky3),
                contentDescription = null
            )
        }
        Text(
            text = text,
            style = getTextStyle(isEnabled)
        )
    }
}

@Composable
private fun getTextStyle(isEnabled: Boolean) =
    if (isEnabled)
        MaterialTheme.typography.subtitle2.copy(color = colorResource(R.color.rich_purple))
    else
        MaterialTheme.typography.subtitle2
