package pl.kwasow.ui.widgets.memories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import pl.kwasow.R
import pl.kwasow.ui.components.PhotoView

// ====== Public composables
@Composable
fun MemoryPhotoView(
    url: String,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.background(Color.Gray)) {
        PhotoView(
            uri = url,
            contentDescription = stringResource(id = R.string.contentDescription_memory_photo),
            contentScale = ContentScale.Crop,
            modifier =
                modifier
                    .fillMaxSize(),
        )
    }
}
