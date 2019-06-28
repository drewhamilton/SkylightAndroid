package drewhamilton.skylight.android.theme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class AutoNightActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(AutoNightDelegate())
    }
}
