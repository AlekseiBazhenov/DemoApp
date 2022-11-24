package uz.ucell.ui_kit.components.inputs

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.chips.ChipsData
import uz.ucell.ui_kit.components.chips.ChipsList

@Composable
fun MoneyInput(
    input: String,
    recommendations: List<ChipsData>,
    onAmountValueChanged: (String) -> Unit,
    onRecommendedAmountClicked: (String) -> Unit
) {
    TextInputs.SingleLineTextInput(
        title = stringResource(R.string.amount),
        value = input,
        onValueChange = { newText ->
            val newTextFiltered = newText.filter { it.isDigit() }
            onAmountValueChanged(newTextFiltered)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        enabled = true,
        isError = false,
    )

    ChipsList(
        modifier = Modifier.padding(vertical = 8.dp),
        chipsList = recommendations,
        select = input,
        onChipsItemClicked = onRecommendedAmountClicked
    )
}
