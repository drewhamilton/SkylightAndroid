package dev.drewhamilton.skylight.android.demo.settings

import android.content.Context
import android.view.LayoutInflater
import com.google.android.material.bottomsheet.BottomSheetDialog
import dev.drewhamilton.skylight.android.demo.BuildConfig
import dev.drewhamilton.skylight.android.demo.R
import dev.drewhamilton.skylight.android.demo.databinding.SettingsDestinationBinding
import dev.drewhamilton.skylight.android.demo.source.SkylightRepository
import dev.drewhamilton.skylight.android.demo.theme.ThemeRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SettingsDialogFactory @Inject constructor(
    private val skylightRepository: SkylightRepository,
    private val themeRepository: ThemeRepository,
) {
    fun createSettingsDialog(
        context: Context,
        onDismiss: () -> Unit = {}
    ) = BottomSheetDialog(context).apply {
        val binding = SettingsDestinationBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        val coroutineScope = CoroutineScope(Dispatchers.Main)

        coroutineScope.launch {
            skylightRepository.getSelectedSkylightTypeFlow().collect { type ->
                when (type) {
                    SkylightRepository.SkylightType.SSO -> binding.networkButton.isChecked = true
                    SkylightRepository.SkylightType.CALCULATOR -> binding.localButton.isChecked = true
                    SkylightRepository.SkylightType.FAKE -> binding.dummyButton.isChecked = true
                }
            }
        }

        binding.sourceSelection.setOnCheckedChangeListener { _, checkedId ->
            val selectedType = when (checkedId) {
                R.id.localButton -> SkylightRepository.SkylightType.CALCULATOR
                R.id.dummyButton -> SkylightRepository.SkylightType.FAKE
                else -> SkylightRepository.SkylightType.SSO
            }
            coroutineScope.launch {
                val previousType = skylightRepository.getSelectedSkylightType()
                if (previousType != selectedType) {
                    skylightRepository.selectSkylightType(selectedType)
                    dismiss()
                }
            }
        }

        coroutineScope.launch {
            themeRepository.getSelectedThemeModeFlow().collect { mode ->
                when (mode) {
                    ThemeRepository.ThemeMode.SYSTEM -> binding.systemButton.isChecked = true
                    ThemeRepository.ThemeMode.SKYLIGHT -> binding.skylightButton.isChecked = true
                    ThemeRepository.ThemeMode.LIGHT -> binding.lightButton.isChecked = true
                    ThemeRepository.ThemeMode.DARK -> binding.darkButton.isChecked = true
                }
            }
        }

        binding.themeSelection.setOnCheckedChangeListener { _, checkedId ->
            val selectedThemeMode = when (checkedId) {
                R.id.systemButton -> ThemeRepository.ThemeMode.SYSTEM
                R.id.skylightButton -> ThemeRepository.ThemeMode.SKYLIGHT
                R.id.lightButton -> ThemeRepository.ThemeMode.LIGHT
                R.id.darkButton -> ThemeRepository.ThemeMode.DARK
                else -> throw IllegalArgumentException("Unknown checkedId $checkedId")
            }
            coroutineScope.launch {
                val previousThemeMode = themeRepository.getSelectedThemeMode()
                if (previousThemeMode != selectedThemeMode) {
                    themeRepository.selectThemeMode(selectedThemeMode)
                    dismiss()
                }
            }
        }

        binding.version.text = context.getString(R.string.version_info, BuildConfig.VERSION_NAME)

        setOnDismissListener {
            coroutineScope.cancel()
            onDismiss()
        }
    }
}
