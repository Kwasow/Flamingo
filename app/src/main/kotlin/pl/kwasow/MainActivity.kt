package pl.kwasow

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.view.WindowCompat
import pl.kwasow.data.enums.BuildFlavors
import pl.kwasow.ui.App
import pl.kwasow.ui.theme.FlamingoTheme

class MainActivity : AppCompatActivity() {
    // ====== Interface methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupKaroniaDefaults()
        setupSystemBars()

        setContent {
            FlamingoTheme {
                App()
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
