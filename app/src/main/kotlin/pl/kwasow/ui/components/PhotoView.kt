package pl.kwasow.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import pl.kwasow.R
import pl.kwasow.ui.composition.LocalFlamingoNavigation

@Composable
fun PhotoView(
    uri: String,
    modifier: Modifier = Modifier,
    contentDescription: String = "",
    contentScale: ContentScale = ContentScale.Fit,
    clickable: Boolean = true,
) {
    val context = LocalContext.current
    val navigation = LocalFlamingoNavigation.current

    val imageRequest =
        ImageRequest
            .Builder(context)
            .data(uri)
            .diskCacheKey(uri)
            .memoryCacheKey(uri)
            .build()

    val mod =
        if (clickable) {
            modifier.clickable {
                navigation.navigateToPhoto(uri)
            }
        } else {
            modifier
        }

    SubcomposeAsyncImage(
        model = imageRequest,
        contentDescription = contentDescription,
        modifier = mod,
        loading = {
            LoadingView(modifier = Modifier.fillMaxSize())
        },
        error = { ErrorView() },
        contentScale = contentScale,
        alignment = Alignment.Center,
    )
}

// ====== Private composables
@Composable
private fun ErrorView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(id = R.string.failed_loading_photo),
            textAlign = TextAlign.Center,
        )
    }
}
