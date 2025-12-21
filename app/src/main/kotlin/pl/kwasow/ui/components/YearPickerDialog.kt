package pl.kwasow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.kwasow.R
import pl.kwasow.data.types.ContentAlpha
import pl.kwasow.extensions.roundUpToMultiple

// ====== Public composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearPickerDialog(
    years: Set<Int>,
    currentYear: Int,
    isShowing: Boolean,
    onYearConfirmed: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    if (!isShowing) {
        return
    }

    BasicAlertDialog(
        onDismissRequest = onDismiss,
    ) {
        DialogContent(
            years = years,
            currentYear = currentYear,
            onYearConfirmed = onYearConfirmed,
            onDismiss = onDismiss,
        )
    }
}

// ====== Private composables
private const val ITEMS_IN_ROW = 3
private const val MIN_ROWS = 3

@Composable
private fun DialogContent(
    years: Set<Int>,
    currentYear: Int,
    onYearConfirmed: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    var pickedYear by remember { mutableIntStateOf(currentYear) }

    Surface(
        modifier =
            Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
        shape = MaterialTheme.shapes.large,
        tonalElevation = AlertDialogDefaults.TonalElevation,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            DialogIcon()
            DialogTitle()

            YearsGrid(
                years = years.toSortedSet(),
                currentYear = currentYear,
                selectedYear = pickedYear,
                onPick = { pickedYear = it },
            )

            ActionButtons(
                onConfirm = { onYearConfirmed(pickedYear) },
                onCancel = onDismiss,
            )
        }
    }
}

@Composable
private fun DialogIcon() {
    Icon(
        imageVector = Icons.Outlined.CalendarMonth,
        contentDescription = stringResource(id = R.string.contentDescription_calendar),
        tint = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun DialogTitle() {
    Text(
        text = stringResource(id = R.string.dialog_year_picker_title),
        style = MaterialTheme.typography.headlineSmall,
        modifier =
            Modifier
                .padding(vertical = (16.dp))
                .fillMaxWidth(),
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun YearsGrid(
    years: Set<Int>,
    currentYear: Int,
    selectedYear: Int,
    onPick: (Int) -> Unit,
) {
    val minYear = years.min()
    val maxYear = years.max()
    val count =
        (maxYear - minYear + 1)
            .coerceAtLeast(ITEMS_IN_ROW * MIN_ROWS)
            .roundUpToMultiple(ITEMS_IN_ROW)

    val colorOnSurface = MaterialTheme.colorScheme.onSurface
    val colorOnSurfaceDisabled = colorOnSurface.copy(alpha = ContentAlpha.disabled)

    LazyVerticalGrid(
        columns = GridCells.Fixed(count = ITEMS_IN_ROW),
        modifier =
            Modifier
                .fillMaxWidth(),
    ) {
        items(count = count, key = { index -> minYear + index }) { i ->
            val year = minYear + i
            val backgroundColor =
                when (year) {
                    selectedYear -> MaterialTheme.colorScheme.primary
                    else -> Color.Transparent
                }
            val textColor =
                when (year) {
                    selectedYear -> MaterialTheme.colorScheme.onPrimary
                    currentYear -> MaterialTheme.colorScheme.primary
                    else -> if (years.contains(year)) colorOnSurface else colorOnSurfaceDisabled
                }
            val borderColor =
                when (year) {
                    currentYear -> MaterialTheme.colorScheme.primary
                    else -> Color.Transparent
                }

            val baseModifier =
                Modifier
                    .padding(vertical = 4.dp, horizontal = 16.dp)
                    .background(
                        color = backgroundColor,
                        shape = MaterialTheme.shapes.small,
                    )
                    .border(
                        width = 1.dp,
                        color = borderColor,
                        shape = MaterialTheme.shapes.small,
                    )
                    .width(IntrinsicSize.Min)
                    .padding(8.dp)
            val modifier =
                if (years.contains(year)) {
                    baseModifier.clickable { onPick(year) }
                } else {
                    baseModifier
                }

            Text(
                text = "$year",
                textAlign = TextAlign.Center,
                color = textColor,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun ActionButtons(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        TextButton(
            modifier = Modifier.padding(end = 8.dp, top = 24.dp),
            onClick = onCancel,
        ) {
            Text(text = stringResource(id = R.string.cancel))
        }

        TextButton(
            onClick = onConfirm,
            modifier = Modifier.padding(top = 24.dp),
        ) {
            Text(text = stringResource(id = R.string.confirm))
        }
    }
}

// ====== Preview
@Preview
@Composable
private fun YearPickerDialogPreview() {
    YearPickerDialog(
        years = setOf(2021, 2022, 2023, 2025, 2027, 2028, 2029, 2035),
        currentYear = 2022,
        isShowing = true,
        onYearConfirmed = {},
        onDismiss = {},
    )
}

@Preview
@Composable
private fun YearGridPreview() {
    YearsGrid(
        years = setOf(2021, 2022, 2023, 2025, 2027, 2028, 2029, 2034),
        currentYear = 2022,
        selectedYear = 2024,
        onPick = {},
    )
}
