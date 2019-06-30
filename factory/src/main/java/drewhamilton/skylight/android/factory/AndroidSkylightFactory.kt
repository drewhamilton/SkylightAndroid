package drewhamilton.skylight.android.factory

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.core.content.PermissionChecker
import drewhamilton.skylight.backport.Coordinates
import drewhamilton.skylight.backport.SkylightDay
import drewhamilton.skylight.backport.SkylightForCoordinates
import drewhamilton.skylight.backport.calculator.CalculatorSkylight
import drewhamilton.skylight.backport.dummy.DummySkylight
import drewhamilton.skylight.backport.forCoordinates
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetTime
import org.threeten.bp.ZoneId

object AndroidSkylightFactory {

    /**
     * Create a [SkylightForCoordinates] using the most recent location available via [context].
     *
     * If [context] does not have location permissions, a dummy [SkylightForCoordinates] is created that assumes dawn at
     * 7am and dusk at 10pm.
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
                createDummy()
            else
                createForLocation(mostRecentLocation)
        } else {
            return createDummy()
        }
    }

    /**
     * Create a [SkylightForCoordinates] using the given [location].
     */
    @JvmStatic fun createForLocation(location: Location) =
        createForCoordinates(Coordinates(location.latitude, location.longitude))

    @JvmStatic private fun createForCoordinates(coordinates: Coordinates) : SkylightForCoordinates =
        CalculatorSkylight().forCoordinates(coordinates)

    @JvmStatic private fun createDummy(): SkylightForCoordinates {
        val currentZoneOffset = ZoneId.systemDefault().rules.getOffset(Instant.now())
        val dummySkylightDay = SkylightDay.NeverDaytime(
            LocalDate.now(),
            dawn = OffsetTime.of(7, 0, 0, 0, currentZoneOffset),
            dusk = OffsetTime.of(22, 0, 0, 0, currentZoneOffset)
        )
        return DummySkylight(dummySkylightDay).forCoordinates(Coordinates(0.0, 0.0))
    }

    @JvmStatic private val Context.hasCoarseLocationPermission get() =
        isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)

    @JvmStatic private val Context.hasFineLocationPermission get() =
        isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)

    @JvmStatic private fun Context.isPermissionGranted(permission: String): Boolean {
        val result = PermissionChecker.checkSelfPermission(this, permission)
        return result == PermissionChecker.PERMISSION_GRANTED
    }
}
