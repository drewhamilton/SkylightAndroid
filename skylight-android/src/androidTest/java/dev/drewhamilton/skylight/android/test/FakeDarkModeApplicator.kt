package dev.drewhamilton.skylight.android.test

import dev.drewhamilton.skylight.android.DarkModeApplicator

class FakeDarkModeApplicator : DarkModeApplicator {
    var appliedMode: DarkModeApplicator.DarkMode? = null
        private set

    override fun applyMode(mode: DarkModeApplicator.DarkMode) {
        appliedMode = mode
    }
}
