package pl.kwasow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import org.koin.compose.KoinContext
import pl.kwasow.ui.App
import pl.kwasow.ui.theme.FlamingoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupSystemBars()

        setContent {
            FlamingoTheme {
                KoinContext {
                    App()
                }
            }
        }
    }

    private fun setupSystemBars() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}
