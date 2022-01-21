package dev.drewhamilton.skylight.android.demo

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dev.drewhamilton.skylight.android.demo.theme.ThemeRepository
import kotlinx.coroutines.runBlocking

@Suppress("Unused") // Used in manifest
class DemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AppComponent.create(this)

        val savedThemeMode: ThemeRepository.ThemeMode = runBlocking {
            AppComponent.instance.themeRepository().getSelectedThemeMode()
        }
        val appCompatNightMode = when (savedThemeMode) {
            ThemeRepository.ThemeMode.SYSTEM,
            ThemeRepository.ThemeMode.SKYLIGHT -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            ThemeRepository.ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            ThemeRepository.ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
        }
        AppCompatDelegate.setDefaultNightMode(appCompatNightMode)
    }
}
