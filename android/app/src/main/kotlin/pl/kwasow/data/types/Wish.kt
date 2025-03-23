package pl.kwasow.data.types

import kotlinx.serialization.Serializable

@Serializable
data class Wish(
    val id: Int,
    val author: String,
    val content: String,
    val done: Boolean,
    val timestamp: Long,
) {
    // ====== Public methods
    fun update(
        newContent: String = content,
        newDone: Boolean = done,
    ): Wish = Wish(id, author, newContent, newDone, timestamp)
}
