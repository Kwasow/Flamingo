package pl.kwasow.ui.widgets.daystogether

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
        AnimatedContent(
            targetState = daysTogether.value,
            label = "days_together",
            transitionSpec = { daysTogetherTransitionSpec(this) },
        ) { value ->
            if (value == null) {
                DaysTogetherText(null)
            } else {
                DaysTogetherText(value)
            }
        }
    }
}

// ====== Private composables
@Composable
private fun DaysTogetherText(daysTogether: Long?) {
    Text(
        buildAnnotatedString {
            withStyle(
                style =
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.displayMedium.fontSize,
                    ),
            ) {
                if (daysTogether == null) {
                    append("---- ")
                } else {
                    append("$daysTogether ")
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

private fun daysTogetherTransitionSpec(
    scope: AnimatedContentTransitionScope<Long?>,
): ContentTransform =
    with(scope) {
        val enterTransition = (
            fadeIn(animationSpec = tween(500)) +
                slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Down)
        )

        val exitTransition = (
            fadeOut(animationSpec = tween(500)) +
                slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Down)
        )

        enterTransition.togetherWith(exitTransition)
    }

// ====== Previews
@Composable
@Preview
private fun DaysTogetherWidgetPreview() {
    DaysTogetherWidget()
}
