package dev.drewhamilton.skylight.android

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import dev.drewhamilton.android.test.CustomActions
import dev.drewhamilton.android.test.UiTest
import java.util.concurrent.TimeUnit
import org.junit.Test

class AutoNightActivityTest : UiTest<TestAutoNightActivity>(TestAutoNightActivity::class.java) {

    // The smallest number of milliseconds needed for test devices to reliably apply the night theme on launch
    private val shortDelayMillis = 200

    @Test fun createdAtDay_launchesInDayMode() {
        val now = System.currentTimeMillis()
        launchActivity(testIntent(dawnMilli = now - 100_000, duskMilli = now + 100_000))

        Espresso.onView(ViewMatchers.withText("Day")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test fun createdAtNight_launchesInNightMode() {
        val now = System.currentTimeMillis()
        launchActivity(testIntent(dawnMilli = now + 100_000, duskMilli = now + 200_000))

        Espresso.onView(ViewMatchers.withText("Night")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test fun reachesDawn_changesToDayMode() {
        val now = System.currentTimeMillis()
        launchActivity(testIntent(dawnMilli = now + 500, duskMilli = now + 100_000))

        CustomActions.waitForUiThread(shortDelayMillis, TimeUnit.MILLISECONDS)
        Espresso.onView(ViewMatchers.withText("Night")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        CustomActions.waitForUiThread(1500, TimeUnit.MILLISECONDS)
        Espresso.onView(ViewMatchers.withText("Day")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test fun reachesDusk_changesToNightMode() {
        val now = System.currentTimeMillis()
        launchActivity(testIntent(dawnMilli = now - 500, duskMilli = now + 500))

        Espresso.onView(ViewMatchers.withText("Day")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        CustomActions.waitForUiThread(1500, TimeUnit.MILLISECONDS)
        Espresso.onView(ViewMatchers.withText("Night")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    private fun testIntent(dawnMilli: Long, duskMilli: Long) =  Intent().apply {
        putExtra(TestAutoNightActivity.KEY_DAWN_EPOCH_MILLI, dawnMilli)
        putExtra(TestAutoNightActivity.KEY_DUSK_EPOCH_MILLI, duskMilli)
    }
}
