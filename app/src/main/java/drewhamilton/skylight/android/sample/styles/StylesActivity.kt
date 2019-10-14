package drewhamilton.skylight.android.sample.styles

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import drewhamilton.skylight.android.sample.R
import kotlinx.android.synthetic.main.styles_destination.buttonsSwitch
import kotlinx.android.synthetic.main.styles_destination.elevatedButton
import kotlinx.android.synthetic.main.styles_destination.errorBanner
import kotlinx.android.synthetic.main.styles_destination.motionLayout
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

        errorBanner.setPrimaryButtonOnClickListener(View.OnClickListener {
            motionLayout.setTransitionListener(TryAgain())
            motionLayout.transitionToStart()
        })
        errorBanner.setSecondaryButtonOnClickListener(View.OnClickListener { motionLayout.transitionToStart() })
        textButton.setOnClickListener { motionLayout.transitionToEnd() }
    }

    /**
     * Listens for the motion layout's next transition to complete and then removes itself and transitions back to the
     * layout's end state.
     */
    private class TryAgain: MotionLayout.TransitionListener {
        override fun onTransitionTrigger(
            motionLayout: MotionLayout,
            triggerId: Int,
            positive: Boolean,
            progress: Float
        ) = Unit

        override fun onTransitionStarted(motionLayout: MotionLayout, startid: Int, endId: Int) = Unit

        override fun onTransitionChange(
            motionLayout: MotionLayout,
            startId: Int, endId: Int,
            progress: Float
        ) = Unit

        override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
            motionLayout.setTransitionListener(null)
            motionLayout.transitionToEnd()
        }
    }
}
