package dev.drewhamilton.skylight.android.demo.main

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.color.DynamicColors
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
        WindowCompat.setDecorFitsSystemWindows(window, false)
        DynamicColors.applyIfAvailable(this)

        super.onCreate(savedInstanceState)
        AppComponent.instance.inject(this)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, windowInsets ->
            with(windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())) {
                binding.appBarLayout.updatePadding(top = top)
                binding.root.updatePadding(left = left, right = right)
                binding.list.updatePadding(bottom = bottom)
            }

            windowInsets
        }

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

        @SuppressLint("MissingPermission")
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)) {
                onCoarseLocationGranted()
            }
        }
        locationPermissionRequest.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    @RequiresPermission("android.permission.ACCESS_COARSE_LOCATION")
    private fun onCoarseLocationGranted() {
        applyThemeMode(themeMode)

        val locationClient = LocationServices.getFusedLocationProviderClient(this@MainActivity)
        val locationTask = locationClient.getCurrentLocation(
            LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY,
            CancellationTokenSource().token
        )
        locationTask.addOnSuccessListener(this@MainActivity) { location ->
            Log.d("MainActivity", "Location found: $location")
        }
        locationTask.addOnCanceledListener(this) {
            Log.w("MainActivity", "Location request canceled")
        }
    }

    override fun onDestroy() {
        displayedSettingsDialog?.dismiss()
        super.onDestroy()
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
}
