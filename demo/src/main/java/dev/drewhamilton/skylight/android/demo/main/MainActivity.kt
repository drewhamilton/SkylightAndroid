package dev.drewhamilton.skylight.android.demo.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
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
import java.time.Instant
import java.time.LocalDate
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

    private val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

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
                    binding.cityCardAdapter.displayLocations()
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
        val adapter = CityCardAdapter().also {
            binding.list.adapter = it
        }

        adapter.displayLocations()
    }

    private val MainDestinationBinding.cityCardAdapter: CityCardAdapter
        get() = list.adapter as CityCardAdapter

    private fun CityCardAdapter.displayLocations() {
        val locations = locationRepository.getLocationOptions()
        lifecycleScope.launchWhenCreated {
            val today = LocalDate.now()
            val data: List<CityCardAdapter.Data> = List(locations.size) { i ->
                val location = locations[i]
                val skylightDay = withContext(Dispatchers.IO) {
                    skylight.getSkylightDay(location.coordinates, today)
                }
                when (skylightDay) {
                    is SkylightDay.Typical -> CityCardAdapter.Data(
                        cityName = location.longDisplayName,
                        dawn = skylightDay.dawn.forDisplay(location),
                        sunrise = skylightDay.sunrise.forDisplay(location),
                        sunset = skylightDay.sunset.forDisplay(location),
                        dusk = skylightDay.dusk.forDisplay(location),
                    )
                    else -> CityCardAdapter.Data(
                        cityName = location.longDisplayName,
                        dawn = "Never",
                        sunrise = "Never",
                        sunset = "Never",
                        dusk = "Never",
                    )
                }
            }
            submitList(data)
        }
    }

    private fun Instant?.forDisplay(location: Location): String {
        return this?.atZone(location.timeZone)?.let {
            timeFormatter.format(it)
        } ?: "Never"
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
