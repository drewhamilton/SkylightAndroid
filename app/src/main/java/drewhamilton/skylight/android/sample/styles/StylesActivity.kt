package drewhamilton.skylight.android.sample.styles

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import drewhamilton.skylight.android.sample.R
import kotlinx.android.synthetic.main.styles_destination.buttonsSwitch
import kotlinx.android.synthetic.main.styles_destination.elevatedButton
import kotlinx.android.synthetic.main.styles_destination.errorBanner
import kotlinx.android.synthetic.main.styles_destination.outlinedButton
import kotlinx.android.synthetic.main.styles_destination.textButton
import kotlinx.android.synthetic.main.styles_destination.toolbar

/**
 * Does nothing but demonstrate some styled UI components.
 */
class StylesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.styles_destination)

        toolbar.setNavigationOnClickListener { finish() }

        buttonsSwitch.setOnCheckedChangeListener { _, isChecked ->
            elevatedButton.isEnabled = isChecked
            outlinedButton.isEnabled = isChecked
            textButton.isEnabled = isChecked
        }

        elevatedButton.setOnClickListener {
            BottomSheetDialog(this).apply {
                setContentView(R.layout.bottom_sheet)
            }.show()
        }

        errorBanner.setPositiveButtonOnClickListener(View.OnClickListener { errorBanner.dismiss() })
        textButton.setOnClickListener { errorBanner.show() }
    }
}
