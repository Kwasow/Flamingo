package pl.kwasow.ui.widgets.memories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import pl.kwasow.R
import pl.kwasow.extensions.nonScaledSp
import pl.kwasow.flamingo.types.Memory
import pl.kwasow.ui.components.PhotoView

// ====== Public composables
@Composable
fun MemoryView(memory: Memory) {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier =
            Modifier
                .padding(12.dp)
                .clickable { showDialog = true },
    ) {
        SharedMemoryView(memory = memory)
        Spacer(modifier = Modifier.fillMaxHeight())
    }

    if (showDialog) {
        MemoryDialog(memory = memory) {
            showDialog = false
        }
    }
}

// ====== Private composables
@Composable
private fun MemoryDialog(
    memory: Memory,
    onDismissRequest: () -> Unit,
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
            ) {
                memory.photo?.let { photo ->
                    Card(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                                .padding(bottom = 12.dp),
                    ) {
                        PhotoView(
                            uri = photo,
                            contentDescription =
                                stringResource(
                                    id = R.string.contentDescription_memory_photo,
                                ),
                            contentScale = ContentScale.FillWidth,
                        )
                    }
                }

                SharedMemoryView(
                    memory = memory,
                    trimLines = false,
                )
            }
        }
    }
}

@Composable
private fun SharedMemoryView(
    memory: Memory,
    trimLines: Boolean = true,
) {
    Text(
        text = memory.title,
        style = MaterialTheme.typography.titleLarge,
        fontSize = 20.nonScaledSp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier =
            Modifier
                .padding(bottom = 4.dp)
                .fillMaxWidth(),
    )
    Text(
        text = memory.getStringStartDate(),
        fontSize = 14.nonScaledSp,
        color = Color.Gray,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
    )
    Text(
        text = memory.description,
        modifier = Modifier.padding(vertical = 8.dp),
        fontSize = 12.nonScaledSp,
        lineHeight = 18.nonScaledSp,
        maxLines = if (trimLines) 4 else Int.MAX_VALUE,
        overflow = TextOverflow.Ellipsis,
    )
}
