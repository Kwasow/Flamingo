package pl.kwasow.ui.screens.modules.memories.modals

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Image
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
import androidx.compose.ui.text.input.KeyboardCapitalization
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
                contentDescription = stringResource(id = R.string.contentDescription_close),
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
    var titleValid by remember { mutableStateOf(true) }
    var startDate by remember { mutableStateOf(initialMemory.startDate) }
    var endDate by remember { mutableStateOf(initialMemory.endDate) }
    var endDateValid by remember { mutableStateOf(true) }
    var description by remember { mutableStateOf(initialMemory.description) }
    var photo by remember { mutableStateOf(initialMemory.photo ?: "") }

    val valid by remember { derivedStateOf { titleValid && endDateValid } }
    val newMemory by remember {
        derivedStateOf {
            initialMemory.copy(
                title = title,
                startDate = startDate,
                endDate = endDate,
                description = description,
                photo = photo.ifBlank { null },
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
            onValueChange = {
                title = it
                titleValid = it.isNotEmpty()
            },
            label = { Text(text = stringResource(id = R.string.module_memories_memory_title)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Title,
                    contentDescription =
                        stringResource(
                            id = R.string.contentDescription_title_icon,
                        ),
                )
            },
            isError = !titleValid,
            keyboardOptions =
                KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Sentences,
                ),
            supportingText = {
                if (!titleValid) {
                    Text(
                        text =
                            stringResource(
                                id = R.string.module_memories_memory_title_empty_error,
                            ),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
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
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Description,
                    contentDescription =
                        stringResource(
                            id = R.string.contentDescription_description_icon,
                        ),
                )
            },
            keyboardOptions =
                KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Sentences,
                ),
        )

        Row {
            DatePicker(
                initialValue = initialMemory.startDate,
                label = stringResource(id = R.string.module_memories_memory_start_date),
                onValueChange = {
                    it?.let {
                        startDate = it
                        endDateValid = endDate != null && it.isBefore(endDate)
                    }
                },
                modifier = Modifier.weight(1f).padding(end = 6.dp),
            )

            DatePicker(
                initialValue = initialMemory.endDate,
                label = stringResource(id = R.string.module_memories_memory_end_date),
                onValueChange = {
                    endDate = it
                    endDateValid = it != null && startDate.isBefore(it)
                },
                isError = !endDateValid,
                modifier = Modifier.weight(1f).padding(start = 6.dp),
                supportingText = {
                    if (!endDateValid) {
                        Text(
                            text =
                                stringResource(
                                    id = R.string.module_memories_memory_end_before_start_error,
                                ),
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                },
            )
        }

        OutlinedTextField(
            value = photo,
            onValueChange = { photo = it },
            label = { Text(text = stringResource(id = R.string.module_memories_memory_photo_url)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Image,
                    contentDescription =
                        stringResource(
                            id = R.string.contentDescription_photo_icon,
                        ),
                )
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePicker(
    initialValue: LocalDate?,
    label: String,
    onValueChange: (LocalDate?) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportingText: @Composable () -> Unit = {},
) {
    val pickerState =
        rememberDatePickerState(
            initialSelectedDate = initialValue,
            selectableDates = PastDates(),
        )
    var showPickerDialog by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    var value by remember { mutableStateOf(initialValue) }

    val onUpdate = { date: LocalDate? ->
        value = date
        onValueChange(date)
    }

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
            if (value != initialValue) {
                IconButton(onClick = { onUpdate(initialValue) }) {
                    Icon(
                        imageVector = Icons.Outlined.Restore,
                        contentDescription =
                            stringResource(
                                id = R.string.contentDescription_reset_date_icon,
                            ),
                    )
                }
            }
        },
        supportingText = supportingText,
        modifier = modifier,
        isError = isError,
    )

    if (showPickerDialog) {
        DatePickerDialog(
            onDismissRequest = { showPickerDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        showPickerDialog = false
                        onUpdate(pickerState.getSelectedDate())
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
