package dev.drewhamilton.skylight.android

/**
 * A general interface for applying a dark mode to the UI.
 */
interface DarkModeApplicator {

    /**
     * Apply the current night mode to whatever UI this instance is responsible for.
     */
    fun applyMode(mode: DarkMode)

    /**
     * A dark mode that can be applied to a UI.
     */
    enum class DarkMode {
        /**
         * Applies a dark UI.
         */
        DARK,

        /**
         * Applies a light UI.
         */
        LIGHT,

        /**
         * Applies a UI that is dark or light based on the system's dark mode setting.
         */
        FOLLOW_SYSTEM,
    }
}
