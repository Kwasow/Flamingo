package pl.kwasow.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import pl.kwasow.R

private const val COLUMNS = 9

// ====== Public composables
@Composable
fun FlamingoBackgroundLight(modifier: Modifier = Modifier) {
    FlamingoBackground(modifier = modifier.alpha(0.35F))
}

@Composable
fun FlamingoBackground(modifier: Modifier = Modifier) {
    val painter = painterResource(id = R.drawable.ic_background_icon)

    LazyVerticalGrid(
        columns = GridCells.Fixed(COLUMNS),
        modifier = modifier.fillMaxSize(),
        userScrollEnabled = false,
    ) {
        // We want to to generate items as long as they don't fill the entire screen
        items(Int.MAX_VALUE) { i ->
            val row = i / COLUMNS
            val column = i % COLUMNS
            val direction = if (column % 2 == 0) -1 else 1

            if ((row + column) % 2 == 0) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.rotate(25f * direction),
                )
            }

            Box(Modifier)
        }
    }
}
