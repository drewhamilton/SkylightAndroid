package dev.drewhamilton.skylight.android.views

import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.core.widget.TextViewCompat

internal fun TextView.setCompatTextAppearance(@StyleRes resId: Int) = TextViewCompat.setTextAppearance(this, resId)
