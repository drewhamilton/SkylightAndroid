package dev.drewhamilton.android.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.test.espresso.intent.rule.IntentsTestRule
import org.junit.Assert
import org.junit.Rule

abstract class UiTest<A : AppCompatActivity>(activityClass: Class<A> ) {

    @get:Rule
    val testRule = IntentsTestRule(activityClass, true, false)

    protected val activity get(): A = testRule.activity

    protected fun launchActivity(intent: Intent = Intent()) {
        testRule.launchActivity(intent)
        onActivityLaunched()
    }

    protected open fun onActivityLaunched() {}

    protected fun runOnUiThread(runnable: () -> Unit) =
        try {
            testRule.runOnUiThread(runnable)
        } catch (exception: Exception) {
            Assert.fail("${exception.javaClass.simpleName} while running on UI thread: ${exception.message}")
        }

    protected fun simulateConfigurationChange() {
        runOnUiThread { activity.recreate() }
        CustomActions.waitForUiThread()
    }
}