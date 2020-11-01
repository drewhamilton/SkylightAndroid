package dev.drewhamilton.skylight.android

import com.google.common.truth.Truth.assertThat
import dev.drewhamilton.skylight.Coordinates
import dev.drewhamilton.skylight.Skylight
import dev.drewhamilton.skylight.SkylightDay
import dev.drewhamilton.skylight.android.test.FakeDarkModeApplicator
import dev.drewhamilton.skylight.android.test.FakeLifecycleOwner
import dev.drewhamilton.skylight.forCoordinates
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DarkModeLifecycleObserverTest {

    private val testDelay = Duration.ofSeconds(3000)

    private val testDispatcher = TestCoroutineDispatcher()

    @Before fun setTestMainDispatcher() = Dispatchers.setMain(testDispatcher)

    @After fun resetMainDispatcher() = Dispatchers.resetMain()

    @Test fun constant_onStart_appliesConstantValue() {
        val applicator = FakeDarkModeApplicator()
        val owner = FakeLifecycleOwner()

        DarkModeApplicator.DarkMode.values().forEach { darkMode ->
            val observer = DarkModeLifecycleObserver.Constant(darkMode, applicator)
            observer.onStart(owner)

            assertThat(applicator.appliedMode).isEqualTo(darkMode)
        }
    }

    @Test fun ofSkylightForCoordinates_onStart_appliesCurrentValue() {
        val skylight = LightDarkFakeSkylight(testDelay).forCoordinates(Coordinates(0.0, 0.0))
        val applicator = FakeDarkModeApplicator()
        val owner = FakeLifecycleOwner()

        val observer = DarkModeLifecycleObserver.OfSkylightForCoordinates(skylight, applicator, testDispatcher)
        observer.onStart(owner)

        assertThat(applicator.appliedMode).isEqualTo(DarkModeApplicator.DarkMode.DARK)
    }

    @Test fun ofSkylightForCoordinates_atEachNextEvent_appliesNextMode() = runBlockingTest {
        val skylight = LightDarkFakeSkylight(testDelay)
        val applicator = FakeDarkModeApplicator()
        val owner = FakeLifecycleOwner()

        val observer = DarkModeLifecycleObserver.OfSkylightForCoordinates(
            skylight.forCoordinates(Coordinates(0.0, 0.0)),
            applicator,
            testDispatcher
        )
        observer.onStart(owner)

        skylight.isLight = true
        testDispatcher.advanceTimeBy(testDelay.toMillis() + 1000)
        assertThat(applicator.appliedMode).isEqualTo(DarkModeApplicator.DarkMode.LIGHT)

        skylight.isLight = false
        testDispatcher.advanceTimeBy(testDelay.toMillis() + 1000)
        assertThat(applicator.appliedMode).isEqualTo(DarkModeApplicator.DarkMode.DARK)
    }

    private class LightDarkFakeSkylight(
        private val fakeEventDelay: Duration,
        var isLight: Boolean = false,
    ) : Skylight {
        override fun getSkylightDay(coordinates: Coordinates, date: LocalDate): SkylightDay = if (isLight)
            SkylightDay.Typical(
                date,
                dawn = Instant.now().minusSeconds(fakeEventDelay.seconds),
                dusk = Instant.now().plusSeconds(fakeEventDelay.seconds),
            )
        else
            SkylightDay.Typical(
                date,
                dawn = Instant.now().plusSeconds(fakeEventDelay.seconds),
                dusk = Instant.now().plusSeconds(2 * fakeEventDelay.seconds),
            )
    }
}
