package drewhamilton.skylight.views.event

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import drewhamilton.skylight.views.R
import drewhamilton.skylight.views.test.CustomViewMatchers
import drewhamilton.skylight.views.test.view.ViewTest
import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test

class SkylightEventViewTest : ViewTest<SkylightEventView>() {

    private val testLabel
        get() = activity.getString(R.string.testLabel)

    private val testTime
        get() = activity.getString(R.string.testTime)

    private val testHint = "Test hint"

    private val withNullHint
        get() = withHint(`is`(null as String?))

    @Before
    fun setUp() = launchActivity()

    //region initialize
    @Test
    fun inflate_withoutAttributes_viewHasEmptyTextViews() {
        setView(R.layout.test_skylight_event_view_no_attributes)
        val inflatedView = getView()

        assertEquals("", inflatedView.labelText)
        assertEquals("", inflatedView.timeText)
        assertEquals(null, inflatedView.timeHint)

        onView(withId(R.id.label))
            .check(matches(withText("")))
            .check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
        onView(withId(R.id.time))
            .check(matches(withText("")))
            .check(matches(withNullHint))
    }

    @Test
    fun inflate_withAttributes_viewHasPopulatedTextViews() {
        setView(R.layout.test_skylight_event_view_with_attributes)
        val inflatedView = getView()

        assertEquals(testLabel, inflatedView.labelText)
        assertEquals(testTime, inflatedView.timeText)
        assertEquals(null, inflatedView.timeHint)

        onView(withId(R.id.label))
            .check(matches(withText(testLabel)))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.time))
            .check(matches(withText(testTime)))
            .check(matches(withNullHint))
    }

    @Test
    fun construct_withContext_viewHasEmptyTextViews() {
        val constructedView = SkylightEventView(activity)
        setView(constructedView)
        assertSame(constructedView, getView())

        assertEquals("", constructedView.labelText)
        assertEquals("", constructedView.timeText)
        assertEquals(null, constructedView.timeHint)

        onView(withId(R.id.label))
            .check(matches(withText("")))
            .check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
        onView(withId(R.id.time))
            .check(matches(withText("")))
            .check(matches(withNullHint))
    }
    //endregion

    //region setLabelText
    @Test
    fun setLabelText_withStringResource_setsLabelText() {
        setView(SkylightEventView(activity))
        val view = getView()

        runOnUiThread {
            view.setLabelText(R.string.testLabel)
        }

        onView(withId(R.id.label))
            .check(matches(withText(R.string.testLabel)))
            .check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
        assertEquals(testLabel, view.labelText)
    }

    @Test
    fun setLabelText_withCharSequence_setsLabelText() {
        setView(SkylightEventView(activity))
        val view = getView()

        runOnUiThread {
            view.labelText = testLabel
        }

        onView(withId(R.id.label))
            .check(matches(withText(testLabel)))
            .check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
        assertEquals(testLabel, view.labelText)
    }
    //endregion

    //region setTimeText
    @Test
    fun setTimeText_withStringResource_setsTimeTextAndLabelVisibility() {
        setView(SkylightEventView(activity))
        val view = getView()

        runOnUiThread {
            view.setLabelText(R.string.testLabel)
            view.setTimeText(R.string.testTime)
        }

        onView(withId(R.id.label)).check(matches(isDisplayed()))
        onView(withId(R.id.time))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.testTime)))
            .check(matches(withNullHint))
        assertEquals(testTime, view.timeText)
    }

    @Test
    fun setTimeText_withCharSequence_setsTimeTextAndLabelVisibility() {
        setView(SkylightEventView(activity))
        val view = getView()

        runOnUiThread {
            view.setLabelText(R.string.testLabel)
            view.timeText = testTime
        }

        onView(withId(R.id.label)).check(matches(isDisplayed()))
        onView(withId(R.id.time))
            .check(matches(isDisplayed()))
            .check(matches(withText(testTime)))
            .check(matches(withNullHint))
        assertEquals(testTime, view.timeText)
    }
    //endregion

    @Test
    fun setLabelTextAppearance_appliesExpectedSize() {
        setView(R.layout.test_skylight_event_view_no_attributes)
        val view = getView()

        runOnUiThread {
            view.setLabelTextAppearance(android.R.style.TextAppearance_Material_Display1)
            view.timeText = testTime
            view.labelText = testLabel
        }

        onView(withId(R.id.label))
            .check(matches(isDisplayed()))
            .check(matches(CustomViewMatchers.withTextSize(34.spToPx())))
    }

    @Test
    fun setTimeTextAppearance_appliesExpectedSize() {
        setView(R.layout.test_skylight_event_view_no_attributes)
        val view = getView()

        runOnUiThread {
            view.setTimeTextAppearance(android.R.style.TextAppearance_Material_Display3)
            view.timeText = testTime
        }

        onView(withId(R.id.time))
            .check(matches(isDisplayed()))
            .check(matches(CustomViewMatchers.withTextSize(56.spToPx())))
    }

    @Test
    fun setTimeTextAutoSizeRange_appliesExpectedSize() {
        setView(R.layout.test_skylight_event_view_no_attributes)
        val view = getView()

        val minSize = 12.spToPx()
        val maxSize = 20.spToPx()
        runOnUiThread {
            view.setTimeTextAutoSizeRange(minSize.toInt(), maxSize.toInt(), stepGranularity = 1)
            view.timeText = testTime
        }

        onView(withId(R.id.time))
            .check(matches(isDisplayed()))
            .check(matches(CustomViewMatchers.withTextSize(maxSize)))
    }

    //region setTimeHint
    @Test
    fun setTimeHint_nonNull_setsTimeHintAndLabelVisibility() {
        setView(SkylightEventView(activity))
        val view = getView()

        runOnUiThread {
            view.setLabelText(R.string.testLabel)
            view.timeHint = testHint
        }

        onView(withId(R.id.label)).check(matches(isDisplayed()))
        onView(withId(R.id.time))
            .check(matches(isDisplayed()))
            .check(matches(withText("")))
            .check(matches(withHint(testHint)))
        assertEquals(testHint, view.timeHint)
    }

    @Test
    fun setTimeHint_null_removesTimeHintAndLabelVisibility() {
        setView(SkylightEventView(activity))
        val view = getView()

        runOnUiThread {
            view.setLabelText(R.string.testLabel)
            view.timeHint = testHint
        }

        onView(withId(R.id.time)).check(matches(withHint(testHint)))

        runOnUiThread { view.timeHint = null }

        onView(withId(R.id.label)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
        onView(withId(R.id.time))
            .check(matches(withText("")))
            .check(matches(withNullHint))
        assertEquals(null, view.timeHint)
    }
    //endregion

    // TODO: Create an inline Sp class
    private fun Int.spToPx() = this * activity.resources.displayMetrics.scaledDensity
}
