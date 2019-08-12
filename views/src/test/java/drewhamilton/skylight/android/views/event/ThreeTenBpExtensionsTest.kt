package drewhamilton.skylight.android.views.event

import android.content.Context
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import org.junit.Before
import org.junit.Test
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class ThreeTenBpExtensionsTest : AlteredTimeZoneTest() {

    private val dummyTime = ZonedDateTime.of(2019, 8, 11, 23, 45, 56, 789, ZoneOffset.UTC)
    private val dummyTimeString = "Dummy time string"
    private val dummyZoneOffset = ZoneOffset.of("+02:00")

    private val defaultDateFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
    private val defaultDummyTimeString = defaultDateFormat.format(dummyTime)

    private val dummyFallbackStringRes = 4321
    private val dummyFallbackString = "Test fallback"

    private lateinit var mockDateTimeFormatter: DateTimeFormatter

    private lateinit var mockSkylightEventView: SkylightEventView
    private lateinit var mockContext: Context

    @Before
    fun setUpMocks() {
        mockDateTimeFormatter = mock {
            on { format(dummyTime) } doReturn dummyTimeString
            on { format(dummyTime.withZoneSameInstant(dummyZoneOffset)) } doReturn dummyTimeString
            on { format(dummyTime.withZoneSameInstant(ZoneId.systemDefault())) } doReturn dummyTimeString
        }

        mockContext = mock {
            on { getString(dummyFallbackStringRes) } doReturn dummyFallbackString
        }
        mockSkylightEventView = mock {
            on { context } doReturn mockContext
        }
    }

    @Test
    fun `setTime(OffsetTime?) with non-null date sets formatted date text`() {
        mockSkylightEventView.setTime(dummyTime)
        verifyTimeTextSet(defaultDummyTimeString)
    }

    @Test
    fun `setTime(OffsetTime?) with null date sets empty text`() {
        mockSkylightEventView.setTime(null as ZonedDateTime?)
        verifyTimeHintSet("")
    }

    @Test
    fun `setTime(OffsetTime?, Int) with non-null date sets formatted date text`() {
        mockSkylightEventView.setTime(dummyTime, dummyFallbackStringRes)
        verify(mockSkylightEventView).context
        verifyTimeTextSet(defaultDummyTimeString)
    }

    @Test
    fun `setTime(OffsetTime?, Int) with null date sets fallback text from resource`() {
        mockSkylightEventView.setTime(null as ZonedDateTime?, dummyFallbackStringRes)
        verify(mockSkylightEventView).context
        verifyTimeHintSet(dummyFallbackString)
    }

    @Test
    fun `setTime(OffsetTime?, String) with non-null date sets formatted date text`() {
        mockSkylightEventView.setTime(dummyTime, fallback = dummyFallbackString)
        verifyTimeTextSet(defaultDummyTimeString)
    }

    @Test
    fun `setTime(OffsetTime?, String) with null date sets fallback text`() {
        mockSkylightEventView.setTime(null as ZonedDateTime?, fallback = dummyFallbackString)
        verifyTimeHintSet(dummyFallbackString)
    }

    @Test
    fun `setTime(OffsetTime?, DateTimeFormatter) with non-null date sets formatted date text`() {
        mockSkylightEventView.setTime(dummyTime, mockDateTimeFormatter)
        verifyTimeTextSet(dummyTimeString)
    }

    @Test
    fun `setTime(OffsetTime?, DateTimeFormatter) with null date sets empty text`() {
        mockSkylightEventView.setTime(null, mockDateTimeFormatter)
        verifyTimeHintSet("")
    }

    @Test
    fun `setTime(OffsetTime?, DateTimeFormatter, Int) with non-null date sets formatted date text`() {
        mockSkylightEventView.setTime(dummyTime, mockDateTimeFormatter, dummyFallbackStringRes)
        verify(mockSkylightEventView).context
        verifyTimeTextSet(dummyTimeString)
    }

    @Test
    fun `setTime(OffsetTime?, DateTimeFormatter, Int) with null date sets fallback text from resource`() {
        mockSkylightEventView.setTime(null, mockDateTimeFormatter, dummyFallbackStringRes)
        verify(mockSkylightEventView).context
        verifyTimeHintSet(dummyFallbackString)
    }

    @Test
    fun `setTime(OffsetTime?, DateTimeFormatter, String) with non-null date sets formatted date text`() {
        mockSkylightEventView.setTime(dummyTime, mockDateTimeFormatter, fallback = dummyFallbackString)
        verifyTimeTextSet(dummyTimeString)
    }

    @Test
    fun `setTime(OffsetTime?, DateTimeFormatter, String) with null date sets fallback text`() {
        mockSkylightEventView.setTime(null, mockDateTimeFormatter, fallback = dummyFallbackString)
        verifyTimeHintSet(dummyFallbackString)
    }

    private fun verifyTimeTextSet(text: String) {
        verify(mockSkylightEventView).timeText = text
        verify(mockSkylightEventView).timeHint = ""
        verifyNoMoreInteractions(mockSkylightEventView)
    }

    private fun verifyTimeHintSet(hint: String) {
        verify(mockSkylightEventView).timeHint = hint
        verify(mockSkylightEventView).timeText = ""
        verifyNoMoreInteractions(mockSkylightEventView)
    }
}
