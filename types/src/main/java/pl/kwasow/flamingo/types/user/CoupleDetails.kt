package pl.kwasow.flamingo.types.user

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Serializable
data class CoupleDetails(
    val anniversary: String,
) {
    // ====== Fields
    companion object {
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
    }

    // ====== Public methods
    fun getLocalAnniversaryDate(): LocalDate? {
        return try {
            LocalDate.parse(anniversary)
        } catch (e: Exception) {
            null
        }
    }

    fun getStringAnniversaryDate(): String {
        val localDate = getLocalAnniversaryDate() ?: return anniversary

        return dateFormatter.format(localDate)
    }
}
