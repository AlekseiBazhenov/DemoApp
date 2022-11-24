package uz.ucell.feature_refill_balance.bank_card

object BankCardUtils {

    fun getCardPaymentSystem(cardNumber: String): PaymentSystemsAll {
        // TODO: add Visa, etc
        val cardNumberFirstBlock = cardNumber.take(CARD_NUMBER_BLOCK_LENGTH)
        return if (UZCARD_PREFIXES.contains(cardNumberFirstBlock)) {
            PaymentSystemsAll.UZCARD
        } else {
            PaymentSystemsAll.HUMO
        }
    }

    fun getLinkedCardPaymentSystem(cardNumber: String): PaymentSystemsAvailable {
        val cardNumberFirstBlock = cardNumber.take(CARD_NUMBER_BLOCK_LENGTH)
        return if (UZCARD_PREFIXES.contains(cardNumberFirstBlock)) {
            PaymentSystemsAvailable.UZCARD
        } else {
            PaymentSystemsAvailable.HUMO
        }
    }
}

private const val CARD_NUMBER_BLOCK_LENGTH = 4

private val UZCARD_PREFIXES = listOf("8600", "6262", "5614", "5440")
private val HUMO_PREFIXES = listOf("9860", "5555", "4073", "4294", "4062")
