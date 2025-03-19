package pl.kwasow

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.view.WindowCompat
import org.koin.compose.KoinContext
import pl.kwasow.data.BuildFlavors
import pl.kwasow.ui.App
import pl.kwasow.ui.theme.FlamingoTheme

class MainActivity : AppCompatActivity() {
    // ====== Public methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupKaroniaDefaults()
        setupSystemBars()

        setContent {
            FlamingoTheme {
                KoinContext {
                    App()
                }
            }
        }
    }

    // ====== Private methods
    private fun setupSystemBars() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    private fun setupKaroniaDefaults() {
        if (BuildConfig.FLAVOR != BuildFlavors.KARONIA.flavorName) {
            return
        }

        val appLocale = LocaleListCompat.forLanguageTags("pl")
        AppCompatDelegate.setApplicationLocales(appLocale)
    }
}
