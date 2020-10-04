package drewhamilton.skylight.android.factory;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import org.junit.After;
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
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import dev.drewhamilton.skylight.SkylightDay;
import dev.drewhamilton.skylight.SkylightForCoordinates;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AndroidSkylightFactoryTest {

    private static final int MILLIS_PER_HOUR = 60 * 60 * 1000;

    @Mock private Context mockContext;
    @Mock private LocationManager mockLocationManager;

    private TimeZone systemTimeZone;

    @Before
    public void mockContext() {
        when(mockContext.getSystemService(Context.LOCATION_SERVICE)).thenReturn(mockLocationManager);
    }

    @Before
    public void setSystemTimeZone() {
        systemTimeZone = TimeZone.getDefault();
        TimeZone.setDefault(new SimpleTimeZone(MILLIS_PER_HOUR, "CET"));
    }

    @After
    public void restoreSystemTimeZone() {
        TimeZone.setDefault(systemTimeZone);
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

        assertFake(february2);
    }

    @Test
    public void createForLocationWithContext_noPermission_calculatesByDummy() {
        mockPermission(mockContext, Manifest.permission.ACCESS_COARSE_LOCATION, false);
        mockPermission(mockContext, Manifest.permission.ACCESS_FINE_LOCATION, false);

        SkylightForCoordinates skylight = AndroidSkylightFactory.createForLocation(mockContext);
        SkylightDay february2 = skylight.getSkylightDay(february2());

        assertFake(february2);
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
                ZonedDateTime.of(february2(), LocalTime.of(7, 45, 5, 170_000_000), ZoneId.systemDefault()).toInstant(),
                typicalSkylightDay.getDawn()
        );
        assertEquals(
                ZonedDateTime.of(february2(), LocalTime.of(8, 23, 58, 598_000_000), ZoneId.systemDefault()).toInstant(),
                typicalSkylightDay.getSunrise()
        );
        assertEquals(
                ZonedDateTime.of(february2(), LocalTime.of(17, 26, 38, 555_000_000), ZoneId.systemDefault()).toInstant(),
                typicalSkylightDay.getSunset()
        );
        assertEquals(
                ZonedDateTime.of(february2(), LocalTime.of(18, 5, 31, 982_000_000), ZoneId.systemDefault()).toInstant(),
                typicalSkylightDay.getDusk()
        );
    }

    private static void assertFake(SkylightDay skylightDay) {
        assertEquals(february2(), skylightDay.getDate());
        assertTrue(skylightDay instanceof SkylightDay.Typical);
        SkylightDay.Typical castSkylightDay = (SkylightDay.Typical) skylightDay;
        ZoneId zoneId = ZoneId.systemDefault();
        assertEquals(
                ZonedDateTime.of(february2(), LocalTime.of(7, 0, 0, 0), zoneId).toInstant(),
                castSkylightDay.getDawn()
        );
        assertEquals(
                ZonedDateTime.of(february2(), LocalTime.of(8, 0, 0, 0), zoneId).toInstant(),
                castSkylightDay.getSunrise()
        );
        assertEquals(
                ZonedDateTime.of(february2(), LocalTime.of(21, 0, 0, 0), zoneId).toInstant(),
                castSkylightDay.getSunset()
        );
        assertEquals(
                ZonedDateTime.of(february2(), LocalTime.of(22, 0, 0, 0), zoneId).toInstant(),
                castSkylightDay.getDusk()
        );
    }
}
