package pl.kwasow.data.enums

import androidx.annotation.Keep

// For some reason R8 removes enum classes, so this annotation is necessary to prevent it
@Keep
enum class InteractionSource {
    User,
    Notification,
    Shortcut,
}
