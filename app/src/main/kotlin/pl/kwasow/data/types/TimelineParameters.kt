package pl.kwasow.data.types

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class TimelineParameters(
    val color: Color,
    val innerColor: Color,
    val radius: Dp,
    val strokeWidth: Dp,
) {
    companion object {
        @Composable
        fun defaults(): TimelineParameters =
            TimelineParameters(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.onPrimary,
                12.dp,
                2.dp,
            )
    }
}
