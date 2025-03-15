package pl.kwasow.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pl.kwasow.ui.composition.LocalFlamingoNavigation
import pl.kwasow.ui.screens.modules.ModuleListItem
import pl.kwasow.ui.screens.modules.location.LocationModuleInfo
import pl.kwasow.ui.screens.modules.memories.MemoriesModuleInfo
import pl.kwasow.ui.screens.modules.missingyou.MissingYouModuleInfo
import pl.kwasow.ui.screens.modules.music.MusicModuleInfo
import pl.kwasow.ui.screens.modules.whishlist.WishlistModuleInfo

// ====== Public composables
@Composable
fun ModuleList(navigationBarPadding: Dp) {
    val navigation = LocalFlamingoNavigation.current

    Column(
        modifier =
            Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .padding(bottom = navigationBarPadding),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (MemoriesModuleInfo.enabled) {
            ModuleListItem(
                moduleInfo = MemoriesModuleInfo,
                navigateToModule = navigation.navigateToMemories,
            )
        }

        if (MusicModuleInfo.enabled) {
            ModuleListItem(
                moduleInfo = MusicModuleInfo,
                navigateToModule = navigation.navigateToMusic,
            )
        }

        if (WishlistModuleInfo.enabled) {
            ModuleListItem(
                moduleInfo = WishlistModuleInfo,
                navigateToModule = navigation.navigateToWishlist,
            )
        }

        if (MissingYouModuleInfo.enabled) {
            ModuleListItem(
                moduleInfo = MissingYouModuleInfo,
                navigateToModule = navigation.navigateToMissingYou,
            )
        }

        if (LocationModuleInfo.enabled) {
            ModuleListItem(
                moduleInfo = LocationModuleInfo,
                navigateToModule = navigation.navigateToLocation,
            )
        }
    }
}

// ====== Previews
@Composable
@Preview
private fun ModuleListPreview() {
    ModuleList(navigationBarPadding = 0.dp)
}
