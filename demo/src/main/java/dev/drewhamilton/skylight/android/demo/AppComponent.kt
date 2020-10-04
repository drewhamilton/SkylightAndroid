package dev.drewhamilton.skylight.android.demo

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dev.drewhamilton.skylight.android.demo.main.MainActivity
import dev.drewhamilton.skylight.android.demo.settings.SettingsActivity
import dev.drewhamilton.skylight.android.demo.theme.MutableThemeRepository
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class,
    SkylightModule::class
])
interface AppComponent {

    fun themeRepository(): MutableThemeRepository

    fun inject(mainActivity: MainActivity)

    fun inject(settingsActivity: SettingsActivity)

    @Component.Factory interface Factory {
        fun create(
            @BindsInstance application: Application,
        ): AppComponent
    }

    companion object {
        val instance get() = _instance

        @Suppress("ObjectPropertyName")
        private lateinit var _instance: AppComponent

        fun create(
            application: Application,
        ) {
            _instance = DaggerAppComponent.factory().create(application)
        }
    }
}
