package uz.ucell.feature_refill_balance.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.NotificationImages
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.components.buttons.ButtonClose
import uz.ucell.ui_kit.components.buttons.ButtonSize
import uz.ucell.ui_kit.components.buttons.ButtonText
import uz.ucell.ui_kit.components.buttons.ButtonTheme

@Composable
fun BlockedCardNotification(
    modifier: Modifier = Modifier,
    onCloseClicked: () -> Unit
) {
    Card(
        modifier = modifier,
        elevation = 2.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Box {
            Row(modifier = Modifier.padding(16.dp)) {
                NotificationImages.Warning(modifier = Modifier.padding(end = 12.dp))
                Column {
                    Texts.H4(
                        modifier = Modifier.padding(bottom = 4.dp),
                        title = stringResource(R.string.refill_balance_card_blocked_title)
                    )
                    Texts.Caption(
                        modifier = Modifier.padding(bottom = 12.dp),
                        title = stringResource(R.string.refill_balance_card_blocked_description),
                        color = colorResource(R.color.base_black)
                    )
                    ButtonText(
                        text = stringResource(R.string.button_go),
                        buttonTheme = ButtonTheme.PURPLE,
                        buttonSize = ButtonSize.EXTRA_SMALL,
                        isFullWidth = false,
                        withArrow = true
                    ) {
                        // TODO переход в раздел "Мои карты"
                    }
                }
            }
            ButtonClose(modifier = Modifier.align(Alignment.TopEnd)) {
                onCloseClicked()
            }
        }
    }
}
