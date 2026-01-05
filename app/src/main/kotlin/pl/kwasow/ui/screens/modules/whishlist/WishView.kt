package pl.kwasow.ui.screens.modules.whishlist

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.extensions.linkify
import pl.kwasow.flamingo.types.wishlist.Wish
import pl.kwasow.ui.components.EditDeleteActions
import pl.kwasow.utils.FlamingoDateUtils
import java.time.LocalDate

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

        EditDeleteActions(
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
            text = FlamingoDateUtils.localDateToString(wish.date),
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray,
        )
    }
}

// ====== Previews
@Preview
@Composable
private fun WishViewPreview() {
    val wish =
        Wish(
            id = 0,
            authorId = 123,
            content = "This is a link to https://google.com",
            date = LocalDate.of(2024, 8, 3),
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
            authorId = 123,
            content = "This is a link to https://google.com",
            date = LocalDate.of(2024, 8, 3),
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
            authorId = 123,
            content = "This is a link to https://google.com",
            date = LocalDate.of(2024, 8, 3),
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
            authorId = 123,
            content = "This is a wish without links",
            date = LocalDate.of(2024, 8, 3),
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
