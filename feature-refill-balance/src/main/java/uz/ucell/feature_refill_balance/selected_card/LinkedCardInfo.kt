package uz.ucell.feature_refill_balance.selected_card

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import uz.ucell.feature_refill_balance.R
import uz.ucell.ui_kit.components.Texts

@Composable
fun LinkedCardInfo(cardData: CardData) {
    Image(
        modifier = Modifier.padding(end = 12.dp),
        painter = painterResource(cardData.image),
        contentDescription = null
    )
    Column {
        Texts.ParagraphText(
            modifier = Modifier.padding(bottom = 2.dp),
            title = cardData.cardTitle,
            color = colorResource(if (cardData.available) R.color.base_black else R.color.fury)
        )
        Texts.Caption(title = cardData.cardInfo)
    }
}
