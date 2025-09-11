package pl.kwasow.ui.widgets.memories

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.ui.components.LoadingView

// ====== Public composables
@Composable
fun MemoriesWidget() {
    val viewModel = koinViewModel<MemoriesWidgetViewModel>()

    LaunchedEffect(true) {
        viewModel.loadMemories()
    }

    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .aspectRatio(16f / 9f),
    ) {
        if (viewModel.areMemoriesLoading) {
            LoadingView(
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            MemoryCarousel(
                todayMemories = viewModel.todayMemories,
                leadingMemories = viewModel.leadingMemories,
            )
        }
    }
}
