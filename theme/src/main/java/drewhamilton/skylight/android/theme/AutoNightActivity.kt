package drewhamilton.skylight.android.theme

import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import drewhamilton.skylight.backport.Coordinates
import drewhamilton.skylight.backport.SkylightDay
import drewhamilton.skylight.backport.calculator.CalculatorSkylight
import drewhamilton.skylight.backport.forCoordinates
import drewhamilton.skylight.backport.isDark
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetTime
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoUnit

class AutoNightActivity : AppCompatActivity() {

    private val skylight = CalculatorSkylight().forCoordinates(Coordinates(0.0, 0.0))

    private var darkModeTimer: CountDownTimer? = null

    override fun onStart() {
        super.onStart()
        updateDarkMode()

        val nextEvent = skylight.getSkylightDay(LocalDate.now()).nextEvent
        if (nextEvent != null) {
            val millisUntil = OffsetTime.now().until(nextEvent, ChronoUnit.MILLIS) + 1000
            darkModeTimer = object : CountDownTimer(millisUntil, millisUntil) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() = updateDarkMode()
            }
        }
    }

    override fun onStop() {
        darkModeTimer?.cancel()
        darkModeTimer = null
        super.onStop()
    }

    private fun updateDarkMode() = setDefaultNightMode(
        if (skylight.isDark(ZonedDateTime.now()))
            MODE_NIGHT_YES
        else
            MODE_NIGHT_NO
    )

    private val SkylightDay.nextEvent: OffsetTime?
        get() {
            val now = OffsetTime.now()
            return when(this) {
                is SkylightDay.Typical -> when {
                    dawn.isAfter(now) -> dawn
                    dusk.isAfter(now) -> dusk
                    else -> null
                }
                is SkylightDay.NeverDaytime -> when {
                    dawn.isAfter(now) -> dawn
                    dusk.isAfter(now) -> dusk
                    else -> null
                }
                else -> null
            }
        }
}
