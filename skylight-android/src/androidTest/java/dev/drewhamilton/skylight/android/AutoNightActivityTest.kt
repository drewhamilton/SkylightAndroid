package dev.drewhamilton.skylight.android

import android.content.Intent
import android.os.Build
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import java.util.concurrent.TimeUnit
import org.junit.Assume.assumeTrue
import org.junit.Test

class AutoNightActivityTest : UiTest<TestAutoNightActivity>(TestAutoNightActivity::class.java) {

    // The smallest number of milliseconds needed for test devices to reliably apply the night theme on launch
    private val shortDelayMillis = 200

    @Test fun createdAtDay_launchesInDayMode() {
        val now = System.currentTimeMillis()
        launchActivity(testIntent(dawnMilli = now - 100_000, duskMilli = now + 100_000))

        onView(ViewMatchers.withText("Day")).check(matches(isDisplayed()))
    }

    @Test fun createdAtNight_launchesInNightMode() {
        val now = System.currentTimeMillis()
        launchActivity(testIntent(dawnMilli = now + 100_000, duskMilli = now + 200_000))

        onView(ViewMatchers.withText("Night")).check(matches(isDisplayed()))
    }

    @Test fun reachesDawn_changesToDayMode() {
        assumeTrue("This test is flaky on API 28+", Build.VERSION.SDK_INT < 28)

        val now = System.currentTimeMillis()
        launchActivity(testIntent(dawnMilli = now + 500, duskMilli = now + 100_000))

        waitForUiThread(shortDelayMillis, TimeUnit.MILLISECONDS)
        onView(ViewMatchers.withText("Night")).check(matches(isDisplayed()))
        waitForUiThread(1500, TimeUnit.MILLISECONDS)
        onView(ViewMatchers.withText("Day")).check(matches(isDisplayed()))
    }

    @Test fun reachesDusk_changesToNightMode() {
        assumeTrue("This test is flaky on API 28+", Build.VERSION.SDK_INT < 28)

        val now = System.currentTimeMillis()
        launchActivity(testIntent(dawnMilli = now - 500, duskMilli = now + 500))

        onView(ViewMatchers.withText("Day")).check(matches(isDisplayed()))
        waitForUiThread(1500, TimeUnit.MILLISECONDS)
        onView(ViewMatchers.withText("Night")).check(matches(isDisplayed()))
    }

    private fun testIntent(dawnMilli: Long, duskMilli: Long) =  Intent().apply {
        putExtra(TestAutoNightActivity.KEY_DAWN_EPOCH_MILLI, dawnMilli)
        putExtra(TestAutoNightActivity.KEY_DUSK_EPOCH_MILLI, duskMilli)
    }
}
