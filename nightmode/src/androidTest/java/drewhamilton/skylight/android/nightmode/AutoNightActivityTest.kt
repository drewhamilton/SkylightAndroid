package drewhamilton.skylight.android.nightmode

import android.content.Intent
import android.util.Log
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.jakewharton.threetenabp.AndroidThreeTen
import drewhamilton.android.test.CustomActions
import drewhamilton.android.test.UiTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.threeten.bp.ZoneId
import java.util.concurrent.TimeUnit

class AutoNightActivityTest : UiTest<TestAutoNightActivity>(TestAutoNightActivity::class.java) {

    @Before fun initializeThreeTenAbp() =
        AndroidThreeTen.init(InstrumentationRegistry.getInstrumentation().targetContext)

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

    @Test fun reachesDawn_changesToDayMode() {
        val now = System.currentTimeMillis()
        launchActivity(testIntent(dawnMilli = now + 500, duskMilli = now + 100_000))

        val actualText = activity.findViewById<TextView>(R.id.testText).text
        assertEquals(
            "(Text was $actualText) System time zone: ${ZoneId.systemDefault()}",
            "Night", actualText
        )
        onView(withText("Night")).check(matches(isDisplayed()))
        CustomActions.waitForUiThread(1500, TimeUnit.MILLISECONDS)
        onView(withText("Day")).check(matches(isDisplayed()))
    }

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
