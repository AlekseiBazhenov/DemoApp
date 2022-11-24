package uz.ucell.ui_kit.common_ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.components.buttons.ButtonCircle
import uz.ucell.ui_kit.components.buttons.ButtonSize
import uz.ucell.ui_kit.components.buttons.ButtonTheme

@Composable
fun ErrorWithRetry(
    text: String,
    onRetryRequestClick: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        ) {
            Texts.H4(modifier = Modifier.padding(bottom = 2.dp), title = text)
            Texts.Caption(title = stringResource(R.string.common_error_description))
        }
        ButtonCircle(
            buttonSize = ButtonSize.MEDIUM,
            buttonTheme = ButtonTheme.SOFT_PURPLE,
            imagePainter = painterResource(R.drawable.ic_refresh_32),
            onClick = onRetryRequestClick
        )
    }
}
