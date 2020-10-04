package dev.drewhamilton.skylight.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * A base Activity that sets the current night mode using an [AutoNightDelegate].
 */
abstract class AutoNightActivity : AppCompatActivity() {

    /**
     * The delegate used to update this Activity's night mode while the Activity is displayed.
     *
     * Uses [AutoNightDelegate.fallback] by default. Should be overridden if a different [AutoNightDelegate] is
     * required.
     */
    protected open val autoNightDelegate: AutoNightDelegate by lazy {
        AutoNightDelegate(delegate, SkylightForMostRecentCoordinatesFactory().createForLocation(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(autoNightDelegate)
    }
}
