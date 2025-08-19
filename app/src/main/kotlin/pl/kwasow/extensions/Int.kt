package pl.kwasow.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.sp

val Int.nonScaledSp
    @Composable
    get() = (this / LocalDensity.current.fontScale).sp

fun Int.roundUpToMultiple(multiple: Int): Int {
    val remainder = this % multiple

    return if (remainder == 0) {
        this
    } else {
        this + (multiple - remainder)
    }
}
