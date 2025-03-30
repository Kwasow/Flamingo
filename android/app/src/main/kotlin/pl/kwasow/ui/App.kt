package pl.kwasow.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import pl.kwasow.extensions.slideComposable
import pl.kwasow.ui.components.RootLayout
import pl.kwasow.ui.composition.FlamingoNavigation
import pl.kwasow.ui.composition.LocalFlamingoNavigation
import pl.kwasow.ui.screens.home.HomeScreen
import pl.kwasow.ui.screens.login.LoginScreen
import pl.kwasow.ui.screens.modules.location.LocationModuleScreen
import pl.kwasow.ui.screens.modules.memories.MemoriesModuleScreen
import pl.kwasow.ui.screens.modules.missingyou.MissingYouModuleScreen
import pl.kwasow.ui.screens.modules.music.AlbumDetailsView
import pl.kwasow.ui.screens.modules.music.MusicModuleScreen
import pl.kwasow.ui.screens.modules.whishlist.WishlistModuleScreen
import pl.kwasow.ui.screens.photo.PhotoPreviewScreen
import pl.kwasow.ui.screens.settings.SettingsScreen
import pl.kwasow.ui.widgets.music.PlaybackControls

// ====== Public composables
@Composable
fun App() {
    RootLayout(
        bottomBar = { modifier -> BottomActions(modifier = modifier) },
        content = { NavContainer(modifier = Modifier.fillMaxSize()) },
    )
}

// ====== Private composables
@Composable
private fun NavContainer(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val viewModel = koinInject<AppViewModel>()

    FlamingoNavHost(
        navController = navController,
        modifier = modifier,
    ) {
        // ====== Main navigation
        slideComposable<HomeScreen> {
            HomeScreen()
        }

        slideComposable<LoginScreen> {
            LoginScreen()
        }

        composable<PhotoScreen> { backStackEntry ->
            val photo: PhotoScreen = backStackEntry.toRoute()

            PhotoPreviewScreen(uri = photo.uri)
        }

        slideComposable<SettingsScreen> {
            SettingsScreen()
        }

        // ====== Modules
        slideComposable<MemoriesScreen> {
            MemoriesModuleScreen()
        }

        slideComposable<MusicScreen> {
            MusicModuleScreen()
        }

        slideComposable<AlbumScreen> { backStackEntry ->
            val album: AlbumScreen = backStackEntry.toRoute()

            AlbumDetailsView(albumUuid = album.albumUuid)
        }

        slideComposable<WishlistScreen> {
            WishlistModuleScreen()
        }

        slideComposable<MissingYouScreen>(
            deepLinks =
                listOf(
                    navDeepLink<MissingYouScreen>(basePath = viewModel.missingYouUrl),
                ),
        ) { backStackEntry ->
            val missingYou: MissingYouScreen = backStackEntry.toRoute()

            MissingYouModuleScreen(interactionSource = missingYou.interactionSource)
        }

        slideComposable<LocationScreen> {
            LocationModuleScreen()
        }
    }
}

@Composable
private fun FlamingoNavHost(
    navController: NavHostController,
    modifier: Modifier,
    builder: NavGraphBuilder.() -> Unit,
) {
    val viewModel = koinViewModel<AppViewModel>()
    val flamingoNavigation =
        FlamingoNavigation(
            navigateToHome = { navController.navigate(HomeScreen) { popUpTo(0) } },
            navigateToLogin = { navController.navigate(LoginScreen) { popUpTo(0) } },
            navigateToPhoto = { navController.navigate(PhotoScreen(it)) },
            navigateToSettings = { navController.navigate(SettingsScreen) },
            navigateToMemories = { navController.navigate(MemoriesScreen) },
            navigateToMusic = { navController.navigate(MusicScreen) },
            navigateToMusicAlbum = { navController.navigate(AlbumScreen(it)) },
            navigateToWishlist = { navController.navigate(WishlistScreen) },
            navigateToMissingYou = { navController.navigate(MissingYouScreen) },
            navigateToLocation = { navController.navigate(LocationScreen) },
            navigateBack = { navController.popBackStack() },
        )

    CompositionLocalProvider(LocalFlamingoNavigation provides flamingoNavigation) {
        NavHost(
            navController = navController,
            startDestination = viewModel.getInitialRoute(),
            modifier = modifier,
            builder = builder,
        )
    }
}

@Composable
private fun BottomActions(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        PlaybackControls()
    }
}
