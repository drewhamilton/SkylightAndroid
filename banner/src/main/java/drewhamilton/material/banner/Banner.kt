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
}
