package pl.kwasow.flamingo.types.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import kotlinx.serialization.Serializable

@Serializable
data class User(
    override val id: Int,
    override val firstName: String,
    val email: String,
    override val icon: UserIcon,
    val couple: Couple,
) : MinimalUser {
    // ====== Fields
    val partner: Partner?
        get() = couple.members.find { member -> member.id != this.id }

    // ====== Constructors
    constructor(dto: UserDto) : this(
        id = dto.id,
        firstName = dto.firstName,
        email = dto.email,
        icon = dto.icon,
        couple = Couple(dto.couple),
    )
}

@Entity
@Table(name = "Users")
data class UserDto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Int,
    @Column(name = "first_name")
    override val firstName: String,
    @Column(name = "email")
    val email: String,
    @Column(name = "icon")
    @Enumerated(EnumType.STRING)
    override val icon: UserIcon,
    @ManyToOne(fetch = FetchType.LAZY)
    val couple: CoupleDto,
) : MinimalUser {
    // ====== Fields
    val partner: PartnerDto?
        get() = couple.members.find { member -> member.id != this.id }

    // ====== Constructors
    constructor() : this(-1, "", "", UserIcon.CAT, CoupleDto())
}
