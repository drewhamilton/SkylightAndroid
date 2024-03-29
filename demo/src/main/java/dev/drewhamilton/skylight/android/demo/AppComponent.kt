package dev.drewhamilton.skylight.android.demo

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dev.drewhamilton.skylight.Skylight
import dev.drewhamilton.skylight.android.demo.main.MainActivity
import dev.drewhamilton.skylight.android.demo.theme.ThemeRepository
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class,
    SkylightModule::class
])
interface AppComponent {

    fun skylight(): Skylight

    fun themeRepository(): ThemeRepository

    fun inject(mainActivity: MainActivity)

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
