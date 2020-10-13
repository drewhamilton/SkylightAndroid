package dev.drewhamilton.skylight.android;

import androidx.appcompat.app.AppCompatDelegate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.OffsetTime;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class AutoNightDelegateTest {

    @Mock private AppCompatDelegate mockAppCompatDelegate;

    @Test
    public void fallback_isStaticAndNonNull() {
        assertNotNull(AutoNightDelegate.fallback(mockAppCompatDelegate));
    }

    @Test
    public void ofTimes_isStaticAndNonNull() {
        assertNotNull(AutoNightDelegate.ofTimes(mockAppCompatDelegate, OffsetTime.now(), OffsetTime.now()));
    }

    @Test
    public void ofCoordinates_isStaticAndNonNull() {
        assertNotNull(AutoNightDelegate.ofCoordinates(mockAppCompatDelegate, 0.1, 0.2));
    }
}