package pl.kwasow.ui

import kotlinx.serialization.Serializable

// ====== Main navigation
@Serializable
object HomeScreen

@Serializable
object LoginScreen

@Serializable
data class PhotoScreen(val uri: String)

@Serializable
object SettingsScreen

// ====== Modules
@Serializable
object MemoriesScreen

@Serializable
object MusicScreen

@Serializable
data class AlbumScreen(val albumUuid: String)

@Serializable
object WishlistScreen

@Serializable
object MissingYouScreen

@Serializable
object LocationScreen
