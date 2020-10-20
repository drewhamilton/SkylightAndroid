package dev.drewhamilton.skylight.android

import android.os.CountDownTimer
import androidx.annotation.CallSuper
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import dev.drewhamilton.skylight.SkylightDay
import dev.drewhamilton.skylight.SkylightForCoordinates
import dev.drewhamilton.skylight.isDark
import java.time.Instant
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A LifecycleObserver that always updates the current night mode setting via [darkModeApplicator] in [onStart], and
 * may implement additional lifecycle-based behaviors.
 */
sealed class DarkModeLifecycleObserver(
    private val darkModeApplicator: DarkModeApplicator
) : DefaultLifecycleObserver {

    private var job: Job? = null

    /**
     * Determines the [DarkModeApplicator.DarkMode] at the current moment.
     */
    protected abstract suspend fun currentDarkMode(): DarkModeApplicator.DarkMode

    protected fun CoroutineScope.applyCurrentDarkMode() {
        job = launch {
            val currentDarkMode = withContext(Dispatchers.IO) {
                currentDarkMode()
            }
            darkModeApplicator.applyMode(currentDarkMode)
        }
    }

    /**
     * Applies the [currentDarkMode] when the [owner] starts. May be overridden to add additional behaviors.
     */
    @CallSuper
    override fun onStart(owner: LifecycleOwner) {
        owner.lifecycleScope.applyCurrentDarkMode()
    }

    /**
     * If applying the [currentDarkMode] is still in progress, stops this job. May be overridden to add additional
     * behaviors.
     */
    @CallSuper
    override fun onStop(owner: LifecycleOwner) {
        job?.cancel()
        job = null
        super.onStop(owner)
    }

    /**
     * A [DarkModeLifecycleObserver] that updates the night mode based on when the given [skylight] resolves light and
     * dark times.
     */
    class OfSkylightForCoordinates(
        darkModeApplicator: DarkModeApplicator,
        private val skylight: SkylightForCoordinates,
    ) : DarkModeLifecycleObserver(darkModeApplicator) {

        /*
         * Counts down to a night mode change while the LifecycleOwner is started
         */
        private var nightModeTimer: CountDownTimer? = null

        /**
         * Updates the current night mode setting and watches for the next dawn/dusk event when the [owner] starts.
         */
        override fun onStart(owner: LifecycleOwner) {
            super.onStart(owner)
            owner.lifecycleScope.launch { startTimer(this) }
        }

        /**
         * Stops watching for the next dawn/dusk event when the [owner] stops.
         */
        override fun onStop(owner: LifecycleOwner) {
            super.onStop(owner)
            stopTimer()
        }

        /**
         * Resolves to [DarkModeApplicator.DarkMode.DARK] if the [SkylightForCoordinates] is dark right now, else
         * resolves to [DarkModeApplicator.DarkMode.LIGHT].
         */
        override suspend fun currentDarkMode(): DarkModeApplicator.DarkMode = if (skylight.isDark(ZonedDateTime.now()))
            DarkModeApplicator.DarkMode.DARK
        else
            DarkModeApplicator.DarkMode.LIGHT

        private suspend fun startTimer(scope: CoroutineScope) {
            val today = LocalDate.now()
            var nextEvent = withContext(Dispatchers.IO) {
                skylight.getSkylightDay(today).nextEvent
            }

            // If there is no dawn/dusk event later today, try tomorrow:
            if (nextEvent == null) {
                nextEvent = withContext(Dispatchers.IO) {
                    skylight.getSkylightDay(today.plusDays(1)).nextEvent
                }
            }

            if (nextEvent == null) {
                // No dawn/dusk events any time soon; nothing to do
                stopTimer()
            } else {
                // Count down to the next dawn/dusk event
                val timerDuration = ChronoUnit.MILLIS.between(Instant.now(), nextEvent) + 1000
                nightModeTimer = OneOffCountDownTimer(timerDuration) {
                    // A dawn/dusk event has happened; update night mode again
                    scope.applyCurrentDarkMode()
                    // And then start counting down to the next event again, recursion-style
                    scope.launch { startTimer(this) }
                }.also {
                    it.start()
                }
            }
        }

        private fun stopTimer() {
            nightModeTimer?.cancel()
            nightModeTimer = null
        }

        private val SkylightDay.nextEvent: Instant?
            get() {
                val now = Instant.now()
                return when (this) {
                    is SkylightDay.Typical -> when {
                        dawn?.isAfter(now) == true -> dawn
                        dusk?.isAfter(now) == true -> dusk
                        else -> null
                    }
                    else -> null
                }
            }

        private class OneOffCountDownTimer(
            durationMillis: Long,
            private val onFinish: () -> Unit
        ) : CountDownTimer(durationMillis, durationMillis) {
            override fun onTick(millisUntilFinished: Long) = Unit
            override fun onFinish() = onFinish.invoke()
        }
    }

    /**
     * A [DarkModeLifecycleObserver] that simply applies [mode] in [onStart].
     */
    class Constant(
        darkModeApplicator: DarkModeApplicator,
        private val mode: DarkModeApplicator.DarkMode
    ) : DarkModeLifecycleObserver(darkModeApplicator) {
        /**
         * Always resolves to the [DarkModeApplicator.DarkMode] passed to the constructor.
         */
        override suspend fun currentDarkMode(): DarkModeApplicator.DarkMode = mode
    }
}
