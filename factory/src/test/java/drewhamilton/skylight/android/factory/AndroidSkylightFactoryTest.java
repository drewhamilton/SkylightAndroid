package drewhamilton.skylight.android.factory;

import android.location.Location;
import drewhamilton.skylight.backport.SkylightDay;
import drewhamilton.skylight.backport.SkylightForCoordinates;
import org.junit.Test;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;
import org.threeten.bp.OffsetTime;
import org.threeten.bp.ZoneOffset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AndroidSkylightFactoryTest {

    @Test
    public void createForLocationWithLocation_calculatesByCoordinates() {
        SkylightForCoordinates skylightForAmsterdam = AndroidSkylightFactory.createForLocation(amsterdam());
        SkylightDay february2 = skylightForAmsterdam.getSkylightDay(LocalDate.of(2019, Month.FEBRUARY, 2));

        assertTrue(february2 instanceof SkylightDay.Typical);
        SkylightDay.Typical february2Cast = (SkylightDay.Typical) february2;
        assertEquals(OffsetTime.of(6, 45, 5, 170_000_000, ZoneOffset.UTC), february2Cast.getDawn());
        assertEquals(OffsetTime.of(7, 23, 58, 598_000_000, ZoneOffset.UTC), february2Cast.getSunrise());
        assertEquals(OffsetTime.of(16, 26, 38, 555_000_000, ZoneOffset.UTC), february2Cast.getSunset());
        assertEquals(OffsetTime.of(17, 5, 31, 982_000_000, ZoneOffset.UTC), february2Cast.getDusk());
    }

    private static Location amsterdam() {
        Location amsterdam = mock(Location.class);
        when(amsterdam.getLatitude()).thenReturn(52.3680);
        when(amsterdam.getLongitude()).thenReturn(4.9036);
        return amsterdam;
    }
}
