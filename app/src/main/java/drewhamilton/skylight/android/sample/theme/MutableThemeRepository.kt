package drewhamilton.skylight.android.sample.theme

import androidx.appcompat.app.AppCompatDelegate
import dagger.Reusable
import drewhamilton.rxpreferences.RxPreferences
import drewhamilton.rxpreferences.edit
import drewhamilton.rxpreferences.getEnumStream
import drewhamilton.rxpreferences.putEnum
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

@Reusable
class MutableThemeRepository @Inject constructor(private val preferences: RxPreferences) {

    fun getSelectedDarkMode(): Observable<DarkMode> =
        preferences.getEnumStream(Keys.DARK_MODE, Defaults.DARK_MODE)

    fun selectDarkMode(darkMode: DarkMode): Completable = preferences.edit {
        putEnum(Keys.DARK_MODE, darkMode)
    }.andThen { AppCompatDelegate.setDefaultNightMode(darkMode.appCompatValue) }

    enum class DarkMode(
        val appCompatValue: Int
    ) {
        SYSTEM(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM),
        LIGHT(AppCompatDelegate.MODE_NIGHT_NO),
        DARK(AppCompatDelegate.MODE_NIGHT_YES)
    }

    private object Keys {
        const val DARK_MODE = "DarkMode"
    }

    private object Defaults {
        val DARK_MODE = DarkMode.SYSTEM
    }
}
