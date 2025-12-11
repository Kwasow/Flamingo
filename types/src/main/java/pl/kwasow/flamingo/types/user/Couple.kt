package pl.kwasow.flamingo.types.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import kotlinx.serialization.Serializable
import pl.kwasow.flamingo.serializers.LocalDateSerializer
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Serializable
data class Couple(
    val id: Int,
    @Serializable(with = LocalDateSerializer::class)
    val anniversary: LocalDate,
    val members: List<Partner>,
) {
    // ====== Fields
    companion object {
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
    }

    val memberIds: List<Int>
        get() = members.map { it.id }

    val stringAnniversary: String
        get() = anniversary.format(dateFormatter)

    // ====== Constructors
    constructor(dto: CoupleDto) : this(
        id = dto.id,
        anniversary = dto.anniversary,
        members = dto.members.map { Partner(it) },
    )
}

@Entity
@Table(name = "Couples")
data class CoupleDto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    @Serializable(with = LocalDateSerializer::class)
    @Column(name = "anniversary_date")
    val anniversary: LocalDate,
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "coupleId")
    val members: MutableList<PartnerDto>,
) {
    // ====== Fields
    val memberIds: List<Int>
        get() = members.map { it.id }

    // ====== Constructors
    constructor() : this(-1, LocalDate.MIN, mutableListOf())
}
