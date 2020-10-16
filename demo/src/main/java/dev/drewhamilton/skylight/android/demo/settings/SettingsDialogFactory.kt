package dev.drewhamilton.skylight.android.demo.settings

import android.content.Context
import android.view.LayoutInflater
import com.google.android.material.bottomsheet.BottomSheetDialog
import dev.drewhamilton.skylight.android.demo.BuildConfig
import dev.drewhamilton.skylight.android.demo.R
import dev.drewhamilton.skylight.android.demo.databinding.SettingsDestinationBinding
import dev.drewhamilton.skylight.android.demo.source.MutableSkylightRepository
import dev.drewhamilton.skylight.android.demo.source.SkylightRepository
import dev.drewhamilton.skylight.android.demo.theme.MutableThemeRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SettingsDialogFactory @Inject constructor(
    private val skylightRepository: MutableSkylightRepository,
    private val themeRepository: MutableThemeRepository,
) {
    fun createSettingsDialog(
        context: Context,
        onDismiss: () -> Unit = {}
    ) = BottomSheetDialog(context).apply {
        val binding = SettingsDestinationBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        val subscriptions = CompositeDisposable()

        subscriptions.add(
            skylightRepository.getSelectedSkylightTypeStream()
                .subscribe { type: SkylightRepository.SkylightType ->
                    when (type) {
                        SkylightRepository.SkylightType.SSO -> binding.networkButton.isChecked = true
                        SkylightRepository.SkylightType.CALCULATOR -> binding.localButton.isChecked = true
                        SkylightRepository.SkylightType.DUMMY -> binding.dummyButton.isChecked = true
                    }
                }
        )

        binding.sourceSelection.setOnCheckedChangeListener { _, checkedId ->
            val selectedType = when (checkedId) {
                R.id.localButton -> SkylightRepository.SkylightType.CALCULATOR
                R.id.dummyButton -> SkylightRepository.SkylightType.DUMMY
                else -> SkylightRepository.SkylightType.SSO
            }
            skylightRepository.selectSkylightType(selectedType)
                .subscribe()
        }

        subscriptions.add(
            themeRepository.getSelectedThemeMode()
                .subscribe { mode: MutableThemeRepository.ThemeMode ->
                    when (mode) {
                        MutableThemeRepository.ThemeMode.SYSTEM -> binding.systemButton.isChecked = true
                        MutableThemeRepository.ThemeMode.SKYLIGHT -> binding.skylightButton.isChecked = true
                        MutableThemeRepository.ThemeMode.LIGHT -> binding.lightButton.isChecked = true
                        MutableThemeRepository.ThemeMode.DARK -> binding.darkButton.isChecked = true
                    }
                }
        )

        binding.themeSelection.setOnCheckedChangeListener { _, checkedId ->
            val selectedDarkMode = when (checkedId) {
                R.id.systemButton -> MutableThemeRepository.ThemeMode.SYSTEM
                R.id.skylightButton -> MutableThemeRepository.ThemeMode.SKYLIGHT
                R.id.lightButton -> MutableThemeRepository.ThemeMode.LIGHT
                R.id.darkButton -> MutableThemeRepository.ThemeMode.DARK
                else -> throw IllegalArgumentException("Unknown checkedId $checkedId")
            }
            themeRepository.selectThemeMode(selectedDarkMode)
                .subscribe()
        }

        binding.version.text = context.getString(R.string.version_info, BuildConfig.VERSION_NAME)

        setOnDismissListener {
            subscriptions.clear()
            onDismiss()
        }
    }
}
