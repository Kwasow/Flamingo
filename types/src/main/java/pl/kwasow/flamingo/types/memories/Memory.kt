package pl.kwasow.flamingo.types.memories

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import kotlinx.serialization.Serializable
import pl.kwasow.flamingo.serializers.LocalDateSerializer
import pl.kwasow.flamingo.types.user.User
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Serializable
data class Memory(
    val id: Int? = null,
    @Serializable(with = LocalDateSerializer::class)
    val startDate: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    val endDate: LocalDate?,
    val title: String,
    val description: String,
    val photo: String?,
) {
    // ====== Fields
    companion object {
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
    }

    val stringStartDate: String
        get() = dateFormatter.format(startDate)

    val stringEndDate: String?
        get() = endDate?.let { dateFormatter.format(endDate) }

    // ====== Constructors
    constructor(dto: MemoryDto) : this(
        id = dto.id,
        startDate = dto.startDate,
        endDate = dto.endDate,
        title = dto.title,
        description = dto.description,
        photo = dto.photo,
    )
}

@Entity
@Table(name = "Memories")
data class MemoryDto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = -1,
    @Column(name = "start_date")
    val startDate: LocalDate,
    @Column(name = "end_date")
    val endDate: LocalDate?,
    @Column(name = "title")
    val title: String,
    @Column(name = "memory_description")
    val description: String,
    @Column(name = "photo")
    val photo: String?,
    @Column(name = "couple_id")
    val coupleId: Int,
) {
    // ====== Constructors
    constructor(memory: Memory, user: User) : this(
        id = memory.id ?: -1,
        startDate = memory.startDate,
        endDate = memory.endDate,
        title = memory.title,
        description = memory.description,
        photo = memory.photo,
        coupleId = user.couple.id,
    )

    constructor() : this(-1, LocalDate.MIN, null, "", "", null, -1)
}
