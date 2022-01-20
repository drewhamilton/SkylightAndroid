package dev.drewhamilton.skylight.android.demo.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import dev.drewhamilton.skylight.Skylight
import dev.drewhamilton.skylight.SkylightDay
import dev.drewhamilton.skylight.android.AppCompatDelegateDarkModeApplicator
import dev.drewhamilton.skylight.android.DarkModeApplicator
import dev.drewhamilton.skylight.android.DarkModeLifecycleObserver
import dev.drewhamilton.skylight.android.SkylightForCoordinatesFactory
import dev.drewhamilton.skylight.android.demo.AppComponent
import dev.drewhamilton.skylight.android.demo.R
import dev.drewhamilton.skylight.android.demo.databinding.MainDestinationBinding
import dev.drewhamilton.skylight.android.demo.location.Location
import dev.drewhamilton.skylight.android.demo.location.LocationRepository
import dev.drewhamilton.skylight.android.demo.settings.SettingsDialogFactory
import dev.drewhamilton.skylight.android.demo.source.SkylightRepository
import dev.drewhamilton.skylight.android.demo.theme.ThemeRepository
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext

@Suppress("ProtectedInFinal")
class MainActivity : AppCompatActivity() {

    private val binding: MainDestinationBinding by lazy(mode = LazyThreadSafetyMode.NONE) {
        MainDestinationBinding.inflate(layoutInflater)
    }

    @Inject protected lateinit var themeRepository: ThemeRepository
    @Inject protected lateinit var skylightRepository: SkylightRepository
    @Inject protected lateinit var locationRepository: LocationRepository
    @Inject protected lateinit var skylightForCoordinatesFactory: SkylightForCoordinatesFactory

    @Inject protected lateinit var settingsDialogFactory: SettingsDialogFactory
    private var displayedSettingsDialog: BottomSheetDialog? = null

    private val skylight: Skylight
        get() = AppComponent.instance.skylight()

    private val darkModeApplicator: DarkModeApplicator by lazy(LazyThreadSafetyMode.NONE) {
        AppCompatDelegateDarkModeApplicator(delegate)
    }

    private lateinit var themeMode: ThemeRepository.ThemeMode
    private var darkModeLifecycleObserver: DarkModeLifecycleObserver? = null
        private set(value) {
            field?.let {
                it.cancel()
                lifecycle.removeObserver(it)
            }
            field = value
            value?.let { lifecycle.addObserver(it) }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppComponent.instance.inject(this)

        setContentView(binding.root)

        initializeMenu()
        initializeLocationOptions()

        lifecycleScope.launchWhenCreated {
            themeRepository.getSelectedThemeModeFlow().collect { themeMode ->
                applyThemeMode(themeMode)
            }
        }

        lifecycleScope.launchWhenCreated {
            skylightRepository.getSelectedSkylightTypeFlow()
                .distinctUntilChanged()
                .collect {
                    (binding.locationSelector.selectedItem as Location).display()
                }
        }

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_REQUEST
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST -> {
                if (
                    permissions.containsAny(
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
                    ) && grantResults.contains(PackageManager.PERMISSION_GRANTED)
                ) {
                    applyThemeMode(themeMode)
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onDestroy() {
        displayedSettingsDialog?.dismiss()
        super.onDestroy()
    }

    @Suppress("SameParameterValue")
    private fun <T> Array<out T>.containsAny(vararg elements: T): Boolean {
        elements.forEach {
            if (this.contains(it))
                return true
        }
        return false
    }

    private fun initializeLocationOptions() {
        val locationOptions = locationRepository.getLocationOptions()
        binding.locationSelector.adapter = LocationSpinnerAdapter(this, locationOptions)

        binding.locationSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
                locationOptions[position].display()

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        binding.locationSelector.setSelection(0)
    }

    private fun Location.display() {
        lifecycleScope.launchWhenCreated {
            val skylightDay = withContext(Dispatchers.IO) {
                skylight.getSkylightDay(coordinates, LocalDate.now())
            }
            skylightDay.display(timeZone)
        }
    }

    private fun SkylightDay.display(timeZone: ZoneId) {
        var dawnDateTime: ZonedDateTime? = null
        var sunriseDateTime: ZonedDateTime? = null
        var sunsetDateTime: ZonedDateTime? = null
        var duskDateTime: ZonedDateTime? = null

        if (this is SkylightDay.Typical) {
            dawnDateTime = dawn?.atZone(timeZone)
            sunriseDateTime = sunrise?.atZone(timeZone)
            sunsetDateTime = sunset?.atZone(timeZone)
            duskDateTime = dusk?.atZone(timeZone)
        }

        binding.dawnTime.setTime(dawnDateTime, R.string.never)
        binding.sunriseTime.setTime(sunriseDateTime, R.string.never)
        binding.sunsetTime.setTime(sunsetDateTime, R.string.never)
        binding.duskTime.setTime(duskDateTime, R.string.never)

        binding.dawnLabel.visibility = View.VISIBLE
        binding.sunriseLabel.visibility = View.VISIBLE
        binding.sunsetLabel.visibility = View.VISIBLE
        binding.duskLabel.visibility = View.VISIBLE
    }

    private fun TextView.setTime(time: ZonedDateTime?, @StringRes fallback: Int) =
        setTime(time, fallback = context.getString(fallback))

    private fun TextView.setTime(
        time: ZonedDateTime?,
        formatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT),
        fallback: CharSequence = ""
    ) {
        if (time == null) {
            hint = fallback
            text = ""
        } else {
            text = formatter.format(time)
            hint = ""
        }
    }

    private fun initializeMenu() {
        val settingsItem = binding.toolbar.menu.findItem(R.id.settings)
        settingsItem.setOnMenuItemClickListener {
            displaySettingsDialog()
            true
        }
    }

    private fun displaySettingsDialog() {
        val settingsDialog = settingsDialogFactory.createSettingsDialog(this) {
            displayedSettingsDialog = null
        }
        settingsDialog.show()
        displayedSettingsDialog = settingsDialog
    }

    private fun applyThemeMode(mode: ThemeRepository.ThemeMode) {
        themeMode = mode
        darkModeLifecycleObserver = when (mode) {
            ThemeRepository.ThemeMode.SKYLIGHT -> DarkModeLifecycleObserver.OfSkylightForCoordinates(
                skylightForCoordinatesFactory.createForLocation(this),
                darkModeApplicator
            )
            ThemeRepository.ThemeMode.SYSTEM -> DarkModeLifecycleObserver.Constant(
                DarkModeApplicator.DarkMode.FOLLOW_SYSTEM,
                darkModeApplicator
            )
            ThemeRepository.ThemeMode.LIGHT -> DarkModeLifecycleObserver.Constant(
                DarkModeApplicator.DarkMode.LIGHT,
                darkModeApplicator
            )
            ThemeRepository.ThemeMode.DARK -> DarkModeLifecycleObserver.Constant(
                DarkModeApplicator.DarkMode.DARK,
                darkModeApplicator
            )
        }
    }

    private companion object {
        private const val LOCATION_REQUEST = 9
    }
}
