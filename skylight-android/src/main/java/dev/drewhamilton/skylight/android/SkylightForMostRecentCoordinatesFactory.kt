package dev.drewhamilton.skylight.android

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.core.content.PermissionChecker
import dev.drewhamilton.skylight.Coordinates
import dev.drewhamilton.skylight.Skylight
import dev.drewhamilton.skylight.SkylightForCoordinates
import dev.drewhamilton.skylight.calculator.CalculatorSkylight
import dev.drewhamilton.skylight.fake.FakeSkylight
import dev.drewhamilton.skylight.forCoordinates
import java.time.LocalTime
import java.time.ZoneId

/**
 * An implementation of [SkylightForCoordinatesFactory] which uses the most recent known location if available, or a
 * location-agnostic fallback otherwise. This implementation does not query the device for its current location.
 */
class SkylightForMostRecentCoordinatesFactory @JvmOverloads internal constructor(
    private val isPermissionGranted: Context.(permission: String) -> Boolean,
    private val preferredSkylight: Skylight = CalculatorSkylight(),
    private val fallbackSkylight: FakeSkylight = FakeSkylight.Typical(
        zone = ZoneId.systemDefault(),
        dawn = LocalTime.of(7, 0),
        sunrise = LocalTime.of(8, 0),
        sunset = LocalTime.of(21, 0),
        dusk = LocalTime.of(22, 0),
    ),
) : SkylightForCoordinatesFactory {

    /**
     * Instantiate a [SkylightForMostRecentCoordinatesFactory] which creates instances of [SkylightForCoordinates] using
     * [preferredSkylight] when a location can be determined and using [fallbackSkylight] when a location cannot be
     * determined.
     */
    @JvmOverloads constructor(
        preferredSkylight: Skylight = CalculatorSkylight(),
        fallbackSkylight: FakeSkylight = FakeSkylight.Typical(
            zone = ZoneId.systemDefault(),
            dawn = LocalTime.of(7, 0),
            sunrise = LocalTime.of(8, 0),
            sunset = LocalTime.of(21, 0),
            dusk = LocalTime.of(22, 0),
        ),
    ) : this(
        isPermissionGranted = { permission ->
            val result = PermissionChecker.checkSelfPermission(this, permission)
            result == PermissionChecker.PERMISSION_GRANTED
        },
        preferredSkylight = preferredSkylight,
        fallbackSkylight = fallbackSkylight,
    )

    /**
     * Create a [SkylightForCoordinates] using the most recent location available via [context].
     *
     * If [context] does not have location permissions, the fallback [FakeSkylight] is used because it is
     * location-agnostic.
     */
    @SuppressLint("MissingPermission") // Location permissions are explicitly checked
    override fun createForLocation(context: Context): SkylightForCoordinates {
        val hasCoarseLocationPermission = context.hasCoarseLocationPermission
        val hasFineLocationPermission = context.hasFineLocationPermission

        if (hasCoarseLocationPermission || hasFineLocationPermission) {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
            val coarseLocation = if (hasCoarseLocationPermission) {
                locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            } else null
            val fineLocation = if (hasFineLocationPermission) {
                locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            } else null

            val mostRecentLocation = if (coarseLocation != null && fineLocation != null) {
                if (coarseLocation.time > fineLocation.time)
                    coarseLocation
                else
                    fineLocation
            } else
                coarseLocation ?: fineLocation

            if (mostRecentLocation != null)
                return preferredSkylight.forLocation(mostRecentLocation)
        }

        return fallbackSkylight.forCoordinates(Coordinates(0.0, 0.0))
    }

    private fun Skylight.forLocation(location: Location) =
        forCoordinates(Coordinates(location.latitude, location.longitude))

    private val Context.hasCoarseLocationPermission
        get() = isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)

    private val Context.hasFineLocationPermission
        get() = isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)
}
