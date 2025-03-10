package pl.kwasow.sunshine.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.kwasow.sunshine.R

// ====== Public composables
@Composable
fun SettingsEntry(
    icon: Painter,
    iconTint: Color = LocalContentColor.current,
    name: String,
    description: String = "",
    onClick: () -> Unit,
    trailingContent: @Composable () -> Unit = {},
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clickable { onClick() }
                .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SettingIcon(
            icon = icon,
            tint = iconTint,
            name = name,
        )
        SettingName(
            name = name,
            description = description,
            modifier = Modifier.weight(1f),
        )
        trailingContent()
    }
}

// ====== Private composable
@Composable
private fun SettingIcon(
    icon: Painter,
    name: String,
    tint: Color = LocalContentColor.current,
) {
    Icon(
        icon,
        contentDescription =
            stringResource(
                id = R.string.contentDescription_settings_entry_icon,
                name,
            ),
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        tint = tint,
    )
}

@Composable
private fun SettingName(
    name: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        if (description.isNotEmpty()) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = ContentAlpha.medium),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

// ====== Previews
@Preview
@Composable
private fun SettingsEntryPreview() {
    SettingsEntry(
        icon = rememberVectorPainter(image = Icons.Outlined.Delete),
        name = "Settings entry",
        description = "This is the setting description",
        onClick = {},
    )
}
