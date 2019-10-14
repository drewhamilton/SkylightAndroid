package drewhamilton.material.banner

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import drewhamilton.material.banner.databinding.BannerBinding

class Banner : ConstraintLayout {

    private val binding: BannerBinding by lazy { BannerBinding.bind(this) }

    init {
        inflate(context, R.layout.banner, this)
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttributeSet(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttributeSet(attrs)
    }

    private fun initAttributeSet(attrs: AttributeSet) {
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.Banner)
        try {
            binding.bannerMessageView.text = styledAttributes.getString(R.styleable.Banner_bannerMessage)
            binding.positiveButton.text = styledAttributes.getString(R.styleable.Banner_bannerPositiveButtonText)
        } finally {
            styledAttributes.recycle()
        }
    }

    var bannerMessage: CharSequence
        get() = binding.bannerMessageView.text
        set(value) {
            binding.bannerMessageView.text = value
        }

    var positiveButtonText: CharSequence
        get() = binding.positiveButton.text
        set(value) {
            binding.positiveButton.text = value
        }

    fun setBannerMessage(@StringRes resId: Int) = binding.bannerMessageView.setText(resId)

    fun setPositiveButtonText(@StringRes resId: Int) = binding.positiveButton.setText(resId)

    fun setPositiveButtonOnClickListener(listener: OnClickListener) {
        binding.positiveButton.setOnClickListener(listener)
    }

    /**
     * Animates this banner from its current Y position into alignment with the top of its parent.
     */
    fun show() {
        ValueAnimator.ofInt(top, 0).apply {
            duration = ANIMATION_DURATION_MS
            interpolator = DecelerateInterpolator()
            addUpdateListener { animation ->
                top = animation.animatedValue as Int
            }
        }.start()
    }

    /**
     * Animates this banner from its current Y position to be completely above the bounds of its parent.
     */
    fun dismiss() {
        ValueAnimator.ofInt(top, -height).apply {
            duration = ANIMATION_DURATION_MS
            interpolator = AccelerateInterpolator()
            addUpdateListener { animation ->
                top = animation.animatedValue as Int
            }
        }.start()
    }

    private companion object {
        const val ANIMATION_DURATION_MS = 250L
    }
}
