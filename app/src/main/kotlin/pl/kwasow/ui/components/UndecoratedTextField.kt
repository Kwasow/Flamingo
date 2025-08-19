package pl.kwasow.ui.components

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.sp

@Composable
fun UndecoratedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    singleLine: Boolean = false,
) {
    BasicTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier =
            modifier
                .drawBehind {
                    if (value.isNotEmpty()) {
                        val strokeWidth = 1 * density
                        val y = size.height

                        drawLine(
                            Color.LightGray,
                            Offset(0f, y),
                            Offset(size.width, y),
                            strokeWidth,
                        )
                    }
                },
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
        textStyle = TextStyle.Default.copy(fontSize = 16.sp),
        decorationBox = { innerTextField ->
            if (value.isEmpty()) {
                Text(
                    text = hint,
                    color = Color.Gray,
                )
            }

            innerTextField()
        },
    )
}
