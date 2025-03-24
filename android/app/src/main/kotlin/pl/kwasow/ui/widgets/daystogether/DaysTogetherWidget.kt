package pl.kwasow.ui.widgets.daystogether

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.R

// ====== Public composables
@Composable
fun DaysTogetherWidget(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<DaysTogetherViewModel>()
    val daysTogether = viewModel.daysTogether.collectAsState(null)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
    ) {
        Text(
            buildAnnotatedString {
                withStyle(
                    style =
                        SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.displayMedium.fontSize,
                        ),
                ) {
                    val days = daysTogether.value
                    if (days == null) {
                        append("---- ")
                    } else {
                        append("$daysTogether. ")
                    }
                }
                withStyle(
                    style = SpanStyle(),
                ) {
                    append(
                        stringResource(id = R.string.widget_daystogether_day_together).uppercase(),
                    )
                }
            },
        )
    }
}

// ====== Previews
@Composable
@Preview
private fun DaysTogetherWidgetPreview() {
    DaysTogetherWidget()
}
