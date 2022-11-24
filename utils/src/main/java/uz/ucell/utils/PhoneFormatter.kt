package uz.ucell.utils

object PhoneFormatter {

    /**
     * Format phone for mask `+998 ## ### ## ##`
     **/
    fun formatPhone(phoneRaw: String): String =
        buildString {
            append(PHONE_FORMAT_PREFIX_WITH_PLUS)
            phoneRaw.forEachIndexed { index, c ->
                append(c)
                if (index == 1 || index == 4 || index == 6) append(PHONE_FORMAT_SPACE)
            }
        }

    /**
     * Removes all not digit symbols
     */
    fun formatPhoneToNumeric(phone: String): String =
        phone.filter { it.isDigit() }

    /**
     * Return phone number `998#########`
     */
    fun addCountryCodeToInput(phone: String): String = PHONE_FORMAT_PREFIX + phone

    /**
     * Return phone number without country code
     */
    fun removeCountryCode(phone: String): String = phone.replace(PHONE_FORMAT_PREFIX, "")
}

const val PHONE_NUMBER_LENGTH = 9

private const val PHONE_FORMAT_PREFIX_WITH_PLUS = "+998 "
private const val PHONE_FORMAT_PREFIX = "998"
private const val PHONE_FORMAT_SPACE = " "
