package dev.drewhamilton.skylight.android

/**
 * A general interface for applying night mode (or notnight mode) to the UI.
 */
// FIXME: Dumb name
interface DarkModeApplicator {
    /**
     * Apply the current night mode to whatever UI this instance is responsible for.
     */
    fun applyMode(mode: DarkMode)

    enum class DarkMode {
        DARK, LIGHT, FOLLOW_SYSTEM, BATTERY
    }
}
