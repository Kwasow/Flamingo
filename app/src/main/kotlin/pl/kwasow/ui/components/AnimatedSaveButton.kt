package pl.kwasow.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.kwasow.R

// ====== Public composables
@Composable
fun AnimatedSaveButton(
    onSave: () -> Unit,
    isSaving: Boolean,
    isEnabled: Boolean,
) {
    TextButton(
        onClick = onSave,
        enabled = isEnabled && !isSaving,
    ) {
        AnimatedContent(
            targetState = isSaving,
            transitionSpec = {
                fadeIn(animationSpec = tween(220, delayMillis = 90))
                    .togetherWith(fadeOut(animationSpec = tween(90)))
            },
        ) { isSaving ->
            if (isSaving) {
                LoadingView(modifier = Modifier.size(8.dp))
            } else {
                Text(text = stringResource(id = R.string.save))
            }
        }
    }
}

// ====== Previews
@Preview
@Composable
private fun NotLoadingPreview() {
    AnimatedSaveButton(
        onSave = {},
        isSaving = false,
        isEnabled = true,
    )
}

@Preview
@Composable
private fun IsLoadingPreview() {
    AnimatedSaveButton(
        onSave = {},
        isSaving = true,
        isEnabled = true,
    )
}

@Preview
@Composable
private fun DisabledPreview() {
    AnimatedSaveButton(
        onSave = {},
        isSaving = false,
        isEnabled = false,
    )
}
