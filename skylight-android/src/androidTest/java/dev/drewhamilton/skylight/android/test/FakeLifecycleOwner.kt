package dev.drewhamilton.skylight.android.test

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

class FakeLifecycleOwner: LifecycleOwner {

    private val lifecycle = LifecycleRegistry.createUnsafe(this)

    override fun getLifecycle(): Lifecycle = lifecycle
}
