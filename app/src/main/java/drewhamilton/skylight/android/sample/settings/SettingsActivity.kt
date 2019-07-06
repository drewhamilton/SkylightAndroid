package drewhamilton.skylight.android.sample.settings

import android.os.Bundle
import drewhamilton.skylight.android.sample.AppComponent
import drewhamilton.skylight.android.sample.R
import drewhamilton.skylight.android.sample.rx.ui.RxActivity
import drewhamilton.skylight.android.sample.source.MutableSkylightRepository
import drewhamilton.skylight.android.sample.source.SkylightRepository
import kotlinx.android.synthetic.main.settings_destination.dummyButton
import kotlinx.android.synthetic.main.settings_destination.localButton
import kotlinx.android.synthetic.main.settings_destination.networkButton
import kotlinx.android.synthetic.main.settings_destination.sourceSelection
import kotlinx.android.synthetic.main.settings_destination.toolbar
import javax.inject.Inject

class SettingsActivity : RxActivity() {

    @Suppress("ProtectedInFinal")
    @Inject protected lateinit var skylightRepository: MutableSkylightRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        AppComponent.instance.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_destination)

        toolbar.setNavigationOnClickListener { finish() }

        skylightRepository.getSelectedSkylightTypeStream()
            .subscribe {
                when (it!!) {
                    SkylightRepository.SkylightType.SSO -> networkButton.isChecked = true
                    SkylightRepository.SkylightType.CALCULATOR -> localButton.isChecked = true
                    SkylightRepository.SkylightType.DUMMY -> dummyButton.isChecked = true
                }
            }
            .untilDestroy()

        sourceSelection.setOnCheckedChangeListener { _, checkedId ->
            val selectedType = when (checkedId) {
                R.id.localButton -> SkylightRepository.SkylightType.CALCULATOR
                R.id.dummyButton -> SkylightRepository.SkylightType.DUMMY
                else -> SkylightRepository.SkylightType.SSO
            }
            skylightRepository.selectSkylightType(selectedType).subscribe()
        }
    }
}
