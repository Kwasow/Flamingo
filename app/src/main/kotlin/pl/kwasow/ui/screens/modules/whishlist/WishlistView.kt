package pl.kwasow.ui.screens.modules.whishlist

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.R
import pl.kwasow.flamingo.types.user.MinimalUser
import pl.kwasow.flamingo.types.user.Partner
import pl.kwasow.flamingo.types.user.UserIcon
import pl.kwasow.flamingo.types.wishlist.Wish
import pl.kwasow.ui.components.UndecoratedTextField
import java.time.LocalDate

// ====== Public composables
@Composable
fun WishlistView(user: MinimalUser) {
    val viewModel = koinViewModel<WishlistModuleViewModel>()

    WishlistView(
        user = user,
        wishes = viewModel.getPersonsWishes(user),
        isRefreshing = viewModel.isWishlistLoading,
        onRefresh = { viewModel.refreshWishlist() },
    )
}

// ====== Private composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistView(
    user: MinimalUser,
    wishes: List<Wish>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { onRefresh() },
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn {
            item {
                WishInput(
                    user = user,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )

                HorizontalDivider(
                    modifier = Modifier.height(1.dp),
                )
            }

            items(
                wishes,
                key = { it.id!! },
            ) { wish ->
                WishView(
                    wish = wish,
                    modifier =
                        Modifier
                            .padding(horizontal = 16.dp)
                            .animateItem(),
                )
            }
        }
    }

    DeleteWishDialog()
}

@Composable
private fun WishInput(
    user: MinimalUser,
    modifier: Modifier = Modifier,
) {
    val viewModel = koinViewModel<WishlistModuleViewModel>()

    Column(
        modifier = modifier.padding(vertical = 12.dp),
        horizontalAlignment = Alignment.End,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = stringResource(id = R.string.contentDescription_add_icon),
                modifier = Modifier.padding(end = 8.dp),
                tint = Color.Gray,
            )
            UndecoratedTextField(
                value = viewModel.inputWishContent,
                hint = stringResource(id = R.string.module_wishlist_wish_entry_hint),
                onValueChange = { viewModel.inputWishContent = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        WishInputActions(user = user)
    }
}

@Composable
private fun WishInputActions(user: MinimalUser) {
    val viewModel = koinViewModel<WishlistModuleViewModel>()

    AnimatedVisibility(visible = viewModel.inputWishContent.isNotBlank()) {
        AnimatedContent(
            targetState = viewModel.sendingWish,
            label = "adding_wish_animation",
        ) { addingWish ->
            if (addingWish) {
                LinearProgressIndicator(
                    modifier =
                        Modifier
                            .padding(vertical = 12.dp)
                            .width(64.dp),
                )
            } else {
                if (viewModel.editedWish != null) {
                    EditWishActions()
                } else {
                    AddWishActions(user = user)
                }
            }
        }
    }
}

@Composable
private fun EditWishActions() {
    val viewModel = koinViewModel<WishlistModuleViewModel>()

    Row {
        TextButton(
            onClick = {
                viewModel.inputWishContent = ""
                viewModel.editedWish = null
            },
        ) {
            Text(text = stringResource(id = R.string.cancel))
        }

        TextButton(
            onClick = { viewModel.updateEditedWish() },
        ) {
            Text(text = stringResource(id = R.string.save))
        }
    }
}

@Composable
private fun AddWishActions(user: MinimalUser) {
    val viewModel = koinViewModel<WishlistModuleViewModel>()

    TextButton(
        onClick = { viewModel.addWish(user) },
    ) {
        Text(text = stringResource(id = R.string.add))
    }
}

// ====== Previews
// TODO: Broken preview
@Preview
@Composable
private fun WishlistViewPreview() {
    val wish =
        Wish(
            id = 0,
            authorId = 123,
            content = "This is a link to https://google.com",
            date = LocalDate.of(2022, 1, 12),
            done = true,
        )

    WishlistView(
        user = Partner(123, "Anon", UserIcon.SHEEP),
        wishes = listOf(wish, wish, wish),
        isRefreshing = false,
        onRefresh = {},
    )
}
