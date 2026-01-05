package pl.kwasow.ui.screens.modules.memories

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.R
import pl.kwasow.ui.components.FlamingoBackgroundLight
import pl.kwasow.ui.components.FlamingoTopAppBar
import pl.kwasow.ui.components.YearPickerDialog
import pl.kwasow.ui.composition.LocalFlamingoNavigation
import pl.kwasow.ui.screens.modules.memories.modals.BottomSheetMemoriesAdd
import pl.kwasow.ui.screens.modules.memories.modals.BottomSheetMemoriesEdit

// ====== Public composables
@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun MemoriesModuleScreen() {
    val viewModel = koinViewModel<MemoriesModuleViewModel>()
    val hazeState = remember { HazeState() }
    val style = HazeMaterials.ultraThin(MaterialTheme.colorScheme.surface)

    Scaffold(
        topBar = {
            AppBar(
                modifier = Modifier.hazeEffect(state = hazeState, style = style),
            )
        },
        floatingActionButton = { FloatingActionButton() },
    ) { paddingValues ->
        MainView(
            paddingValues = paddingValues,
            hazeState = hazeState,
        )

        if (viewModel.memories.isNotEmpty()) {
            YearPickerDialog(
                years = viewModel.memories.keys,
                currentYear = viewModel.currentYear,
                isShowing = viewModel.showYearPickerDialog,
                onYearConfirmed = { year ->
                    viewModel.setSelectedYear(year)
                    viewModel.showYearPickerDialog = false
                },
                onDismiss = { viewModel.showYearPickerDialog = false },
            )
        }

        BottomSheetMemoriesAdd()
        BottomSheetMemoriesEdit()
    }
}

// ====== Private composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<MemoriesModuleViewModel>()
    val navigation = LocalFlamingoNavigation.current

    FlamingoTopAppBar(
        title = stringResource(id = MemoriesModuleInfo.nameId),
        onBackPressed = { navigation.navigateBack() },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent,
            ),
        modifier = modifier,
        actions = {
            if (viewModel.memories.isNotEmpty()) {
                IconButton(onClick = { viewModel.showYearPickerDialog = true }) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription =
                            stringResource(
                                id = R.string.contentDescription_calendar,
                            ),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
    )
}

@Composable
private fun FloatingActionButton() {
    val viewModel = koinViewModel<MemoriesModuleViewModel>()

    FloatingActionButton(
        onClick = { viewModel.showAddMemoryDialog = true },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        Icon(imageVector = Icons.Outlined.Add, contentDescription = "TODO")
    }
}

@Composable
private fun MainView(
    paddingValues: PaddingValues,
    hazeState: HazeState,
) {
    val viewModel = koinViewModel<MemoriesModuleViewModel>()

    FlamingoBackgroundLight(
        modifier =
            Modifier
                .hazeSource(state = hazeState)
                .padding(paddingValues),
    )

    PullToRefreshContainer(paddingValues = paddingValues) {
        if (viewModel.memoriesLoaded) {
            val currentYearMemories =
                viewModel.memories.getOrDefault(viewModel.currentYear, emptyList())

            MemoriesTimeline(
                memories = currentYearMemories,
                onEdit = { viewModel.editedMemory = it },
                modifier = Modifier.hazeSource(hazeState),
                contentPadding = paddingValues,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PullToRefreshContainer(
    paddingValues: PaddingValues,
    content: @Composable () -> Unit,
) {
    val viewModel = koinViewModel<MemoriesModuleViewModel>()
    val state = rememberPullToRefreshState()

    fun refresh(force: Boolean = false) {
        viewModel.refreshMemories(force)
    }

    PullToRefreshBox(
        isRefreshing = viewModel.areMemoriesLoading,
        onRefresh = { refresh(true) },
        modifier = Modifier.fillMaxSize(),
        state = state,
        indicator = {
            PullToRefreshDefaults.Indicator(
                state = state,
                isRefreshing = viewModel.areMemoriesLoading,
                modifier =
                    Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = paddingValues.calculateTopPadding()),
            )
        },
    ) {
        content()
    }
}

// ====== Previews
// TODO: Broken preview
@Preview
@Composable
private fun MemoriesModuleScreenPreview() {
    MemoriesModuleScreen()
}
