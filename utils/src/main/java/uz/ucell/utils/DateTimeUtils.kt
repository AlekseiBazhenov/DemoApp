package uz.ucell.utils

import android.os.Build
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun formatSeconds(totalSeconds: Long): String {
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}

/**
 * Форматирует дату, полученную от сервера, в дату, которая отображается пользователю
 *
 * @param dateString Строка в формате "2022-08-17T17:50:59+05:00"
 * @return Строка в формате "17.08.2022"
 */
fun formatToDate(dateString: String): String = try {
    val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    dateString.toDate()?.let { dateFormat.format(it) } ?: ""
} catch (e: ParseException) {
    // todo send error to monitoring
    ""
}

fun calculateAgeFrom(dateString: String, format: String): Int {
    return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
        val from = LocalDate.parse(dateString, DateTimeFormatter.ofPattern(format))
        Period.between(
            from,
            LocalDate.now()
        ).years
    } else {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        val firstDate = sdf.parse(dateString)

        if (firstDate != null) {
            return Date().year - firstDate.year
        } else {
            ZERO
        }
    }
}

fun String.toDate(): Date? =
    SimpleDateFormat(SERVER_DATE_TIME_FORMAT, Locale.getDefault()).parse(this)

const val SERVER_SIMPLE_DATE_TIME_FORMAT = "yyyy-MM-dd"
const val SERVER_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZZZZZ"

const val ZERO: Int = 0

private const val DATE_FORMAT = "dd.MM.yyyy"
