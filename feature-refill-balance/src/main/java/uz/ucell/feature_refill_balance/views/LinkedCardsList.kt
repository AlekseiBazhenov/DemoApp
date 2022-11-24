package uz.ucell.feature_refill_balance.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.ucell.feature_refill_balance.selected_card.CardData
import uz.ucell.feature_refill_balance.selected_card.LinkedCardInfo
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.RadioButton
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.extentions.clickableWithoutIndication

@Composable
fun LinkedCardsList(
    modifier: Modifier = Modifier,
    cards: List<CardData>,
    selectedCardId: String?,
    onCardItemClicked: (CardData) -> Unit,
    onAddNewCardClicked: () -> Unit
) {
    val hasBlockedCard = cards.find { !it.available } != null
    val showNotification = remember { mutableStateOf(true) }
    LazyColumn(modifier = modifier) {
        if (hasBlockedCard && showNotification.value) {
            item {
                BlockedCardNotification(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp, bottom = 12.dp)
                ) {
                    showNotification.value = false
                }
            }
        }
        items(cards) { card ->

            fun onItemClick() {
                if (card.available) {
                    onCardItemClicked(card)
                }
            }

            Row(
                modifier = Modifier
                    .background(
                        color = if (selectedCardId == card.id) {
                            colorResource(R.color.soft_purple)
                        } else {
                            Color.Transparent
                        },
                        RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
                    .clickableWithoutIndication { onItemClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinkedCardInfo(card)
                Spacer(modifier = Modifier.weight(1f))
                RadioButton(
                    isSelected = selectedCardId == card.id,
                    isEnabled = card.available,
                    onCheckedChange = { onItemClick() }
                )
            }
        }
        item {
            AddNewCardItem(onAddNewCardClicked = onAddNewCardClicked)
        }
    }
}

@Composable
private fun AddNewCardItem(onAddNewCardClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .clickableWithoutIndication { onAddNewCardClicked() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.padding(end = 12.dp),
            painter = painterResource(R.drawable.ic_image_add_new_card),
            contentDescription = null
        )
        Column {
            Texts.ParagraphText(
                modifier = Modifier.padding(bottom = 2.dp),
                title = stringResource(R.string.label_new_payment_card),
            )
            Texts.Caption(title = stringResource(R.string.label_supported_payment_systems))
        }
        Spacer(modifier = Modifier.weight(1f))
        RadioButton(
            isSelected = false,
            isEnabled = true,
            onCheckedChange = { onAddNewCardClicked() }
        )
    }
}
