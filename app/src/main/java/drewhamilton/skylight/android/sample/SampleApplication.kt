package drewhamilton.skylight.android.sample

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.jakewharton.threetenabp.AndroidThreeTen
import drewhamilton.skylight.backport.SkylightDay
import drewhamilton.skylight.backport.dummy.dagger.DummySkylightComponent
import drewhamilton.skylight.backport.sso.dagger.SsoSkylightComponent
import org.threeten.bp.LocalDate

@Suppress("Unused")
class SampleApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        AppComponent.create(
            this,
            SsoSkylightComponent.create(),
            DummySkylightComponent.create(SkylightDay.NeverLight(LocalDate.now()))
        )

        val savedDarkMode = AppComponent.instance.themeRepository().getSelectedDarkMode().blockingFirst()
        AppCompatDelegate.setDefaultNightMode(savedDarkMode.appCompatValue)
    }
}
