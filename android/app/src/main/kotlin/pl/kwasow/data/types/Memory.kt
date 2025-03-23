package pl.kwasow.data.types

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Serializable
data class Memory(
    val id: Int,
    val startDate: String,
    val endDate: String?,
    val title: String,
    val description: String,
    val photo: String?,
) {
    companion object {
        val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
    }

    // ====== Public methods
    fun getLocalStartDate(): LocalDate? {
        return try {
            LocalDate.parse(startDate)
        } catch (e: Exception) {
            null
        }
    }

    fun getStringDate(): String {
        val localDate = getLocalStartDate() ?: return startDate

        return dateFormatter.format(localDate)
    }
}
