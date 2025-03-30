package pl.kwasow.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import pl.kwasow.ui.composition.LocalBottomBarHeight
import pl.kwasow.utils.FlamingoLogger

// ====== Public composables
@Composable
fun RootLayout(
    bottomBar: @Composable (Modifier) -> Unit = {},
    content: @Composable () -> Unit,
) {
    var bottomBarHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Box {
        CompositionLocalProvider(LocalBottomBarHeight provides bottomBarHeight) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                content()
            }
        }

        Box(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding(),
        ) {
            bottomBar(
                Modifier.onGloballyPositioned { coordinates ->
                    with(density) {
                        bottomBarHeight = coordinates.size.height.toDp()
                        FlamingoLogger.d(bottomBarHeight.toString())
                    }
                },
            )
        }
    }
}
