package dev.drewhamilton.skylight.android

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
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A LifecycleObserver that always updates the current night mode setting via [darkModeApplicator] in [onStart], and
 * may implement additional lifecycle-based behaviors.
 */
sealed class DarkModeLifecycleObserver(
    private val darkModeApplicator: DarkModeApplicator,
) : DefaultLifecycleObserver {

    private var supervisor: Job? = null

    /**
     * Extension for launching a coroutine [block] that will be cancelled in [onStop].
     */
    protected fun CoroutineScope.launchSupervised(block: suspend CoroutineScope.() -> Unit) {
        launch(context = checkNotNull(supervisor), block = block)
    }

    /**
     * Determine the [DarkModeApplicator.DarkMode] at the current moment.
     */
    protected abstract suspend fun currentDarkMode(): DarkModeApplicator.DarkMode

    protected suspend fun applyCurrentDarkMode() {
        darkModeApplicator.applyMode(currentDarkMode())
    }

    /**
     * Applies the [currentDarkMode] when the [owner] starts. May be overridden to add additional behaviors.
     */
    @CallSuper
    override fun onStart(owner: LifecycleOwner) {
        check(supervisor == null) { "onStart was called, but <$this> is already started" }
        supervisor = SupervisorJob()

        owner.lifecycleScope.launchSupervised {
            applyCurrentDarkMode()
        }
    }

    /**
     * Stops any running coroutines, including the job to apply the [currentDarkMode].
     */
    final override fun onStop(owner: LifecycleOwner) {
        cancel()
    }

    /**
     * Stops any running coroutines, including the job to apply the [currentDarkMode].
     */
    fun cancel() {
        supervisor?.cancel()
        supervisor = null
    }

    /**
     * A [DarkModeLifecycleObserver] that updates the night mode based on when the given [skylight] resolves light and
     * dark times.
     */
    class OfSkylightForCoordinates(
        private val skylight: SkylightForCoordinates,
        darkModeApplicator: DarkModeApplicator,
        private val skylightContext: CoroutineContext = Dispatchers.IO,
    ) : DarkModeLifecycleObserver(darkModeApplicator) {

        /**
         * Updates the current night mode setting and watches for the next dawn/dusk event when the [owner] starts.
         */
        override fun onStart(owner: LifecycleOwner) {
            super.onStart(owner)
            owner.lifecycleScope.launchSupervised { startTimer() }
        }

        /**
         * Resolves to [DarkModeApplicator.DarkMode.DARK] if the [SkylightForCoordinates] is dark right now, else
         * resolves to [DarkModeApplicator.DarkMode.LIGHT].
         */
        override suspend fun currentDarkMode(): DarkModeApplicator.DarkMode = withContext(skylightContext) {
            if (skylight.isDark(ZonedDateTime.now()))
                DarkModeApplicator.DarkMode.DARK
            else
                DarkModeApplicator.DarkMode.LIGHT
        }

        private suspend fun startTimer() {
            // Loop for as long as there is a next Skylight event (`delay` call slows down the loop)
            while (true) {
                val today = LocalDate.now()
                var nextEvent = withContext(skylightContext) {
                    skylight.getSkylightDay(today).nextEvent
                }

                // If there is no dawn/dusk event later today, try tomorrow:
                if (nextEvent == null) {
                    nextEvent = withContext(skylightContext) {
                        skylight.getSkylightDay(today.plusDays(1)).nextEvent
                    }
                }

                if (nextEvent == null) {
                    // No events any time soon; nothing to do
                    return
                } else {
                    // Count down to the next dawn/dusk event
                    val timerDuration = ChronoUnit.MILLIS.between(Instant.now(), nextEvent) + 1000
                    delay(timerDuration)

                    applyCurrentDarkMode()
                }
            }
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
    }

    /**
     * A [DarkModeLifecycleObserver] that simply applies [mode] in [onStart].
     */
    class Constant(
        private val mode: DarkModeApplicator.DarkMode,
        darkModeApplicator: DarkModeApplicator,
    ) : DarkModeLifecycleObserver(darkModeApplicator) {
        /**
         * Always resolves to the [DarkModeApplicator.DarkMode] passed to the constructor.
         */
        override suspend fun currentDarkMode(): DarkModeApplicator.DarkMode = mode
    }
}
