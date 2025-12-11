package pl.kwasow.utils

import android.text.format.DateUtils
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object FlamingoDateUtils {
    // ====== Fields
    private val textDateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)

    // ====== Public methods
    fun timestampToString(timestamp: Long): String {
        val instant = Instant.ofEpochMilli(timestamp)
        val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

        val month =
            when (date.month) {
                Month.JANUARY -> "stycznia"
                Month.FEBRUARY -> "lutego"
                Month.MARCH -> "marca"
                Month.APRIL -> "kwietnia"
                Month.MAY -> "maja"
                Month.JUNE -> "czerwca"
                Month.JULY -> "lipca"
                Month.AUGUST -> "sierpnia"
                Month.SEPTEMBER -> "września"
                Month.OCTOBER -> "października"
                Month.NOVEMBER -> "listopada"
                Month.DECEMBER -> "grudnia"
                else -> NullPointerException("date.month cannot be null")
            }

        return "${date.dayOfMonth} $month ${date.year}"
    }

    fun localDateToString(date: LocalDateTime): String = localDateToString(date.toLocalDate())

    fun localDateToString(date: LocalDate): String = textDateFormatter.format(date)

    fun getRelativeTimeString(date: LocalDateTime): String {
        val zonedDate = date.atZone(ZoneOffset.systemDefault())

        return DateUtils
            .getRelativeTimeSpanString(
                zonedDate.toEpochSecond() * 1000,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS,
            )
            .toString()
    }
}
