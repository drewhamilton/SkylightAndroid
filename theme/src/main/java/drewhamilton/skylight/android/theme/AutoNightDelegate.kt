package drewhamilton.skylight.android.theme

import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import drewhamilton.skylight.backport.Coordinates
import drewhamilton.skylight.backport.SkylightDay
import drewhamilton.skylight.backport.SkylightForCoordinates
import drewhamilton.skylight.backport.calculator.CalculatorSkylight
import drewhamilton.skylight.backport.dummy.DummySkylight
import drewhamilton.skylight.backport.forCoordinates
import drewhamilton.skylight.backport.isDark
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoUnit

class AutoNightDelegate(
    private val skylight: SkylightForCoordinates = if (true) {
        CalculatorSkylight().forCoordinates(Coordinates(0.0, 0.0))
    } else {
        val zoneOffset = ZoneOffset.of(ZoneId.systemDefault().id)
        DummySkylight(SkylightDay.NeverDaytime(
            date = LocalDate.now(),
            dawn = OffsetTime.of(7, 0, 0, 0, zoneOffset),
            dusk = OffsetTime.of(22, 0, 0, 0, zoneOffset)
        )).forCoordinates(Coordinates(0.0, 0.0))
    }
) : DefaultLifecycleObserver {

    /*
     * Count down to a night mode change while the LifecycleOwner is active so the theme can change while the screen
     * is active.
     *
     * TODO: Use coroutines
     */
    private var darkModeTimer: CountDownTimer? = null

    override fun onStart(lifecycleOwner: LifecycleOwner) {
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

    override fun onStop(owner: LifecycleOwner) {
        darkModeTimer?.cancel()
        darkModeTimer = null
    }

    private fun updateDarkMode() = AppCompatDelegate.setDefaultNightMode(
        if (skylight.isDark(ZonedDateTime.now()))
            AppCompatDelegate.MODE_NIGHT_YES
        else
            AppCompatDelegate.MODE_NIGHT_NO
    )

    private val SkylightDay.nextEvent get(): OffsetTime? {
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
