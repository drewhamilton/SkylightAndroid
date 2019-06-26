package drewhamilton.skylight.android.theme

import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatDelegate
import drewhamilton.skylight.backport.SkylightDay
import drewhamilton.skylight.backport.SkylightForCoordinates
import drewhamilton.skylight.backport.isDark
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetTime
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoUnit

class AutoNightDelegate(
    private val skylight: SkylightForCoordinates
) {

    private var darkModeTimer: CountDownTimer? = null

    fun onActivityStart() {
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

    fun onActivityStop() {
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
