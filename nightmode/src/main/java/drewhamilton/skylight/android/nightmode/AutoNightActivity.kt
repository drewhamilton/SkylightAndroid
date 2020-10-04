package drewhamilton.skylight.android.nightmode

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.drewhamilton.skylight.android.factory.AndroidSkylightFactory

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
        AutoNightDelegate(delegate, AndroidSkylightFactory.createForLocation(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(autoNightDelegate)
    }
}
