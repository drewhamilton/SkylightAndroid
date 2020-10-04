package dev.drewhamilton.skylight.android;

import android.Manifest;
import android.content.Context;
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
import kotlin.jvm.functions.Function2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SkylightForMostRecentCoordinatesFactoryTest {

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
        SkylightForMostRecentCoordinatesFactory factory = new SkylightForMostRecentCoordinatesFactory(
                (Function2<Context, String, Boolean>) (context, permission) ->
                        permission.equals(Manifest.permission.ACCESS_COARSE_LOCATION)
                                || permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)
        );

        Location amsterdam = amsterdam();
        when(amsterdam.getTime()).thenReturn(1000L);
        when(mockLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)).thenReturn(amsterdam);
        Location bogus = mock(Location.class);
        when(bogus.getTime()).thenReturn(0L);
        when(mockLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)).thenReturn(bogus);

        SkylightForCoordinates skylight = factory.createForLocation(mockContext);
        SkylightDay february2 = skylight.getSkylightDay(february2());

        assertAmsterdamFebruary2(february2);
    }


    @Test
    public void createForLocationWithContext_bothPermissionsGpsRecent_calculatesByCoordinatesOfGpsLocation() {
        SkylightForMostRecentCoordinatesFactory factory = new SkylightForMostRecentCoordinatesFactory(
                (Function2<Context, String, Boolean>) (context, permission) ->
                        permission.equals(Manifest.permission.ACCESS_COARSE_LOCATION)
                                || permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)
        );

        Location amsterdam = amsterdam();
        when(amsterdam.getTime()).thenReturn(1000L);
        when(mockLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)).thenReturn(amsterdam);
        Location bogus = mock(Location.class);
        when(bogus.getTime()).thenReturn(0L);
        when(mockLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)).thenReturn(bogus);

        SkylightForCoordinates skylight = factory.createForLocation(mockContext);
        SkylightDay february2 = skylight.getSkylightDay(february2());

        assertAmsterdamFebruary2(february2);
    }

    @Test
    public void createForLocationWithContext_coarsePermissionOnly_calculatesByCoordinatesOfNetworkLocation() {
        SkylightForMostRecentCoordinatesFactory factory = new SkylightForMostRecentCoordinatesFactory(
                (Function2<Context, String, Boolean>) (context, permission) ->
                        permission.equals(Manifest.permission.ACCESS_COARSE_LOCATION)
        );

        Location amsterdam = amsterdam();
        when(mockLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)).thenReturn(amsterdam);

        SkylightForCoordinates skylight = factory.createForLocation(mockContext);
        SkylightDay february2 = skylight.getSkylightDay(february2());

        assertAmsterdamFebruary2(february2);
    }

    @Test
    public void createForLocationWithContext_finePermissionOnly_calculatesByCoordinatesOfGpsLocation() {
        SkylightForMostRecentCoordinatesFactory factory = new SkylightForMostRecentCoordinatesFactory(
                (Function2<Context, String, Boolean>) (context, permission) ->
                        permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)
        );

        Location amsterdam = amsterdam();
        when(mockLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)).thenReturn(amsterdam);

        SkylightForCoordinates skylight = factory.createForLocation(mockContext);
        SkylightDay february2 = skylight.getSkylightDay(february2());

        assertAmsterdamFebruary2(february2);
    }

    @Test
    public void createForLocationWithContext_permissionButNoLocation_calculatesByDummy() {
        SkylightForMostRecentCoordinatesFactory factory = new SkylightForMostRecentCoordinatesFactory(
                (Function2<Context, String, Boolean>) (context, permission) ->
                        permission.equals(Manifest.permission.ACCESS_COARSE_LOCATION)
        );

        SkylightForCoordinates skylight = factory.createForLocation(mockContext);
        SkylightDay february2 = skylight.getSkylightDay(february2());

        assertFake(february2);
    }

    @Test
    public void createForLocationWithContext_noPermission_calculatesByDummy() {
        SkylightForMostRecentCoordinatesFactory factory = new SkylightForMostRecentCoordinatesFactory(
                (Function2<Context, String, Boolean>) (context, permission) -> false
        );

        SkylightForCoordinates skylight = factory.createForLocation(mockContext);
        SkylightDay february2 = skylight.getSkylightDay(february2());

        assertFake(february2);
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
