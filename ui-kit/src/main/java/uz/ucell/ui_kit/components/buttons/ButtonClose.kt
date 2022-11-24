package uz.ucell.ui_kit.components.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import uz.ucell.ui_kit.R

@Composable
fun ButtonClose(
    modifier: Modifier = Modifier,
    buttonStyle: ButtonStyle = ButtonStyle.GREY,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = modifier,
        onClick = { onClick() }
    ) {
        Image(
            modifier = modifier.clickable { onClick() },
            painter = painterResource(
                when (buttonStyle) {
                    ButtonStyle.RED -> R.drawable.ic_clear_round_red
                    ButtonStyle.GREY -> R.drawable.ic_clear_round_grey
                }
            ),
            contentDescription = "",
        )
    }
}

enum class ButtonStyle {
    GREY, RED
}
