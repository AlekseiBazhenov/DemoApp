package uz.ucell.ui_kit.components.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R

@Composable
fun PinCodeInput(
    modifier: Modifier = Modifier,
    input: String,
    codeLength: Int,
    isError: Boolean = false,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 32.dp),
    ) {
        for (i in 0 until codeLength) {
            CodeCell(
                value = input.getOrNull(i),
                isError = isError
            )
        }
    }
}

@Composable
private fun CodeCell(
    value: Char?,
    isError: Boolean
) {
    Box(
        modifier = Modifier
            .requiredSize(12.dp)
            .background(
                if (isError)
                    colorResource(R.color.fury)
                else if (value == null)
                    colorResource(R.color.sky1)
                else
                    colorResource(R.color.rich_purple),
                CircleShape
            )
    )
}
