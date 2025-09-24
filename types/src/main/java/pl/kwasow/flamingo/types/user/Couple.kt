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

@Entity
@Serializable
@Table(name = "Couples")
data class Couple(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    @Serializable(with = LocalDateSerializer::class)
    @Column(name = "anniversary_date")
    val anniversary: LocalDate,
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "coupleId")
    val members: MutableList<Partner>,
) {
    // ====== Fields
    companion object {
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
    }

    // ====== Constructors
    constructor() : this(-1, LocalDate.MIN, mutableListOf())

    // ====== Public methods
    fun getStringAnniversaryDate(): String = dateFormatter.format(anniversary)

    fun getMemberIds(): List<Int> = members.map { it.id }
}
