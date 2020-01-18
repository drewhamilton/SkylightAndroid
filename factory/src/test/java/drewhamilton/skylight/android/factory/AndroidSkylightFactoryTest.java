package drewhamilton.skylight.android.factory;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import drewhamilton.skylight.SkylightDay;
import drewhamilton.skylight.SkylightForCoordinates;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AndroidSkylightFactoryTest {

    @Mock private Context mockContext;
    @Mock private LocationManager mockLocationManager;

    @Before
    public void mockContext() {
        when(mockContext.getSystemService(Context.LOCATION_SERVICE)).thenReturn(mockLocationManager);
    }

    @Test
    public void createForLocationWithContext_bothPermissionsNetworkRecent_calculatesByCoordinatesOfNetworkLocation() {
        mockPermission(mockContext, Manifest.permission.ACCESS_COARSE_LOCATION, true);
        mockPermission(mockContext, Manifest.permission.ACCESS_FINE_LOCATION, true);

        Location amsterdam = amsterdam();
        when(amsterdam.getTime()).thenReturn(1000L);
        when(mockLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)).thenReturn(amsterdam);
        Location bogus = mock(Location.class);
        when(bogus.getTime()).thenReturn(0L);
        when(mockLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)).thenReturn(bogus);

        SkylightForCoordinates skylight = AndroidSkylightFactory.createForLocation(mockContext);
        SkylightDay february2 = skylight.getSkylightDay(february2());

        assertAmsterdamFebruary2(february2);
    }


    @Test
    public void createForLocationWithContext_bothPermissionsGpsRecent_calculatesByCoordinatesOfGpsLocation() {
        mockPermission(mockContext, Manifest.permission.ACCESS_COARSE_LOCATION, true);
        mockPermission(mockContext, Manifest.permission.ACCESS_FINE_LOCATION, true);

        Location amsterdam = amsterdam();
        when(amsterdam.getTime()).thenReturn(1000L);
        when(mockLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)).thenReturn(amsterdam);
        Location bogus = mock(Location.class);
        when(bogus.getTime()).thenReturn(0L);
        when(mockLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)).thenReturn(bogus);

        SkylightForCoordinates skylight = AndroidSkylightFactory.createForLocation(mockContext);
        SkylightDay february2 = skylight.getSkylightDay(february2());

        assertAmsterdamFebruary2(february2);
    }

    @Test
    public void createForLocationWithContext_coarsePermissionOnly_calculatesByCoordinatesOfNetworkLocation() {
        mockPermission(mockContext, Manifest.permission.ACCESS_COARSE_LOCATION, true);
        mockPermission(mockContext, Manifest.permission.ACCESS_FINE_LOCATION, false);

        Location amsterdam = amsterdam();
        when(mockLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)).thenReturn(amsterdam);

        SkylightForCoordinates skylight = AndroidSkylightFactory.createForLocation(mockContext);
        SkylightDay february2 = skylight.getSkylightDay(february2());

        assertAmsterdamFebruary2(february2);
    }

    @Test
    public void createForLocationWithContext_finePermissionOnly_calculatesByCoordinatesOfGpsLocation() {
        mockPermission(mockContext, Manifest.permission.ACCESS_COARSE_LOCATION, false);
        mockPermission(mockContext, Manifest.permission.ACCESS_FINE_LOCATION, true);

        Location amsterdam = amsterdam();
        when(mockLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)).thenReturn(amsterdam);

        SkylightForCoordinates skylight = AndroidSkylightFactory.createForLocation(mockContext);
        SkylightDay february2 = skylight.getSkylightDay(february2());

        assertAmsterdamFebruary2(february2);
    }

    @Test
    public void createForLocationWithContext_permissionButNoLocation_calculatesByDummy() {
        mockPermission(mockContext, Manifest.permission.ACCESS_COARSE_LOCATION, true);
        mockPermission(mockContext, Manifest.permission.ACCESS_FINE_LOCATION, false);

        SkylightForCoordinates skylight = AndroidSkylightFactory.createForLocation(mockContext);
        SkylightDay february2 = skylight.getSkylightDay(february2());

        assertDummy(february2);
    }

    @Test
    public void createForLocationWithContext_noPermission_calculatesByDummy() {
        mockPermission(mockContext, Manifest.permission.ACCESS_COARSE_LOCATION, false);
        mockPermission(mockContext, Manifest.permission.ACCESS_FINE_LOCATION, false);

        SkylightForCoordinates skylight = AndroidSkylightFactory.createForLocation(mockContext);
        SkylightDay february2 = skylight.getSkylightDay(february2());

        assertDummy(february2);
    }

    @Test
    public void createForLocationWithLocation_calculatesByCoordinates() {
        SkylightForCoordinates skylightForAmsterdam = AndroidSkylightFactory.createForLocation(amsterdam());
        SkylightDay february2 = skylightForAmsterdam.getSkylightDay(february2());

        assertAmsterdamFebruary2(february2);
    }

    private static Location amsterdam() {
        Location amsterdam = mock(Location.class);
        when(amsterdam.getLatitude()).thenReturn(52.3680);
        when(amsterdam.getLongitude()).thenReturn(4.9036);
        return amsterdam;
    }

    private static LocalDate february2() {
        return LocalDate.of(2019, Month.FEBRUARY, 2);
    }

    private static void mockPermission(Context mockContext, String permission, boolean granted) {
        int permissionResult = granted ? PackageManager.PERMISSION_GRANTED : PackageManager.PERMISSION_DENIED;
        when(mockContext.checkPermission(eq(permission), anyInt(), anyInt())).thenReturn(permissionResult);
    }

    private static void assertAmsterdamFebruary2(SkylightDay skylightDay) {
        assertEquals(february2(), skylightDay.getDate());
        assertTrue(skylightDay instanceof SkylightDay.Typical);
        SkylightDay.Typical typicalSkylightDay = (SkylightDay.Typical) skylightDay;
        assertEquals(
                ZonedDateTime.of(february2(), LocalTime.of(7, 45, 8, 841_000_000), ZoneId.systemDefault()),
                typicalSkylightDay.getDawn()
        );
        assertEquals(
                ZonedDateTime.of(february2(), LocalTime.of(8, 24, 2, 641_000_000), ZoneId.systemDefault()),
                typicalSkylightDay.getSunrise()
        );
        assertEquals(
                ZonedDateTime.of(february2(), LocalTime.of(17, 26, 33, 815_000_000), ZoneId.systemDefault()),
                typicalSkylightDay.getSunset()
        );
        assertEquals(
                ZonedDateTime.of(february2(), LocalTime.of(18, 5, 27, 615_000_000), ZoneId.systemDefault()),
                typicalSkylightDay.getDusk()
        );
    }

    private static void assertDummy(SkylightDay skylightDay) {
        assertEquals(february2(), skylightDay.getDate());
        assertTrue(skylightDay instanceof SkylightDay.Typical);
        SkylightDay.Typical castSkylightDay = (SkylightDay.Typical) skylightDay;
        ZoneId zoneId = ZoneId.systemDefault();
        assertEquals(
                ZonedDateTime.of(february2(), LocalTime.of(7, 0, 0, 0), zoneId),
                castSkylightDay.getDawn()
        );
        assertEquals(
                ZonedDateTime.of(february2(), LocalTime.of(22, 0, 0, 0), zoneId),
                castSkylightDay.getDusk()
        );
        assertNull(castSkylightDay.getSunrise());
        assertNull(castSkylightDay.getSunset());
    }
}
