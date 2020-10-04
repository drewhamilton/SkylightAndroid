package dev.drewhamilton.skylight.android.views.event

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import dev.drewhamilton.inlinedimens.exact
import dev.drewhamilton.inlinedimens.sp
import dev.drewhamilton.inlinedimens.toPx
import dev.drewhamilton.inlinedimens.toSize
import dev.drewhamilton.skylight.android.views.R
import dev.drewhamilton.android.test.CustomViewMatchers
import dev.drewhamilton.android.test.view.ViewTest
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SkylightEventViewTest : ViewTest<SkylightEventView>() {

    private val testLabel
        get() = activity.getString(R.string.testLabel)

    private val testTime
        get() = activity.getString(R.string.testTime)

    private val testHint = "Test hint"

    private val withNullHint
        get() = ViewMatchers.withHint(Matchers.`is`(null as String?))

    @Before
    fun setUp() = launchActivity()

    //region initialize
    @Test
    fun inflate_withoutAttributes_viewHasEmptyTextViews() {
        setView(R.layout.test_skylight_event_view_no_attributes)
        val inflatedView = getView()

        Assert.assertEquals("", inflatedView.labelText)
        Assert.assertEquals("", inflatedView.timeText)
        Assert.assertEquals(null, inflatedView.timeHint)

        Espresso.onView(ViewMatchers.withId(R.id.label))
            .check(ViewAssertions.matches(ViewMatchers.withText("")))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)))
        Espresso.onView(ViewMatchers.withId(R.id.time))
            .check(ViewAssertions.matches(ViewMatchers.withText("")))
            .check(ViewAssertions.matches(withNullHint))
    }

    @Test
    fun inflate_withAttributes_viewHasPopulatedTextViews() {
        setView(R.layout.test_skylight_event_view_with_attributes)
        val inflatedView = getView()

        Assert.assertEquals(testLabel, inflatedView.labelText)
        Assert.assertEquals(testTime, inflatedView.timeText)
        Assert.assertEquals(null, inflatedView.timeHint)

        Espresso.onView(ViewMatchers.withId(R.id.label))
            .check(ViewAssertions.matches(ViewMatchers.withText(testLabel)))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        Espresso.onView(ViewMatchers.withId(R.id.time))
            .check(ViewAssertions.matches(ViewMatchers.withText(testTime)))
            .check(ViewAssertions.matches(withNullHint))
    }

    @Test
    fun construct_withContext_viewHasEmptyTextViews() {
        val constructedView = SkylightEventView(activity)
        setView(constructedView)
        Assert.assertSame(constructedView, getView())

        Assert.assertEquals("", constructedView.labelText)
        Assert.assertEquals("", constructedView.timeText)
        Assert.assertEquals(null, constructedView.timeHint)

        Espresso.onView(ViewMatchers.withId(R.id.label))
            .check(ViewAssertions.matches(ViewMatchers.withText("")))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)))
        Espresso.onView(ViewMatchers.withId(R.id.time))
            .check(ViewAssertions.matches(ViewMatchers.withText("")))
            .check(ViewAssertions.matches(withNullHint))
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

        Espresso.onView(ViewMatchers.withId(R.id.label))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.testLabel)))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)))
        Assert.assertEquals(testLabel, view.labelText)
    }

    @Test
    fun setLabelText_withCharSequence_setsLabelText() {
        setView(SkylightEventView(activity))
        val view = getView()

        runOnUiThread {
            view.labelText = testLabel
        }

        Espresso.onView(ViewMatchers.withId(R.id.label))
            .check(ViewAssertions.matches(ViewMatchers.withText(testLabel)))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)))
        Assert.assertEquals(testLabel, view.labelText)
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

        Espresso.onView(ViewMatchers.withId(R.id.label)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.time))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.testTime)))
            .check(ViewAssertions.matches(withNullHint))
        Assert.assertEquals(testTime, view.timeText)
    }

    @Test
    fun setTimeText_withCharSequence_setsTimeTextAndLabelVisibility() {
        setView(SkylightEventView(activity))
        val view = getView()

        runOnUiThread {
            view.setLabelText(R.string.testLabel)
            view.timeText = testTime
        }

        Espresso.onView(ViewMatchers.withId(R.id.label)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.time))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .check(ViewAssertions.matches(ViewMatchers.withText(testTime)))
            .check(ViewAssertions.matches(withNullHint))
        Assert.assertEquals(testTime, view.timeText)
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

        Espresso.onView(ViewMatchers.withId(R.id.label))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .check(ViewAssertions.matches(CustomViewMatchers.withTextSize(34.sp.toPx(activity).toSize().exact().value)))
    }

    @Test
    fun setTimeTextAppearance_appliesExpectedSize() {
        setView(R.layout.test_skylight_event_view_no_attributes)
        val view = getView()

        runOnUiThread {
            view.setTimeTextAppearance(android.R.style.TextAppearance_Material_Display3)
            view.timeText = testTime
        }

        Espresso.onView(ViewMatchers.withId(R.id.time))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .check(ViewAssertions.matches(CustomViewMatchers.withTextSize(56.sp.toPx(activity).value)))
    }

    @Test
    fun setTimeTextAutoSizeRange_appliesExpectedSize() {
        setView(R.layout.test_skylight_event_view_no_attributes)
        val view = getView()

        val minSize = 12.sp.toPx(activity).toSize()
        val maxSize = 20.sp.toPx(activity).toSize()
        runOnUiThread {
            view.setTimeTextAutoSizeRange(minSize.value, maxSize.value, stepGranularity = 1)
            view.timeText = testTime
        }

        Espresso.onView(ViewMatchers.withId(R.id.time))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .check(ViewAssertions.matches(CustomViewMatchers.withTextSize(maxSize.exact().value)))
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

        Espresso.onView(ViewMatchers.withId(R.id.label)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.time))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .check(ViewAssertions.matches(ViewMatchers.withText("")))
            .check(ViewAssertions.matches(ViewMatchers.withHint(testHint)))
        Assert.assertEquals(testHint, view.timeHint)
    }

    @Test
    fun setTimeHint_null_removesTimeHintAndLabelVisibility() {
        setView(SkylightEventView(activity))
        val view = getView()

        runOnUiThread {
            view.setLabelText(R.string.testLabel)
            view.timeHint = testHint
        }

        Espresso.onView(ViewMatchers.withId(R.id.time)).check(ViewAssertions.matches(ViewMatchers.withHint(testHint)))

        runOnUiThread { view.timeHint = null }

        Espresso.onView(ViewMatchers.withId(R.id.label))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)))
        Espresso.onView(ViewMatchers.withId(R.id.time))
            .check(ViewAssertions.matches(ViewMatchers.withText("")))
            .check(ViewAssertions.matches(withNullHint))
        Assert.assertEquals(null, view.timeHint)
    }
    //endregion
}
