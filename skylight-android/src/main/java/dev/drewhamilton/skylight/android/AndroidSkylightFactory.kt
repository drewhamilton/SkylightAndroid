package dev.drewhamilton.skylight.android

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.core.content.PermissionChecker
import dev.drewhamilton.skylight.Coordinates
import dev.drewhamilton.skylight.SkylightForCoordinates
import dev.drewhamilton.skylight.calculator.CalculatorSkylight
import dev.drewhamilton.skylight.fake.FakeSkylight
import dev.drewhamilton.skylight.forCoordinates
import java.time.LocalTime
import java.time.ZoneId

object AndroidSkylightFactory {

    /**
     * Create a [SkylightForCoordinates] using the most recent location available via [context].
     *
     * If [context] does not have location permissions, a dummy [SkylightForCoordinates] is created that assumes dawn at
     * 7am and dusk at 10pm in the device's current time zone.
     */
    @SuppressLint("MissingPermission") // Location permissions are explicitly checked
    @JvmStatic fun createForLocation(context: Context): SkylightForCoordinates {
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

            return if (mostRecentLocation == null)
                createFake()
            else
                createForLocation(mostRecentLocation)
        } else {
            return createFake()
        }
    }

    /**
     * Create a [SkylightForCoordinates] using the given [location].
     */
    @JvmStatic fun createForLocation(location: Location) =
        createForCoordinates(Coordinates(location.latitude, location.longitude))

    @JvmStatic private fun createForCoordinates(coordinates: Coordinates): SkylightForCoordinates =
        CalculatorSkylight().forCoordinates(coordinates)

    @JvmStatic private fun createFake(): SkylightForCoordinates = FakeSkylight.Typical(
        zone = ZoneId.systemDefault(),
        dawn = LocalTime.of(7, 0),
        sunrise = LocalTime.of(8, 0),
        sunset = LocalTime.of(21, 0),
        dusk = LocalTime.of(22, 0),
    ).forCoordinates(Coordinates(0.0, 0.0))

    @JvmStatic private val Context.hasCoarseLocationPermission
        get() = isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)

    @JvmStatic private val Context.hasFineLocationPermission
        get() = isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)

    @JvmStatic private fun Context.isPermissionGranted(permission: String): Boolean {
        val result = PermissionChecker.checkSelfPermission(this, permission)
        return result == PermissionChecker.PERMISSION_GRANTED
    }
}
