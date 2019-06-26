package drewhamilton.skylight.android.theme

import androidx.appcompat.app.AppCompatActivity
import drewhamilton.skylight.backport.Coordinates
import drewhamilton.skylight.backport.calculator.CalculatorSkylight
import drewhamilton.skylight.backport.forCoordinates

class AutoNightActivity : AppCompatActivity() {

    private val skylight = CalculatorSkylight().forCoordinates(Coordinates(0.0, 0.0))
    private val autoNightDelegate: AutoNightDelegate = AutoNightDelegate(skylight)

    override fun onStart() {
        super.onStart()
        autoNightDelegate.onActivityStart()
    }

    override fun onStop() {
        autoNightDelegate.onActivityStop()
        super.onStop()
    }
}
