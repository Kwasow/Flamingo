package pl.kwasow.flamingo.types.wishlist

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import kotlinx.serialization.Serializable

@Serializable
data class Wish(
    val id: Int? = null,
    val authorId: Int,
    val content: String,
    val done: Boolean,
    val timestamp: Long,
) {
    // ====== Constructors
    constructor(dto: WishDto) : this(
        id = dto.id,
        authorId = dto.authorId,
        content = dto.content,
        done = dto.done,
        timestamp = dto.timestamp,
    )

    // ====== Public methods
    fun update(
        newContent: String = content,
        newDone: Boolean = done,
    ): Wish = Wish(id, authorId, newContent, newDone, timestamp)
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
    @Column(name = "time_stamp")
    val timestamp: Long,
) {
    // ====== Constructors
    constructor(wish: Wish) : this(
        id = wish.id ?: -1,
        authorId = wish.authorId,
        content = wish.content,
        done = wish.done,
        timestamp = wish.timestamp,
    )

    constructor() : this(-1, -1, "", false, -1)

    // ====== Public methods
    fun update(
        newContent: String = content,
        newDone: Boolean = done,
    ): Wish = Wish(id, authorId, newContent, newDone, timestamp)
}
