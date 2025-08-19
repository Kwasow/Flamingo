package pl.kwasow.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import pl.kwasow.data.types.TimelineParameters

// ====== Public composables
@Composable
fun <T> TimelineView(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    dataArray: List<T>,
    item: @Composable (data: T, modifier: Modifier) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
    ) {
        item {
            Spacer(modifier = Modifier.padding(top = 12.dp))
        }

        itemsIndexed(dataArray) { index, data ->
            TimelineNode(isLast = index == dataArray.lastIndex) { modifier ->
                item(data, modifier)
            }
        }

        item {
            Spacer(modifier = Modifier.padding(top = 12.dp))
        }
    }
}

// ====== Private composables
@Composable
private fun TimelineNode(
    isLast: Boolean,
    content: @Composable (modifier: Modifier) -> Unit,
) {
    val timelineParameters = TimelineParameters.defaults()

    Box(
        modifier =
            Modifier
                .wrapContentSize()
                .drawTimeline(timelineParameters, isLast),
    ) {
        content(
            Modifier.padding(
                start = timelineParameters.radius + 24.dp,
                bottom = if (isLast) 0.dp else 16.dp,
            ),
        )
    }
}

private fun Modifier.drawTimeline(
    parameters: TimelineParameters,
    isLast: Boolean,
): Modifier {
    return drawBehind {
        val circleRadiusPx = parameters.radius.toPx()
        val smallCircleRadiusPx = circleRadiusPx / 2

        drawCircle(
            color = parameters.color,
            radius = circleRadiusPx,
            center = Offset(circleRadiusPx, circleRadiusPx),
        )

        drawCircle(
            color = parameters.innerColor,
            radius = smallCircleRadiusPx,
            center = Offset(circleRadiusPx, circleRadiusPx),
        )

        if (!isLast) {
            drawLine(
                color = parameters.color,
                start = Offset(x = circleRadiusPx, y = circleRadiusPx * 2),
                end = Offset(x = circleRadiusPx, y = this.size.height),
                strokeWidth = parameters.strokeWidth.toPx(),
            )
        }
    }
}
