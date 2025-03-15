package pl.kwasow.ui.screens.modules.whishlist

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.R
import pl.kwasow.ui.components.FlamingoBackgroundLight
import pl.kwasow.ui.components.FlamingoTopAppBar
import pl.kwasow.ui.composition.LocalFlamingoNavigation

// ====== Public composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistModuleScreen() {
    val viewModel = koinViewModel<WishlistModuleViewModel>()
    val navigation = LocalFlamingoNavigation.current

    LaunchedEffect(true) {
        viewModel.refreshWishlist()
    }

    Scaffold(
        topBar = {
            FlamingoTopAppBar(
                title = stringResource(WishlistModuleInfo.nameId),
                onBackPressed = navigation.navigateBack,
            )
        },
    ) { paddingValues ->
        FlamingoBackgroundLight(modifier = Modifier.padding(paddingValues))

        MainView(paddingValues = paddingValues)
    }
}

// ====== Private composables
@Composable
private fun MainView(paddingValues: PaddingValues) {
    val viewModel = koinViewModel<WishlistModuleViewModel>()
    val navigation = LocalFlamingoNavigation.current

    if (viewModel.tabs.isEmpty()) {
        return errorLoadingWishlist(navigation.navigateBack, LocalContext.current)
    }

    WishlistTabs(paddingValues = paddingValues)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WishlistTabs(paddingValues: PaddingValues) {
    val viewModel = koinViewModel<WishlistModuleViewModel>()
    val pagerState = rememberPagerState(pageCount = { viewModel.tabs.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
    ) {
        PrimaryTabRow(selectedTabIndex = pagerState.currentPage) {
            viewModel.tabs.forEachIndexed { index, item ->
                Tab(
                    text = { Text(text = item.title) },
                    icon = {
                        if (item.icon != null && item.iconDescription != null) {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = stringResource(id = item.iconDescription),
                            )
                        }
                    },
                    unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                )
            }
        }

        HorizontalPager(state = pagerState) { index ->
            viewModel.tabs[index].view()
        }
    }
}

private fun errorLoadingWishlist(
    onBackPressed: () -> Unit,
    context: Context,
) {
    Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_LONG).show()
    onBackPressed()
}

// ====== Previews
// TODO: Broken preview
@Preview
@Composable
private fun WishlistModuleScreenPreview() {
    WishlistModuleScreen()
}
