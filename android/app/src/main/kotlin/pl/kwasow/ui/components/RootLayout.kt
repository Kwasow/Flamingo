package pl.kwasow.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

// ====== Public composables
@Composable
fun RootLayout(
    bottomBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Box {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                content()
            }

            Box(
                modifier = Modifier.align(Alignment.BottomCenter),
            ) {
                bottomBar()
            }
        }
    }
}
