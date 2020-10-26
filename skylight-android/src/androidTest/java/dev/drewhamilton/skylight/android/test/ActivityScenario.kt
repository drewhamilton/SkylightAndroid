package dev.drewhamilton.skylight.android.test

import android.app.Activity
import androidx.test.core.app.ActivityScenario

inline fun <reified A : Activity> launchActivity(): ActivityScenario<A> = ActivityScenario.launch(A::class.java)
