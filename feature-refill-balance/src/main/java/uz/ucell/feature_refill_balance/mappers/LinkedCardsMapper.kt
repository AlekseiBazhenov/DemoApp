package uz.ucell.feature_refill_balance.mappers

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import uz.ucell.core.constatns.CARD_NUMBER_LAST_DIGITS
import uz.ucell.feature_refill_balance.R
import uz.ucell.feature_refill_balance.bank_card.BankCardUtils
import uz.ucell.feature_refill_balance.bank_card.PaymentSystemsAvailable
import uz.ucell.feature_refill_balance.selected_card.CardData
import uz.ucell.networking.network.response.LinkedCardsResponse
import uz.ucell.utils.toDate
import javax.inject.Inject

@ViewModelScoped
class LinkedCardsMapper @Inject constructor(@ApplicationContext val context: Context) {

    fun cards(list: List<LinkedCardsResponse.Card>): List<CardData> {
        val sortedCards = list.sortedBy { it.linkedDate.toDate() }
        val cards = mutableListOf<CardData>()
        sortedCards.forEachIndexed { index, item ->
            val paymentSystem = BankCardUtils.getLinkedCardPaymentSystem(item.maskedPan)
            val image = if (cards.count { it.paymentSystem == paymentSystem } == 0) {
                when (paymentSystem) {
                    PaymentSystemsAvailable.HUMO -> R.drawable.ic_logo_white_humo_with_bg_primary
                    PaymentSystemsAvailable.UZCARD -> R.drawable.ic_logo_white_uzcard_with_bg_primary
                }
            } else {
                when (paymentSystem) {
                    PaymentSystemsAvailable.HUMO -> R.drawable.ic_logo_white_humo_with_bg_secondary
                    PaymentSystemsAvailable.UZCARD -> R.drawable.ic_logo_white_uzcard_with_bg_secondary
                }
            }
            val cardNumberLastDigits = item.maskedPan.takeLast(CARD_NUMBER_LAST_DIGITS)
            cards.add(
                CardData(
                    id = item.id,
                    available = item.available,
                    cardInfo = if (index == 0) {
                        context.getString(R.string.label_main_payment_card, cardNumberLastDigits)
                    } else {
                        "$cardNumberLastDigits • ${paymentSystem.name}"
                    },
                    cardTitle = if (!item.available) {
                        item.description ?: ""
                    } else {
                        context.getString(
                            R.string.currency_uz_summ,
                            item.balance
                        ) // TODO: форматировать суммы
                    },
                    paymentSystem = paymentSystem,
                    image = image,
                    otpStatus = item.otpStatus
                )
            )
        }
        return cards.toList()
    }
}
