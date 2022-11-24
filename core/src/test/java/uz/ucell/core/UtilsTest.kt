package uz.ucell.core

import org.junit.Assert.assertTrue
import org.junit.Test
import uz.ucell.utils.PhoneFormatter

class UtilsTest {

    @Test
    fun `test msisdn format to ucell user friendly view`() {
        val msisdn1 = "98 123 45 67"
        val formattedMsisdn = "+998 98 123 45 67"
        assertTrue(formattedMsisdn == PhoneFormatter.formatPhone(msisdn1))

        val msisdn2 = "981234567"
        assertTrue(formattedMsisdn == PhoneFormatter.formatPhone(msisdn2))
    }

    @Test
    fun `test msisdn format to numeric`() {
        val msisdn = "+998 98 123 45 67"
        val formattedMsisdn = "998981234567"
        assertTrue(formattedMsisdn == PhoneFormatter.formatPhoneToNumeric(msisdn))
    }
}
