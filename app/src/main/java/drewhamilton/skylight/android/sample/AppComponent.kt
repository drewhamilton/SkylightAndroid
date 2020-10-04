package drewhamilton.skylight.android.sample

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import drewhamilton.skylight.android.sample.main.MainActivity
import drewhamilton.skylight.android.sample.settings.SettingsActivity
import drewhamilton.skylight.android.sample.theme.MutableThemeRepository
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
