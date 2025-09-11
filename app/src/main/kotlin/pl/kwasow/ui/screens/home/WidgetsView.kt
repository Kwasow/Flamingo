package pl.kwasow.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.kwasow.ui.widgets.daystogether.DaysTogetherWidget
import pl.kwasow.ui.widgets.memories.MemoriesWidget

// ====== Public composables
@Composable
fun WidgetsView() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        MemoriesWidget()
        DaysTogetherWidget(modifier = Modifier.padding(horizontal = 16.dp))
    }
}

// ====== Previews
// TODO: Broken preview
@Composable
@Preview
private fun WidgetsViewPreview() {
    WidgetsView()
}
