package pl.kwasow

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import pl.kwasow.koin.managersModule
import pl.kwasow.koin.viewModelsModule

class MainApplication : Application() {
    // ====== Interface methods
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(managersModule, viewModelsModule)
        }
    }
}
