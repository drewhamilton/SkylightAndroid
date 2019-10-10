package drewhamilton.skylight.android.sample.styles

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import drewhamilton.skylight.android.sample.R
import kotlinx.android.synthetic.main.styles_destination.toolbar

/**
 * Does nothing but demonstrate some styled UI components.
 */
class StylesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.styles_destination)

        toolbar.setNavigationOnClickListener { finish() }
    }
}
