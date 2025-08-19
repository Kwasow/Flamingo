package pl.kwasow.flamingo.types.wishlist

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import kotlinx.serialization.Serializable

@Entity
@Serializable
@Table(name = "Wishlist")
data class Wish(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
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
    constructor() : this(-1, -1, "", false, -1)

    // ====== Public methods
    fun update(
        newContent: String = content,
        newDone: Boolean = done,
    ): Wish = Wish(id, authorId, newContent, newDone, timestamp)
}
