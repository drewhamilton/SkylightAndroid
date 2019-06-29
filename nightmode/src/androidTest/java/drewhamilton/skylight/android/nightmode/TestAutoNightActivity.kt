package drewhamilton.skylight.android.nightmode

import android.os.Bundle
import org.threeten.bp.Instant
import org.threeten.bp.OffsetTime
import org.threeten.bp.ZoneId

class TestAutoNightActivity : AutoNightActivity() {

    override lateinit var autoNightDelegate: AutoNightDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        val intent = intent
        val dawnMilli = intent.getLongExtra(KEY_DAWN_EPOCH_MILLI, -1)
        val duskMilli = intent.getLongExtra(KEY_DUSK_EPOCH_MILLI, -1)
        if (dawnMilli == -1L || duskMilli == -1L)
            throw IllegalArgumentException("Dawn and dusk times must be provided")

        autoNightDelegate = AutoNightDelegate.ofTimes(
            delegate,
            dawn = OffsetTime.ofInstant(Instant.ofEpochMilli(dawnMilli), ZoneId.systemDefault()),
            dusk = OffsetTime.ofInstant(Instant.ofEpochMilli(duskMilli), ZoneId.systemDefault())
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_text)
    }

    companion object {
        const val KEY_DAWN_EPOCH_MILLI = "Dawn epoch milli"
        const val KEY_DUSK_EPOCH_MILLI = "Dusk epoch milli"
    }
}
