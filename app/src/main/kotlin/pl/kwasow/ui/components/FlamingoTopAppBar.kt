package pl.kwasow.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pl.kwasow.R

// ====== Public composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlamingoTopAppBar(
    title: String,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(
                onClick = { onBackPressed() },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription =
                        stringResource(
                            id = R.string.contentDescription_navigate_back,
                        ),
                )
            }
        },
        modifier = modifier,
        colors = colors,
        actions = actions,
    )
}

// ====== Preview
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun FlamingoTopAppBarPreview() {
    FlamingoTopAppBar(
        title = "Preview",
        onBackPressed = {},
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun FlamingoTopAppBarWithActionsPreview() {
    FlamingoTopAppBar(
        title = "Preview",
        onBackPressed = {},
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar),
                    contentDescription = stringResource(id = R.string.contentDescription_calendar),
                )
            }
        },
    )
}
