package dev.drewhamilton.skylight.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import dev.drewhamilton.skylight.android.test.R
import dev.drewhamilton.skylight.android.test.launchActivity
import org.junit.Test

class AppCompatDelegateDarkModeApplicatorTest {

    @Test fun globalScope_applyDarkMode_displaysNightLayout() {
        val scenario = launchActivity<TestActivity>()

        scenario.onActivity {
            val applicator = AppCompatDelegateDarkModeApplicator(it.delegate)
            applicator.applyMode(DarkModeApplicator.DarkMode.DARK)
        }

        onView(withText("Day")).check(doesNotExist())
        onView(withText("Night")).check(matches(isDisplayed()))
    }

    @Test fun globalScope_applyLightMode_displaysNotNightLayout() {
        val scenario = launchActivity<TestActivity>()

        scenario.onActivity {
            val applicator = AppCompatDelegateDarkModeApplicator(it.delegate)
            applicator.applyMode(DarkModeApplicator.DarkMode.LIGHT)
        }

        onView(withText("Day")).check(matches(isDisplayed()))
        onView(withText("Night")).check(doesNotExist())
    }

    @Test fun localScope_applyDarkMode_displaysNightLayout() {
        val scenario = launchActivity<TestActivity>()

        scenario.onActivity {
            val applicator = AppCompatDelegateDarkModeApplicator(
                it.delegate,
                AppCompatDelegateDarkModeApplicator.Scope.LOCAL
            )
            applicator.applyMode(DarkModeApplicator.DarkMode.DARK)
        }

        onView(withText("Day")).check(doesNotExist())
        onView(withText("Night")).check(matches(isDisplayed()))
    }

    @Test fun localScope_applyLightMode_displaysNotNightLayout() {
        val scenario = launchActivity<TestActivity>()

        scenario.onActivity {
            val applicator = AppCompatDelegateDarkModeApplicator(
                it.delegate,
                AppCompatDelegateDarkModeApplicator.Scope.LOCAL
            )
            applicator.applyMode(DarkModeApplicator.DarkMode.LIGHT)
        }

        onView(withText("Day")).check(matches(isDisplayed()))
        onView(withText("Night")).check(doesNotExist())
    }

    class TestActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.test_text)
        }
    }
}
