package pl.kwasow.ui

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pl.kwasow.data.enums.InteractionSource

// ====== Main navigation
@Serializable
object HomeScreen

@Serializable
object LoginScreen

@Serializable
data class PhotoScreen(
    val uri: String,
)

@Serializable
object SettingsScreen

// ====== Modules
@Serializable
object MemoriesScreen

@Serializable
object MusicScreen

@Serializable
data class AlbumScreen(
    val albumUuid: String,
)

@Serializable
object WishlistScreen

@Serializable
data class MissingYouScreen(
    @SerialName("source")
    val interactionSource: InteractionSource = InteractionSource.User,
)

@Serializable
object LocationScreen
