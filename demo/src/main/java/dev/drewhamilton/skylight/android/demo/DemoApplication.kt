package dev.drewhamilton.skylight.android.demo

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication

@Suppress("Unused") // Used in manifest
class DemoApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        AppComponent.create(this)

        val savedDarkMode = AppComponent.instance.themeRepository()
            .getSelectedDarkMode()
            .blockingFirst()
        AppCompatDelegate.setDefaultNightMode(savedDarkMode.appCompatValue)
    }
}
