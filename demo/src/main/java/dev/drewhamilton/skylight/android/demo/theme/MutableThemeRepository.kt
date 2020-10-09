package dev.drewhamilton.skylight.android.demo.theme

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

    fun getSelectedThemeMode(): Observable<ThemeMode> =
        preferences.getEnumStream(Keys.DARK_MODE, Defaults.DARK_MODE)

    fun selectThemeMode(themeMode: ThemeMode): Completable = preferences.edit {
        putEnum(Keys.DARK_MODE, themeMode)
    }.andThen {
        val appCompatNightMode = when (themeMode) {
            ThemeMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            else -> null
        }
        appCompatNightMode?.let { AppCompatDelegate.setDefaultNightMode(it) }
    }

    enum class ThemeMode {
        SYSTEM, SKYLIGHT, LIGHT, DARK
    }

    private object Keys {
        const val DARK_MODE = "DarkMode"
    }

    private object Defaults {
        val DARK_MODE = ThemeMode.SYSTEM
    }
}
