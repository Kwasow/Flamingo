package pl.kwasow.flamingo.types.wishlist

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import kotlinx.serialization.Serializable
import pl.kwasow.flamingo.serializers.LocalDateSerializer
import java.time.LocalDate

@Serializable
data class Wish(
    val id: Int? = null,
    val authorId: Int,
    val content: String,
    val done: Boolean,
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate,
) {
    // ====== Constructors
    constructor(dto: WishDto) : this(
        id = dto.id,
        authorId = dto.authorId,
        content = dto.content,
        done = dto.done,
        date = dto.date,
    )

    // ====== Public methods
    fun update(
        newContent: String = content,
        newDone: Boolean = done,
    ): Wish = Wish(id, authorId, newContent, newDone, date)
}

@Entity
@Table(name = "Wishlist")
data class WishDto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = -1,
    @Column(name = "author")
    val authorId: Int,
    @Column(name = "content")
    val content: String,
    @Column(name = "done")
    val done: Boolean,
    @Column(name = "date")
    val date: LocalDate,
) {
    // ====== Constructors
    constructor(wish: Wish) : this(
        id = wish.id ?: -1,
        authorId = wish.authorId,
        content = wish.content,
        done = wish.done,
        date = wish.date,
    )

    constructor() : this(-1, -1, "", false, LocalDate.MIN)
}
