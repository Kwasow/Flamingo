package pl.kwasow.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.kwasow.R

// ====== Public composables
@Composable
fun AnimatedRefreshButton(
    onRefresh: () -> Unit,
    isRefreshing: Boolean,
) {
    IconButton(
        onClick = onRefresh,
        enabled = !isRefreshing,
    ) {
        AnimatedContent(
            targetState = isRefreshing,
            transitionSpec = {
                fadeIn(animationSpec = tween(220, delayMillis = 90))
                    .togetherWith(fadeOut(animationSpec = tween(90)))
            },
        ) { isRefreshing ->
            if (isRefreshing) {
                LoadingView(modifier = Modifier.padding(8.dp))
            } else {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = stringResource(id = R.string.contentDescription_refresh),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

// ====== Previews
@Preview
@Composable
private fun NotLoadingPreview() {
    AnimatedRefreshButton(
        onRefresh = {},
        isRefreshing = false,
    )
}

@Preview
@Composable
private fun IsLoadingPreview() {
    AnimatedRefreshButton(
        onRefresh = {},
        isRefreshing = true,
    )
}
