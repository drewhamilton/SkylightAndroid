package dev.drewhamilton.skylight.android

import androidx.appcompat.app.AppCompatDelegate
import dev.drewhamilton.skylight.android.AppCompatDelegateDarkModeApplicator.Scope

/**
 * A [DarkModeApplicator] that applies [DarkModeApplicator.DarkMode]s to [AppCompatDelegate].
 *
 * If [scope] is [Scope.GLOBAL], the mode is set as [AppCompatDelegate]'s mode for all components. If [scope] is
 * [Scope.LOCAL], the mode is set only on the given [appCompatDelegate].
 */
class AppCompatDelegateDarkModeApplicator @JvmOverloads constructor(
    private val appCompatDelegate: AppCompatDelegate,
    private val scope: Scope = Scope.GLOBAL
) : DarkModeApplicator {

    /**
     * Apply the given [mode] to [AppCompatDelegate] based on this [AppCompatDelegateDarkModeApplicator]'s [Scope].
     * Regardless of the scope, [AppCompatDelegate.applyDayNight] is called afterward.
     */
    override fun applyMode(mode: DarkModeApplicator.DarkMode) {
        val appCompatMode = mode.appCompatValue
        if (scope == Scope.GLOBAL)
            AppCompatDelegate.setDefaultNightMode(appCompatMode)
        else
            appCompatDelegate.localNightMode = appCompatMode

        // TODO: Check if this is necessary for GLOBAL scope
        appCompatDelegate.applyDayNight()
    }

    private val DarkModeApplicator.DarkMode.appCompatValue: Int
        get() = when (this) {
            DarkModeApplicator.DarkMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            DarkModeApplicator.DarkMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            DarkModeApplicator.DarkMode.FOLLOW_SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

    /**
     * The [AppCompatDelegate] scope to which a given [DarkModeApplicator.DarkMode] is applied: Either all components
     * or locally a single instance.
     */
    enum class Scope {
        GLOBAL, LOCAL
    }
}
