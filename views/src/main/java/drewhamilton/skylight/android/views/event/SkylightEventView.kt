package drewhamilton.skylight.android.views.event

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import com.google.android.material.card.MaterialCardView
import drewhamilton.inlinedimens.PxInt
import drewhamilton.inlinedimens.getDimensionPxSize
import drewhamilton.inlinedimens.setAutoSizeTextTypeUniformWithConfiguration
import drewhamilton.skylight.android.views.R
import drewhamilton.skylight.android.views.setCompatTextAppearance
import kotlinx.android.synthetic.main.view_skylight_event.view.label
import kotlinx.android.synthetic.main.view_skylight_event.view.time

/**
 * A simple card view showing a single Skylight event. Typically used to display the time of
 * the event and the label, e.g. "Sunrise" or "Sunset".
 */
class SkylightEventView : MaterialCardView {

    var labelText: CharSequence
        get() = label.text
        set(text) {
            label.text = text
        }

    var timeText: CharSequence
        get() = time.text
        set(text) {
            time.text = text
            label.visibility = if (shouldShowLabel) View.VISIBLE else View.INVISIBLE
        }

    internal var timeHint: CharSequence?
        get() = time.hint
        set(hint) {
            time.hint = hint
            label.visibility = if (shouldShowLabel) View.VISIBLE else View.INVISIBLE
        }

    private val shouldShowLabel
        get() = !(time.text.isEmpty() && time.hint?.isEmpty() ?: true)

    init {
        inflate(context, R.layout.view_skylight_event, this)
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttributeSet(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttributeSet(attrs)
    }

    fun setLabelText(@StringRes resId: Int) = label.setText(resId)

    /**
     * Set the text appearance for the label view to [resId].
     */
    fun setLabelTextAppearance(@StyleRes resId: Int) {
        label.setCompatTextAppearance(resId)
    }

    /**
     * Set the time text to the string resolved through [resId].
     */
    fun setTimeText(@StringRes resId: Int) {
        time.setText(resId)
        label.visibility = if (shouldShowLabel) View.VISIBLE else View.INVISIBLE
    }

    /**
     * Set the text appearance for the time view to [resId].
     */
    fun setTimeTextAppearance(@StyleRes resId: Int) {
        time.setCompatTextAppearance(resId)
    }

    /**
     * Set the time view to auto-size its text from [minSizePx] to [maxSizePx] based on the view size.
     */
    fun setTimeTextAutoSizeRange(
        minSizePx: Int, maxSizePx: Int,
        stepGranularity: Int = resources.getDimensionPixelSize(R.dimen.skylight_granularity_skylightEventTime)
    ) = setTimeTextAutoSizeRange(PxInt(minSizePx), PxInt(maxSizePx), PxInt(stepGranularity))

    /**
     * Set the time view to auto-size its text from [minSize] to [maxSize] based on the view size.
     */
    fun setTimeTextAutoSizeRange(
        minSize: PxInt, maxSize: PxInt,
        stepGranularity: PxInt = resources.getDimensionPxSize(R.dimen.skylight_granularity_skylightEventTime)
    ) = time.setAutoSizeTextTypeUniformWithConfiguration(minSize, maxSize, stepGranularity)

    private fun initAttributeSet(attrs: AttributeSet) {
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.SkylightEventView)
        try {
            applyLabelTextAppearance(styledAttributes)
            label.text = styledAttributes.getString(R.styleable.SkylightEventView_skylightEventLabelText)

            applyTimeTextAppearance(styledAttributes)
            applyTimeTextAutoSizes(styledAttributes)
            time.text = styledAttributes.getString(R.styleable.SkylightEventView_skylightEventTimeText)

            if (shouldShowLabel) label.visibility = View.VISIBLE
        } finally {
            styledAttributes.recycle()
        }
    }

    private fun applyLabelTextAppearance(styledAttributes: TypedArray) {
        val labelTextAppearance = styledAttributes.getResourceId(
            R.styleable.SkylightEventView_skylightEventLabelTextAppearance,
            R.style.TextAppearance_AppCompat_Caption
        )
        label.setCompatTextAppearance(labelTextAppearance)
    }

    private fun applyTimeTextAppearance(styledAttributes: TypedArray) {
        val timeTextAppearance = styledAttributes.getResourceId(
            R.styleable.SkylightEventView_skylightEventTimeTextAppearance,
            R.style.TextAppearance_AppCompat_Display3
        )
        time.setCompatTextAppearance(timeTextAppearance)
    }

    private fun applyTimeTextAutoSizes(styledAttributes: TypedArray) {
        val unspecifiedTextSize = -1
        val timeTextMinSize = styledAttributes.getDimensionPixelSize(
            R.styleable.SkylightEventView_skylightEventTimeTextMinSize,
            unspecifiedTextSize
        )
        val timeTextMaxSize = styledAttributes.getDimensionPixelSize(
            R.styleable.SkylightEventView_skylightEventTimeTextMaxSize,
            unspecifiedTextSize
        )
        if (!(timeTextMinSize == unspecifiedTextSize && timeTextMaxSize == unspecifiedTextSize)) {
            when {
                timeTextMinSize == unspecifiedTextSize ->
                    time.setTextSize(TypedValue.COMPLEX_UNIT_PX, timeTextMaxSize.toFloat())
                timeTextMaxSize == unspecifiedTextSize ->
                    time.setTextSize(TypedValue.COMPLEX_UNIT_PX, timeTextMinSize.toFloat())
                else ->
                    setTimeTextAutoSizeRange(timeTextMinSize, timeTextMaxSize)
            }
        }
    }
}