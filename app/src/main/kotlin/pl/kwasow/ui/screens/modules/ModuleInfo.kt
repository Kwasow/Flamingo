package pl.kwasow.ui.screens.modules

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class ModuleInfo(
    @StringRes val nameId: Int,
    @DrawableRes val iconId: Int,
    val enabled: Boolean = true,
)
