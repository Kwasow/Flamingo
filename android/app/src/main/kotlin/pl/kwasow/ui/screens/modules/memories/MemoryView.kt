package pl.kwasow.ui.screens.modules.memories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.kwasow.R
import pl.kwasow.flamingo.types.Memory
import pl.kwasow.ui.components.PhotoView

// ====== Public composables
@Composable
fun MemoryView(
    memory: Memory,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = memory.title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp),
        )

        Text(
            text = memory.getStringStartDate(),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(bottom = 8.dp),
            color = Color.Gray,
        )

        Text(text = memory.description)

        memory.photo?.let { photo ->
            MemoryPhoto(url = photo)
        }
    }
}

// ====== Private composables
@Composable
private fun MemoryPhoto(url: String) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
    ) {
        PhotoView(
            uri = url,
            contentDescription = stringResource(id = R.string.contentDescription_memory_photo),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

// ====== Previews
@Preview
@Composable
private fun MemoryViewPreview() {
    val memory =
        Memory(
            id = 0,
            startDate = "2024-01-01",
            endDate = "2024-01-15",
            title = "Event name",
            description = "This is a description of a memory",
            photo =
                "https://en.wikipedia.org/wiki/Photograph#/media/" +
                    "File:Nic%C3%A9phore_Ni%C3%A9pce_Oldest_Photograph_1825.jpg",
        )

    MemoryView(memory)
}
