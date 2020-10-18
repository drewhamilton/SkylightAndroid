package dev.drewhamilton.skylight.android

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import java.util.concurrent.TimeUnit
import org.hamcrest.Matchers

fun waitForUiThread() = InstrumentationRegistry.getInstrumentation().waitForIdleSync()

fun waitForUiThread(time: Int, unit: TimeUnit): ViewInteraction =
    Espresso.onView(ViewMatchers.isRoot()).perform(loopMainThread(time, unit))

private fun loopMainThread(time: Int, unit: TimeUnit) = object : ViewAction {
    override fun getConstraints() = Matchers.any(View::class.java)

    override fun getDescription() = "loop main thread for ${toMilliseconds(time, unit)} milliseconds"

    override fun perform(uiController: UiController, view: View) =
        uiController.loopMainThreadForAtLeast(toMilliseconds(time, unit))

    private fun toMilliseconds(time: Int, unit: TimeUnit) = TimeUnit.MILLISECONDS.convert(time.toLong(), unit)
}
