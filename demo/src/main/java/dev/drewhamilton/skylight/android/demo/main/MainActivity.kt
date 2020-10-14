package dev.drewhamilton.skylight.android.demo.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import dev.drewhamilton.skylight.Coordinates
import dev.drewhamilton.skylight.Skylight
import dev.drewhamilton.skylight.SkylightDay
import dev.drewhamilton.skylight.android.AppCompatDelegateDarkModeApplicator
import dev.drewhamilton.skylight.android.DarkModeApplicator
import dev.drewhamilton.skylight.android.DarkModeLifecycleObserver
import dev.drewhamilton.skylight.android.SkylightForCoordinatesFactory
import dev.drewhamilton.skylight.android.demo.AppComponent
import dev.drewhamilton.skylight.android.demo.BuildConfig
import dev.drewhamilton.skylight.android.demo.R
import dev.drewhamilton.skylight.android.demo.databinding.MainDestinationBinding
import dev.drewhamilton.skylight.android.demo.location.Location
import dev.drewhamilton.skylight.android.demo.location.LocationRepository
import dev.drewhamilton.skylight.android.demo.rx.ui.RxActivity
import dev.drewhamilton.skylight.android.demo.settings.SettingsActivity
import dev.drewhamilton.skylight.android.demo.theme.MutableThemeRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import javax.inject.Inject

@Suppress("ProtectedInFinal")
class MainActivity : RxActivity() {

    private val binding: MainDestinationBinding by lazy(mode = LazyThreadSafetyMode.NONE) {
        MainDestinationBinding.inflate(layoutInflater)
    }

    @Inject protected lateinit var locationRepository: LocationRepository

    @Inject protected lateinit var themeRepository: MutableThemeRepository

    @Inject protected lateinit var skylight: Skylight

    @Inject protected lateinit var skylightForCoordinatesFactory: SkylightForCoordinatesFactory

    private val darkModeApplicator: DarkModeApplicator by lazy(LazyThreadSafetyMode.NONE) {
        AppCompatDelegateDarkModeApplicator(delegate)
    }

    private lateinit var themeMode: MutableThemeRepository.ThemeMode
    private var darkModeLifecycleObserver: DarkModeLifecycleObserver? = null
        private set(value) {
            field?.let { lifecycle.removeObserver(it) }
            field = value
            value?.let { lifecycle.addObserver(it) }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= 29) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }

        super.onCreate(savedInstanceState)
        AppComponent.instance.inject(this)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, windowInsets ->
            with(windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())) {
                binding.toolbar.updatePadding(top = top)
                binding.version.updatePadding(bottom = bottom)
                binding.root.updatePadding(left = left, right = right)
            }

            windowInsets
        }

        val backgroundGradient = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(
                ContextCompat.getColor(this, R.color.skylight_dawn),
                ContextCompat.getColor(this, R.color.skylight_sunrise),
                ContextCompat.getColor(this, R.color.skylight_sunset),
                ContextCompat.getColor(this, R.color.skylight_dusk),
            )
        )
        ViewCompat.setBackground(binding.background, backgroundGradient)

        binding.version.text = getString(R.string.version_info, BuildConfig.VERSION_NAME)
        initializeMenu()
        initializeLocationOptions()

        themeRepository.getSelectedThemeMode()
            .subscribe { themeMode: MutableThemeRepository.ThemeMode -> applyThemeMode(themeMode) }
            .untilDestroy()

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_REQUEST
        )
    }

    override fun onResume() {
        super.onResume()
        // Inject again in onResume because settings activity may change what is injected:
        AppComponent.instance.inject(this)

        // Re-display selected item in case something has changed:
        (binding.locationSelector.selectedItem as Location).display()
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
        skylight.getSkylightDaySingle(coordinates, LocalDate.now())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer { it.display(timeZone) })
            .untilDestroy()
    }

    // FIXME: This crashes if the Activity is destroyed while the network call is in progress
    private fun Skylight.getSkylightDaySingle(coordinates: Coordinates, date: LocalDate) = Single.fromCallable {
        getSkylightDay(coordinates, date)
    }

    private fun SkylightDay.display(timeZone: ZoneId) {
        var dawnDateTime: ZonedDateTime? = null
        var sunriseDateTime: ZonedDateTime? = null
        var sunsetDateTime: ZonedDateTime? = null
        var duskDateTime: ZonedDateTime? = null

        when (this) {
            is SkylightDay.Typical -> {
                dawnDateTime = dawn?.atZone(timeZone)
                sunriseDateTime = sunrise?.atZone(timeZone)
                sunsetDateTime = sunset?.atZone(timeZone)
                duskDateTime = dusk?.atZone(timeZone)
            }
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
            startActivity(Intent(this, SettingsActivity::class.java))
            true
        }
    }

    private fun applyThemeMode(mode: MutableThemeRepository.ThemeMode) {
        themeMode = mode
        darkModeLifecycleObserver = when (mode) {
            MutableThemeRepository.ThemeMode.SKYLIGHT -> DarkModeLifecycleObserver.Skylight(
                darkModeApplicator,
                skylightForCoordinatesFactory.createForLocation(this)
            )
            MutableThemeRepository.ThemeMode.SYSTEM -> DarkModeLifecycleObserver.Constant(
                darkModeApplicator,
                DarkModeApplicator.DarkMode.FOLLOW_SYSTEM
            )
            MutableThemeRepository.ThemeMode.LIGHT -> DarkModeLifecycleObserver.Constant(
                darkModeApplicator,
                DarkModeApplicator.DarkMode.LIGHT
            )
            MutableThemeRepository.ThemeMode.DARK -> DarkModeLifecycleObserver.Constant(
                darkModeApplicator,
                DarkModeApplicator.DarkMode.DARK
            )
        }
    }

    private companion object {
        private const val LOCATION_REQUEST = 9
    }
}
