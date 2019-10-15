package drewhamilton.material.banner

import android.content.Context
import android.util.AttributeSet
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
            binding.primaryButton.text = styledAttributes.getString(R.styleable.Banner_bannerPrimaryButtonText)
            binding.secondaryButton.text = styledAttributes.getString(R.styleable.Banner_bannerSecondaryButtonText)
        } finally {
            styledAttributes.recycle()
        }
    }

    var bannerMessage: CharSequence
        get() = binding.bannerMessageView.text
        set(value) {
            binding.bannerMessageView.text = value
        }

    var primaryButtonText: CharSequence
        get() = binding.primaryButton.text
        set(value) {
            binding.primaryButton.text = value
        }

    var secondaryButtonText: CharSequence
        get() = binding.secondaryButton.text
        set(value) {
            binding.secondaryButton.text = value
        }

    fun setBannerMessage(@StringRes resId: Int) = binding.bannerMessageView.setText(resId)

    fun setPrimaryButtonText(@StringRes resId: Int) = binding.primaryButton.setText(resId)

    fun setSecondaryButtonText(@StringRes resId: Int) = binding.secondaryButton.setText(resId)

    fun setPrimaryButtonOnClickListener(listener: OnClickListener) {
        binding.primaryButton.setOnClickListener(listener)
    }

    fun setSecondaryButtonOnClickListener(listener: OnClickListener) {
        binding.secondaryButton.setOnClickListener(listener)
    }
}
