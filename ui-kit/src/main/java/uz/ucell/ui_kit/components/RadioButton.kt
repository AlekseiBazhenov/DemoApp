package uz.ucell.ui_kit.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R

@Composable
fun RadioButton(
    isSelected: Boolean,
    isEnabled: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    IconToggleButton(
        checked = isSelected,
        onCheckedChange = onCheckedChange,
        modifier = Modifier.size(24.dp)
    ) {
        fun getResIcon() = if (!isEnabled) {
            R.drawable.ic_radio_disabled
        } else if (isSelected) {
            R.drawable.ic_radio_selected
        } else {
            R.drawable.ic_radio_unselected
        }

        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(
                id = getResIcon()
            ),
            contentDescription = "",
            tint = Color.Unspecified
        )
    }
}

@Preview
@Composable
private fun RadioButtonSelectedPreview() {
    RadioButton(isSelected = true, isEnabled = true) {}
}

@Preview
@Composable
private fun RadioButtonNotSelectedPreview() {
    RadioButton(isSelected = false, isEnabled = true) {}
}

@Preview
@Composable
private fun RadioButtonDisabledPreview() {
    RadioButton(isSelected = true, isEnabled = false) {}
}
