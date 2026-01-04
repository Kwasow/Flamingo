package pl.kwasow.ui.screens.modules.memories.dialogs

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.getSelectedDate
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import pl.kwasow.R
import pl.kwasow.flamingo.types.memories.Memory
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

// ====== Public composables
@Composable
fun DialogMemoriesShared(
    initialMemory: Memory,
    icon: @Composable (() -> Unit),
    title: @Composable (() -> Unit),
    onConfirm: (Memory) -> Unit,
    onCancel: () -> Unit,
) {
    var memoryValid by remember { mutableStateOf(true) }
    var newMemory by remember { mutableStateOf(initialMemory) }

    AlertDialog(
        icon = icon,
        title = title,
        text = {
            AlertContent(
                initialMemory = initialMemory,
                onUpdate = { valid, memory ->
                    memoryValid = valid
                    newMemory = memory
                },
            )
        },
        confirmButton = {
            AlertConfirmButton(
                enabled = memoryValid,
                onClick = { onConfirm(newMemory) },
            )
        },
        dismissButton = {
            AlertDismissButton(
                onClick = onCancel,
            )
        },
        onDismissRequest = onCancel,
    )
}

// ====== Private composables
@Composable
private fun AlertContent(
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

    Column {
        // Bigger title edit text
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title (TODO)") },
            singleLine = true,
        )

        // Normal description edit text
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description (TODO)") },
        )

        // Normal photo edit text
        OutlinedTextField(
            value = photo,
            onValueChange = { photo = it },
            label = { Text("Photo link (TODO)") },
            singleLine = true,
        )

        // Start date picker
        DatePicker(
            label = "Start (TODO)",
            value = startDate,
            onPick = { it?.let { startDate = it } },
            onReset = { startDate = initialMemory.startDate },
        )

        // End date picker
        DatePicker(
            label = "End (TODO)",
            value = endDate,
            onPick = { endDate = it },
            onReset = { endDate = initialMemory.endDate },
        )
    }
}

@Composable
private fun AlertConfirmButton(
    enabled: Boolean,
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        enabled = enabled,
    ) {
        Text(text = stringResource(id = R.string.save))
    }
}

@Composable
private fun AlertDismissButton(onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
    ) {
        Text(text = stringResource(id = R.string.cancel))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePicker(
    label: String,
    value: LocalDate?,
    onPick: (LocalDate?) -> Unit,
    onReset: () -> Unit,
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
