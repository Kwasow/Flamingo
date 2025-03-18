package pl.kwasow.ui.screens.modules.whishlist

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.R
import pl.kwasow.data.Wish
import pl.kwasow.extensions.linkify
import pl.kwasow.utils.FlamingoDateUtils

// ====== Public composables
@Composable
fun WishView(
    wish: Wish,
    modifier: Modifier = Modifier,
) {
    val viewModel = koinViewModel<WishlistModuleViewModel>()

    WishView(
        wish = wish,
        isUpdating = viewModel.wishToUpdate?.id == wish.id,
        onDeleteRequest = { viewModel.askDeleteWish(wish) },
        onEditRequest = {
            viewModel.editedWish = wish
            viewModel.inputWishContent = wish.content
        },
        onChangeState = { viewModel.changeWishState(wish) },
        modifier = modifier,
    )
}

// ====== Private composables
@Composable
private fun WishView(
    wish: Wish,
    isUpdating: Boolean,
    onDeleteRequest: () -> Unit,
    onEditRequest: () -> Unit,
    onChangeState: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier.padding(vertical = 8.dp),
    ) {
        LoadingCheckbox(
            wish = wish,
            isUpdating = isUpdating,
            onChangeState = { onChangeState() },
        )

        Content(
            wish = wish,
            modifier = Modifier.weight(1f),
        )

        Actions(
            onDeleteRequest = onDeleteRequest,
            onEditRequest = onEditRequest,
        )
    }
}

@Composable
private fun LoadingCheckbox(
    wish: Wish,
    isUpdating: Boolean,
    onChangeState: () -> Unit,
) {
    AnimatedContent(
        targetState = isUpdating,
        label = "updating_wish_animation",
        modifier = Modifier.size(48.dp),
    ) { updating ->
        if (updating) {
            Box(modifier = Modifier.padding(16.dp)) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                )
            }
        } else {
            Checkbox(
                checked = wish.done,
                onCheckedChange = { onChangeState() },
            )
        }
    }
}

@Composable
private fun Content(
    wish: Wish,
    modifier: Modifier = Modifier,
) {
    val linkifiedText = wish.content.linkify()
    val decoration = if (wish.done) TextDecoration.LineThrough else TextDecoration.None

    Column(
        modifier = modifier.padding(top = ButtonDefaults.ContentPadding.calculateTopPadding()),
    ) {
        SelectionContainer {
            BasicText(
                text = linkifiedText,
                style =
                    TextStyle.Default.copy(
                        textDecoration = decoration,
                    ),
            )
        }
        Text(
            text = FlamingoDateUtils.timestampToString(wish.timestamp),
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray,
        )
    }
}

@Composable
private fun Actions(
    onDeleteRequest: () -> Unit,
    onEditRequest: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription =
                    stringResource(
                        id = R.string.contentDescription_three_dot_menu,
                    ),
                tint = Color.Gray,
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            Column {
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.edit)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription =
                                stringResource(
                                    id = R.string.contentDescription_edit_icon,
                                ),
                        )
                    },
                    onClick = {
                        expanded = false
                        onEditRequest()
                    },
                )
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.delete)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription =
                                stringResource(
                                    id = R.string.contentDescription_delete_icon,
                                ),
                        )
                    },
                    onClick = {
                        expanded = false
                        onDeleteRequest()
                    },
                )
            }
        }
    }
}

// ====== Previews
@Preview
@Composable
private fun WishViewPreview() {
    val wish =
        Wish(
            id = 0,
            author = "Anon",
            content = "This is a link to https://google.com",
            timestamp = 0,
            done = false,
        )

    WishView(
        wish = wish,
        isUpdating = false,
        onDeleteRequest = {},
        onEditRequest = {},
        onChangeState = {},
    )
}

@Preview
@Composable
private fun WishViewPreviewDone() {
    val wish =
        Wish(
            id = 0,
            author = "Anon",
            content = "This is a link to https://google.com",
            timestamp = 0,
            done = true,
        )

    WishView(
        wish = wish,
        isUpdating = false,
        onDeleteRequest = {},
        onEditRequest = {},
        onChangeState = {},
    )
}

@Preview
@Composable
private fun WishViewPreviewLoading() {
    val wish =
        Wish(
            id = 0,
            author = "Anon",
            content = "This is a link to https://google.com",
            timestamp = 0,
            done = false,
        )

    WishView(
        wish = wish,
        isUpdating = true,
        onDeleteRequest = {},
        onEditRequest = {},
        onChangeState = {},
    )
}

@Preview
@Composable
private fun WishViewPreviewNoLink() {
    val wish =
        Wish(
            id = 0,
            author = "Anon",
            content = "This is a wish without links",
            timestamp = 0,
            done = false,
        )

    WishView(
        wish = wish,
        isUpdating = false,
        onDeleteRequest = {},
        onEditRequest = {},
        onChangeState = {},
    )
}
