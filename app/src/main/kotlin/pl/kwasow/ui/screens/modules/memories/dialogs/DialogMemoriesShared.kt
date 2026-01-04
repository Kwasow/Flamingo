package pl.kwasow.ui.screens.modules.memories.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.getSelectedDate
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import pl.kwasow.R
import pl.kwasow.flamingo.types.memories.Memory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

// ====== Public composables
@Composable
fun DialogMemoriesShared(
    initialMemory: Memory,
    icon:  @Composable (() -> Unit),
    title:  @Composable (() -> Unit),
    onConfirm: (Memory) -> Unit,
    onCancel: () -> Unit,
) {
    val buttonsEnabled = true

    AlertDialog(
        icon = icon,
        title = title,
        text = { AlertContent(initialMemory = initialMemory) },
        confirmButton = {
            AlertConfirmButton(
                enabled = buttonsEnabled,
                onClick = onConfirm,
            )
        },
        dismissButton = {
            AlertDismissButton(
                enabled = buttonsEnabled,
                onClick = onCancel,
            )
        },
        onDismissRequest = onCancel,
    )
}

// ====== Private composables
@Composable
private fun AlertContent(initialMemory: Memory) {
    var title by remember { mutableStateOf(initialMemory.title) }
    var startDate by remember { mutableStateOf(initialMemory.startDate) }
    var endDate by remember { mutableStateOf(initialMemory.endDate) }
    var description by remember { mutableStateOf(initialMemory.description) }
    var photo by remember { mutableStateOf(initialMemory.photo ?: "") }

    Column {
        // Bigger title edit text
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title (TODO)") },
            singleLine = true,
        )

        // Normal description edit text
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description (TODO)") },
        )

        // Normal photo edit text
        TextField(
            value = photo,
            onValueChange = { photo = it },
            label = { Text("Photo link (TODO)") },
        )

        // Start date picker
        DatePicker(
            label = "Start (TODO)",
            value = startDate,
            onPick = { it?.let { startDate = it } },
            onReset = { startDate = initialMemory.startDate }
        )

        // End date picker
        DatePicker(
            label = "End (TODO)",
            value = endDate,
            onPick = { endDate = it },
            onReset = { initialMemory.endDate }
        )
    }
}

@Composable
private fun AlertConfirmButton(
    enabled: Boolean,
    onClick: (Memory) -> Unit,
) {
    TextButton(
        onClick = { },
        enabled = enabled,
    ) {
        Text(text = stringResource(id = R.string.add))
    }
}

@Composable
private fun AlertDismissButton(
    enabled: Boolean,
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        enabled = enabled,
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
    val pickerState = rememberDatePickerState(
        initialSelectedDate = value,
        selectableDates = PastDates(),
    )
    var showPickerDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "$label: ${value?.toString() ?: "—"}"
        )

        IconButton(
            onClick = { showPickerDialog = true }
        ) {
            Icon(
                imageVector = Icons.Outlined.CalendarToday,
                contentDescription = "TODO"
            )
        }
    }

    if (showPickerDialog) {
        DatePickerDialog(
            onDismissRequest = { showPickerDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        showPickerDialog = false
                        onPick(pickerState.getSelectedDate())
                    }
                ) {
                    Text("OK (TODO)")
                }
            },
            dismissButton = {
                Button(onClick = { showPickerDialog = false }) {
                    Text("Cancel (TODO)")
                }
            },
        ) {
            DatePicker(state = pickerState)
        }
    }
}

private class PastDates : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis <= LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) * 1000
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year <= LocalDate.now().year
    }
}
