package dev.drewhamilton.skylight.android.demo

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dev.drewhamilton.skylight.android.demo.theme.MutableThemeRepository

@Suppress("Unused") // Used in manifest
class DemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AppComponent.create(this)

        val savedThemeMode: MutableThemeRepository.ThemeMode = AppComponent.instance.themeRepository()
            .getSelectedThemeMode()
            .blockingFirst()
        val appCompatNightMode = when (savedThemeMode) {
            MutableThemeRepository.ThemeMode.SYSTEM,
            MutableThemeRepository.ThemeMode.SKYLIGHT -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            MutableThemeRepository.ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            MutableThemeRepository.ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
        }
        AppCompatDelegate.setDefaultNightMode(appCompatNightMode)
    }
}
