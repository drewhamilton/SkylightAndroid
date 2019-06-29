package drewhamilton.skylight.android.nightmode

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.jakewharton.threetenabp.AndroidThreeTen
import drewhamilton.android.test.CustomActions
import drewhamilton.android.test.UiTest
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

class AutoNightActivityTest : UiTest<TestAutoNightActivity>(TestAutoNightActivity::class.java) {

    @Before fun initializeThreeTenAbp() =
        AndroidThreeTen.init(InstrumentationRegistry.getInstrumentation().targetContext)

    @Test fun createdAtDay_launchesInDayMode() {
        val now = System.currentTimeMillis()
        launchActivity(testIntent(now - 100_000, now + 100_000))

        CustomActions.waitForUiThread(1, TimeUnit.SECONDS)
        onView(withText("Day")).check(matches(isDisplayed()))
    }

    @Test fun createdAtNight_launchesInNightMode() {
        val now = System.currentTimeMillis()
        launchActivity(testIntent(now + 100_000, now + 200_000))

        onView(withText("Night")).check(matches(isDisplayed()))
    }

    private fun testIntent(dawnMilli: Long, duskMilli: Long) =  Intent().apply {
        putExtra(TestAutoNightActivity.KEY_DAWN_EPOCH_MILLI, dawnMilli)
        putExtra(TestAutoNightActivity.KEY_DUSK_EPOCH_MILLI, duskMilli)
    }
}
