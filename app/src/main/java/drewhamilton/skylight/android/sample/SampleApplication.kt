package drewhamilton.skylight.android.sample

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import drewhamilton.skylight.SkylightDay
import drewhamilton.skylight.dummy.dagger.DummySkylightComponent
import drewhamilton.skylight.sso.dagger.SsoSkylightComponent
import java.time.LocalDate

@Suppress("Unused")
class SampleApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        AppComponent.create(
            this,
            SsoSkylightComponent.create(),
            DummySkylightComponent.create(SkylightDay.NeverLight { date = LocalDate.now() })
        )

        val savedDarkMode = AppComponent.instance.themeRepository().getSelectedDarkMode().blockingFirst()
        AppCompatDelegate.setDefaultNightMode(savedDarkMode.appCompatValue)
    }
}
