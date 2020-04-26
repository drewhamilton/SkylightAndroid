package drewhamilton.skylight.android.nightmode

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import drewhamilton.android.test.CustomActions
import drewhamilton.android.test.UiTest
import org.junit.Ignore
import org.junit.Test
import java.util.concurrent.TimeUnit

class AutoNightActivityTest : UiTest<TestAutoNightActivity>(TestAutoNightActivity::class.java) {

    // The smallest number of milliseconds needed for test devices to reliably apply the night theme on launch
    private val shortDelayMillis = 200

    @Test fun createdAtDay_launchesInDayMode() {
        val now = System.currentTimeMillis()
        launchActivity(testIntent(dawnMilli = now - 100_000, duskMilli = now + 100_000))

        onView(withText("Day")).check(matches(isDisplayed()))
    }

    @Test fun createdAtNight_launchesInNightMode() {
        val now = System.currentTimeMillis()
        launchActivity(testIntent(dawnMilli = now + 100_000, duskMilli = now + 200_000))

        onView(withText("Night")).check(matches(isDisplayed()))
    }

    @Ignore("Azure doesn't like this test for some reason")
    @Test fun reachesDawn_changesToDayMode() {
        val now = System.currentTimeMillis()
        launchActivity(testIntent(dawnMilli = now + 500, duskMilli = now + 100_000))

        CustomActions.waitForUiThread(shortDelayMillis, TimeUnit.MILLISECONDS)
        onView(withText("Night")).check(matches(isDisplayed()))
        CustomActions.waitForUiThread(1500, TimeUnit.MILLISECONDS)
        onView(withText("Day")).check(matches(isDisplayed()))
    }

    @Ignore("Azure doesn't like this test for some reason")
    @Test fun reachesDusk_changesToNightMode() {
        val now = System.currentTimeMillis()
        launchActivity(testIntent(dawnMilli = now - 500, duskMilli = now + 500))

        onView(withText("Day")).check(matches(isDisplayed()))
        CustomActions.waitForUiThread(1500, TimeUnit.MILLISECONDS)
        onView(withText("Night")).check(matches(isDisplayed()))
    }

    private fun testIntent(dawnMilli: Long, duskMilli: Long) =  Intent().apply {
        putExtra(TestAutoNightActivity.KEY_DAWN_EPOCH_MILLI, dawnMilli)
        putExtra(TestAutoNightActivity.KEY_DUSK_EPOCH_MILLI, duskMilli)
    }
}
