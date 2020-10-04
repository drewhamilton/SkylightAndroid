package drewhamilton.skylight.android.sample

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication

@Suppress("Unused")
class SampleApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        AppComponent.create(this)

        val savedDarkMode = AppComponent.instance.themeRepository()
            .getSelectedDarkMode()
            .blockingFirst()
        AppCompatDelegate.setDefaultNightMode(savedDarkMode.appCompatValue)
    }
}
