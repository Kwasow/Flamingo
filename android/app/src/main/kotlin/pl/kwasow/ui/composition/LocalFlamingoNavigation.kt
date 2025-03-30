package pl.kwasow.ui.composition

import androidx.compose.runtime.compositionLocalOf

data class FlamingoNavigation(
    val navigateToHome: () -> Unit = {},
    val navigateToLogin: () -> Unit = {},
    val navigateToPhoto: (uri: String) -> Unit = {},
    val navigateToSettings: () -> Unit = {},
    val navigateToMemories: () -> Unit = {},
    val navigateToMusic: () -> Unit = {},
    val navigateToMusicAlbum: (albumUuid: String) -> Unit = {},
    val navigateToWishlist: () -> Unit = {},
    val navigateToMissingYou: () -> Unit = {},
    val navigateToLocation: () -> Unit = {},
    val navigateBack: () -> Unit = {},
)

val LocalFlamingoNavigation =
    compositionLocalOf {
        FlamingoNavigation()
    }
