package pl.kwasow.ui.screens.modules.memories.modals

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Preview
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.material.icons.outlined.Title
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.getSelectedDate
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.kwasow.R
import pl.kwasow.flamingo.types.memories.Memory
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

// ====== Public composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetMemoriesShared(
    initialMemory: Memory,
    title: String,
    onConfirm: (Memory) -> Unit,
    onCancel: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(),
) {
    var memoryValid by remember { mutableStateOf(true) }
    var newMemory by remember { mutableStateOf(initialMemory) }

    ModalBottomSheet(
        onDismissRequest = onCancel,
        sheetState = sheetState,
    ) {
        TitleBar(
            title = title,
            onSave = { onConfirm(newMemory) },
            onCancel = onCancel,
            enabled = memoryValid,
        )

        MainContent(
            initialMemory = initialMemory,
            onUpdate = { valid, memory ->
                memoryValid = valid
                newMemory = memory
            },
        )
    }
}

// ====== Private composables
@Composable
private fun TitleBar(
    title: String,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    enabled: Boolean,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(bottom = 16.dp),
    ) {
        IconButton(onClick = onCancel) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "TODO",
            )
        }

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.weight(1f))

        TextButton(
            onClick = onSave,
            enabled = enabled,
        ) {
            Text(text = stringResource(id = R.string.save))
        }
    }
}

@Composable
private fun MainContent(
    initialMemory: Memory,
    onUpdate: (Boolean, Memory) -> Unit,
) {
    var title by remember { mutableStateOf(initialMemory.title) }
    var startDate by remember { mutableStateOf(initialMemory.startDate) }
    var endDate by remember { mutableStateOf(initialMemory.endDate) }
    var description by remember { mutableStateOf(initialMemory.description) }
    var photo by remember { mutableStateOf(initialMemory.photo ?: "") }

    val valid by remember {
        derivedStateOf { title.isNotBlank() }
    }
    val newMemory by remember {
        derivedStateOf {
            initialMemory.copy(
                title = title,
                startDate = startDate,
                endDate = endDate,
                description = description,
                photo = photo,
            )
        }
    }

    LaunchedEffect(valid, newMemory) {
        onUpdate(valid, newMemory)
    }

    Column(
        modifier =
            Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 48.dp),
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(text = stringResource(id = R.string.module_memories_memory_title)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Title,
                    contentDescription = "TODO",
                )
            },
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = {
                Text(
                    text = stringResource(id = R.string.module_memories_memory_description),
                )
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Description,
                    contentDescription = "TODO",
                )
            },
        )

        OutlinedTextField(
            value = photo,
            onValueChange = { photo = it },
            label = { Text(text = stringResource(id = R.string.module_memories_memory_photo_url)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Image,
                    contentDescription = "TODO",
                )
            },
        )

        Row {
            DatePicker(
                label = stringResource(id = R.string.module_memories_memory_start_date),
                value = startDate,
                onPick = { it?.let { startDate = it } },
                onReset = { startDate = initialMemory.startDate },
                modifier = Modifier.weight(1f).padding(end = 6.dp),
            )

            DatePicker(
                label = stringResource(id = R.string.module_memories_memory_end_date),
                value = endDate,
                onPick = { endDate = it },
                onReset = { endDate = initialMemory.endDate },
                modifier = Modifier.weight(1f).padding(start = 6.dp),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePicker(
    label: String,
    value: LocalDate?,
    onPick: (LocalDate?) -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pickerState =
        rememberDatePickerState(
            initialSelectedDate = value,
            selectableDates = PastDates(),
        )
    var showPickerDialog by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    if (interactionSource.collectIsPressedAsState().value) {
        showPickerDialog = true
    }

    OutlinedTextField(
        value = value?.toString() ?: "—",
        onValueChange = {},
        label = { Text(text = label) },
        readOnly = true,
        interactionSource = interactionSource,
        trailingIcon = {
            IconButton(onClick = onReset) {
                Icon(
                    imageVector = Icons.Outlined.Restore,
                    contentDescription = "TODO",
                )
            }
        },
        modifier = modifier,
    )

    if (showPickerDialog) {
        DatePickerDialog(
            onDismissRequest = { showPickerDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        showPickerDialog = false
                        onPick(pickerState.getSelectedDate())
                    },
                ) {
                    Text(text = stringResource(id = R.string.ok))
                }
            },
            dismissButton = {
                Button(onClick = { showPickerDialog = false }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            },
        ) {
            DatePicker(
                state = pickerState,
                showModeToggle = false,
            )
        }
    }
}

private class PastDates : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val pick =
            Instant
                .ofEpochMilli(utcTimeMillis)
                .atZone(ZoneId.of("UTC"))
                .toLocalDate()
        val today = LocalDate.now()

        return pick.isBefore(today) || pick.isEqual(today)
    }

    override fun isSelectableYear(year: Int): Boolean = year <= LocalDate.now().year
}

// ====== Previews
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun BottomSheetMemoriesSharedEmptyMemoryPreview() {
    val initialMemory =
        Memory(
            id = null,
            startDate = LocalDate.now(),
            endDate = null,
            title = "",
            description = "",
            photo = null,
        )
    val sheetState =
        rememberStandardBottomSheetState(
            initialValue = SheetValue.Expanded,
        )

    BottomSheetMemoriesShared(
        initialMemory = initialMemory,
        title = "Adding memory",
        onConfirm = {},
        onCancel = {},
        sheetState = sheetState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun BottomSheetMemoriesSharedCorrectMemoryPreview() {
    val initialMemory =
        Memory(
            id = null,
            startDate = LocalDate.now(),
            endDate = null,
            title = "This is a title",
            description = "Some description",
            photo = null,
        )
    val sheetState =
        rememberStandardBottomSheetState(
            initialValue = SheetValue.Expanded,
        )

    BottomSheetMemoriesShared(
        initialMemory = initialMemory,
        title = "Adding memory",
        onConfirm = {},
        onCancel = {},
        sheetState = sheetState,
    )
}
