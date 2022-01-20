package dev.drewhamilton.skylight.android.demo.theme

import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.Reusable
import dev.drewhamilton.skylight.android.demo.getEnum
import dev.drewhamilton.skylight.android.demo.setEnum
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@Reusable
class MutableThemeRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    @Named("database") private val databaseDispatcher: CoroutineDispatcher = Dispatchers.Default,
    @Named("main") private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
) {

    fun getSelectedThemeModeFlow(): Flow<ThemeMode> = dataStore.data.map {
        it.getEnum<ThemeMode>(Keys.DARK_MODE) ?: Defaults.DARK_MODE
    }

    suspend fun getSelectedThemeMode(): ThemeMode = dataStore.data
        .first()
        .getEnum<ThemeMode>(Keys.DARK_MODE)
        ?: Defaults.DARK_MODE

    suspend fun selectThemeMode(themeMode: ThemeMode) {
        withContext(databaseDispatcher) {
            dataStore.edit {
                it.setEnum(Keys.DARK_MODE, themeMode)
            }
        }

        val appCompatNightMode = when (themeMode) {
            ThemeMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            else -> null
        }
        appCompatNightMode?.let {
            withContext(mainDispatcher) {
                AppCompatDelegate.setDefaultNightMode(it)
            }
        }
    }

    enum class ThemeMode {
        SYSTEM, SKYLIGHT, LIGHT, DARK
    }

    private object Keys {
        val DARK_MODE = stringPreferencesKey("DarkMode")
    }

    private object Defaults {
        val DARK_MODE = ThemeMode.SYSTEM
    }
}
