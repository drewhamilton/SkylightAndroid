package dev.drewhamilton.skylight.android.demo.settings

import android.os.Bundle
import dev.drewhamilton.skylight.android.demo.AppComponent
import dev.drewhamilton.skylight.android.demo.R
import dev.drewhamilton.skylight.android.demo.rx.ui.RxActivity
import dev.drewhamilton.skylight.android.demo.source.MutableSkylightRepository
import dev.drewhamilton.skylight.android.demo.source.SkylightRepository
import dev.drewhamilton.skylight.android.demo.theme.MutableThemeRepository
import javax.inject.Inject
import kotlinx.android.synthetic.main.settings_destination.darkButton
import kotlinx.android.synthetic.main.settings_destination.dummyButton
import kotlinx.android.synthetic.main.settings_destination.lightButton
import kotlinx.android.synthetic.main.settings_destination.localButton
import kotlinx.android.synthetic.main.settings_destination.networkButton
import kotlinx.android.synthetic.main.settings_destination.sourceSelection
import kotlinx.android.synthetic.main.settings_destination.systemButton
import kotlinx.android.synthetic.main.settings_destination.themeSelection
import kotlinx.android.synthetic.main.settings_destination.toolbar

class SettingsActivity : RxActivity() {

    @Suppress("ProtectedInFinal")
    @Inject protected lateinit var skylightRepository: MutableSkylightRepository

    @Suppress("ProtectedInFinal")
    @Inject protected lateinit var themeRepository: MutableThemeRepository

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
            skylightRepository.selectSkylightType(selectedType)
                .subscribe()
        }

        themeRepository.getSelectedDarkMode()
            .subscribe {
                when (it!!) {
                    MutableThemeRepository.DarkMode.SYSTEM -> systemButton.isChecked = true
                    MutableThemeRepository.DarkMode.LIGHT -> lightButton.isChecked = true
                    MutableThemeRepository.DarkMode.DARK -> darkButton.isChecked = true
                }
            }
            .untilDestroy()

        themeSelection.setOnCheckedChangeListener { _, checkedId ->
            val selectedDarkMode = when (checkedId) {
                R.id.lightButton -> MutableThemeRepository.DarkMode.LIGHT
                R.id.darkButton -> MutableThemeRepository.DarkMode.DARK
                else -> MutableThemeRepository.DarkMode.SYSTEM
            }
            themeRepository.selectDarkMode(selectedDarkMode)
                .subscribe()
        }
    }
}
