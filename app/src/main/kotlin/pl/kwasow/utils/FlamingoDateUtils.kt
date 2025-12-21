package pl.kwasow.utils

import android.text.format.DateUtils
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object FlamingoDateUtils {
    // ====== Fields
    private val textDateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)

    // ====== Public methods
    fun localDateToString(date: LocalDateTime): String = localDateToString(date.toLocalDate())

    fun localDateToString(date: LocalDate): String = textDateFormatter.format(date)

    fun getRelativeTimeString(date: LocalDateTime): String {
        val zonedDate = date.atZone(ZoneOffset.systemDefault())

        return DateUtils
            .getRelativeTimeSpanString(
                zonedDate.toEpochSecond() * 1000,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS,
            ).toString()
    }
}
