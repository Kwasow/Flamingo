package pl.kwasow.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import pl.kwasow.ui.composition.LocalBottomBarHeight

// ====== Public composables
@Composable
fun RootLayout(
    bottomBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    var bottomBarHeight by remember { mutableStateOf(0.dp) }

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
                    .onGloballyPositioned { coordinates ->
                        bottomBarHeight = coordinates.size.height.dp
                    },
        ) {
            bottomBar()
        }
    }
}
