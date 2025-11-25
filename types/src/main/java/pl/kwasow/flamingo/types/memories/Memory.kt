package pl.kwasow.flamingo.types.memories

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import kotlinx.serialization.Serializable
import pl.kwasow.flamingo.serializers.LocalDateSerializer
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Entity
@Serializable
@Table(name = "Memories")
data class Memory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = -1,
    @Serializable(with = LocalDateSerializer::class)
    @Column(name = "start_date")
    val startDate: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    @Column(name = "end_date")
    val endDate: LocalDate?,
    @Column(name = "title")
    val title: String,
    @Column(name = "memory_description")
    val description: String,
    @Column(name = "photo")
    val photo: String?,
    @Column(name = "couple")
    val coupleId: Int,
) {
    // ====== Fields
    companion object {
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
    }

    // ====== Constructors
    constructor() : this(-1, LocalDate.MIN, null, "", "", null, -1)

    // ====== Public methods
    fun getStringStartDate(): String = dateFormatter.format(startDate)
}
