package dev.drewhamilton.skylight.android

import android.content.Context
import dev.drewhamilton.skylight.SkylightForCoordinates

/**
 * A factory for creating [SkylightForCoordinates] instances on Android.
 */
interface SkylightForCoordinatesFactory {

    /**
     * Create a [SkylightForCoordinates] based on the location derived from the given [context].
     */
    fun createForLocation(context: Context): SkylightForCoordinates
}
