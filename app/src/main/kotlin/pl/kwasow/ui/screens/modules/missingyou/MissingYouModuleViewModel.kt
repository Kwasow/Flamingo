package pl.kwasow.ui.screens.modules.missingyou

import android.content.Context
import android.media.MediaPlayer
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.kwasow.R
import pl.kwasow.managers.MessagingManager

class MissingYouModuleViewModel(
    private val applicationContext: Context,
    private val messagingManager: MessagingManager,
) : ViewModel() {
    // ====== Fields
    val snackbarHostState = SnackbarHostState()
    var hueVisible: Boolean by mutableStateOf(false)
        private set

    // ====== Public methods
    fun sendMissingYou(mediaPlayer: MediaPlayer) {
        viewModelScope.launch {
            val result = messagingManager.sendMissingYou()

            if (result) {
                hueVisible = true
                performLongVibration(applicationContext)

                mediaPlayer.start()
                snackbarHostState.showSnackbar(
                    message =
                        applicationContext.getString(
                            R.string.module_missingyou_sending_success,
                        ),
                    duration = SnackbarDuration.Short,
                )

                hueVisible = false
            } else {
                snackbarHostState.showSnackbar(
                    message =
                        applicationContext.getString(
                            R.string.module_missingyou_sending_failed,
                        ),
                    duration = SnackbarDuration.Long,
                )
            }
        }
    }

    // ====== Private methods
    private fun performLongVibration(context: Context) {
        val vibrator = context.getSystemService(Vibrator::class.java)
        vibrator.vibrate(
            VibrationEffect.createWaveform(
                LongArray(4) { i -> if (i % 2 == 0) 100 else 400 },
                -1,
            ),
        )
    }
}
