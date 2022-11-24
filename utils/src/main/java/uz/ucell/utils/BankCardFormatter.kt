package uz.ucell.utils

object BankCardFormatter {

    /**
     * Format bank crd for mask `#### #### #### ####`
     **/
    fun formatCard(cardRaw: String): String =
        buildString {
            val trimmed =
                if (cardRaw.length >= BANK_CARD_NUMBER_LENGTH) cardRaw.substring(0..15) else cardRaw
            trimmed.forEachIndexed { index, c ->
                append(c)
                if (index % 4 == 3 && index != 15) {
                    append(PHONE_FORMAT_SPACE)
                }
            }
        }
}

const val BANK_CARD_NUMBER_LENGTH = 16

private const val PHONE_FORMAT_SPACE = " "
