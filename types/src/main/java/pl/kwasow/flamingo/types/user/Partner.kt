package pl.kwasow.flamingo.types.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient as SerialTransient

@Entity
@Serializable
@Table(name = "Users")
data class Partner(
    @Id
    override val id: Int,
    @Column(name = "first_name")
    override val firstName: String,
    @Column(name = "icon")
    @Enumerated(EnumType.STRING)
    override val icon: UserIcon,
    @SerialTransient
    @Column(name = "couple_id")
    val coupleId: Int = -1,
) : MinimalUser {
    constructor() : this(-1, "", UserIcon.CAT, -1)
}
