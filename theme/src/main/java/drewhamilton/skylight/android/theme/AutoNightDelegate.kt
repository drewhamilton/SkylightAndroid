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
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoUnit

/**
 * A [androidx.lifecycle.FullLifecycleObserver] that updates the [AppCompatDelegate]'s default night mode. The night
 * is updated when the given [LifecycleOwner] is started, and again at the next dawn/dusk event if the [LifecycleOwner]
 * has not stopped.
 */
class AutoNightDelegate(
    private val skylight: SkylightForCoordinates
) : DefaultLifecycleObserver {

    /*
     * Count down to a night mode change while the LifecycleOwner is active so the theme can change while the screen
     * is active.
     *
     * TODO: Use coroutines
     */
    private var darkModeTimer: CountDownTimer? = null

    override fun onStart(lifecycleOwner: LifecycleOwner) {
        updateNightMode()
        startTimer()
    }

    override fun onStop(owner: LifecycleOwner) = stopTimer()

    private fun updateNightMode() = AppCompatDelegate.setDefaultNightMode(
        if (skylight.isDark(ZonedDateTime.now()))
            AppCompatDelegate.MODE_NIGHT_YES
        else
            AppCompatDelegate.MODE_NIGHT_NO
    )

    private fun startTimer() {
        val today = LocalDate.now()
        var nextEvent = skylight.getSkylightDay(today).nextEvent

        // If there is no dawn/dusk event later today, so try tomorrow:
        if (nextEvent == null) {
            nextEvent = skylight.getSkylightDay(today.plusDays(1)).nextEvent
        }

        if (nextEvent == null) {
            // No dawn/dusk events any time soon; nothing to do
            stopTimer()
        } else {
            // Count down to the next dawn/dusk event
            val millisUntil = OffsetTime.now().absoluteMillisUntil(nextEvent) + 1000
            darkModeTimer = object : OneOffCountDownTimer(millisUntil) {
                override fun onFinish() {
                    // A dawn/dusk event has happened; update night mode again
                    updateNightMode()
                    // And then start counting down to the next event again, recursion-style
                    startTimer()
                }
            }
        }
    }

    /**
     * If [endExclusive] is before [this], treats [endExclusive] as the next day such that the result is always
     * non-negative. E.g. 1am is 2 hours (converted to millis) after 11pm.
     */
    private fun OffsetTime.absoluteMillisUntil(endExclusive: OffsetTime): Long {
        val until = until(endExclusive, ChronoUnit.MILLIS)
        return if (until < 0) {
            ChronoUnit.DAYS.duration.toMillis() + until
        } else {
            until
        }
    }

    private fun stopTimer() {
        darkModeTimer?.cancel()
        darkModeTimer = null
    }

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

    private abstract class OneOffCountDownTimer(millisInFuture: Long) : CountDownTimer(millisInFuture, millisInFuture) {
        final override fun onTick(millisUntilFinished: Long) {}
    }

    companion object {

        /**
         * Construct an [AutoNightDelegate] that updates the night mode at the default times of 7am and 10pm.
         */
        @JvmStatic fun fallback(): AutoNightDelegate {
            val currentZoneOffset = ZoneId.systemDefault().rules.getOffset(Instant.now())
            return ofTimes(
                dawn = OffsetTime.of(7, 0, 0, 0, currentZoneOffset),
                dusk = OffsetTime.of(22, 0, 0, 0, currentZoneOffset)
            )
        }

        /**
         * Construct an [AutoNightDelegate] that updates the night mode at the given [dawn] and [dusk] times.
         */
        @JvmStatic fun ofTimes(dawn: OffsetTime, dusk: OffsetTime): AutoNightDelegate {
            val dummySkylightDay = SkylightDay.NeverDaytime(LocalDate.now(), dawn, dusk)
            val skylight = DummySkylight(dummySkylightDay).forCoordinates(Coordinates(0.0, 0.0))
            return AutoNightDelegate(skylight)
        }

        /**
         * Construct an [AutoNightDelegate] that updates the night mode at dawn and dusk at the given [latitude]
         * and [longitude].
         */
        @JvmStatic fun ofCoordinates(latitude: Double, longitude: Double): AutoNightDelegate {
            val skylight = CalculatorSkylight().forCoordinates(Coordinates(latitude, longitude))
            return AutoNightDelegate(skylight)
        }
    }
}
