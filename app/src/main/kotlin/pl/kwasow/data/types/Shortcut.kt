package pl.kwasow.data.types

import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Shortcut(
    val id: String,
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
    val intent: Intent,
)
