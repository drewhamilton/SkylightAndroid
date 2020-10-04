package dev.drewhamilton.skylight.android.views.event

import java.util.TimeZone
import org.junit.After
import org.junit.Before

/**
 * A base test class that alters the system default time zone to a specified time zone before each test, and restores
 * the previous system default after each test.
 */
abstract class AlteredTimeZoneTest(
    private val testSystemDefaultTimeZone: TimeZone? = TimeZone.getTimeZone("UTC")
) {

    private var systemDefaultTimeZone: TimeZone? = null

    @Before
    fun setDefaultTimeZoneForTest() {
        systemDefaultTimeZone = TimeZone.getDefault()
        TimeZone.setDefault(testSystemDefaultTimeZone)
    }

    @After
    fun restoreSystemDefaultTimeZone() {
        TimeZone.setDefault(systemDefaultTimeZone)
    }
}
