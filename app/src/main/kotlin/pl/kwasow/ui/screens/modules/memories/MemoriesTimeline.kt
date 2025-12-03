package pl.kwasow.ui.screens.modules.memories

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.kwasow.R
import pl.kwasow.flamingo.types.memories.Memory
import pl.kwasow.ui.components.TimelineView
import java.time.LocalDate

// ====== Public composables
@Composable
fun MemoriesTimeline(
    memories: List<Memory>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    if (memories.isEmpty()) {
        NoMemoriesView()
        return
    }

    TimelineView(
        modifier = modifier.padding(horizontal = 16.dp),
        contentPadding = contentPadding,
        dataArray = memories,
    ) { memory, innerModifier ->
        MemoryView(
            memory = memory,
            modifier = innerModifier,
        )
    }
}

// ====== Private composables
@Composable
private fun NoMemoriesView() {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(id = R.string.module_memories_no_memories),
            style = MaterialTheme.typography.titleMedium,
            color = Color.LightGray,
        )
    }
}

// ====== Previews
@Preview
@Composable
private fun MemoriesTimelinePreview() {
    val memory =
        Memory(
            id = 0,
            startDate = LocalDate.of(2024, 1, 1),
            endDate = LocalDate.of(2024, 1, 15),
            title = "Event name",
            description = "This is a description of a memory",
            photo =
                "https://en.wikipedia.org/wiki/Photograph#/media/" +
                    "File:Nic%C3%A9phore_Ni%C3%A9pce_Oldest_Photograph_1825.jpg",
        )

    MemoriesTimeline(memories = listOf(memory, memory, memory))
}

@Preview
@Composable
private fun NoMemoriesViewPreview() {
    NoMemoriesView()
}
