package dev.drewhamilton.skylight.android.demo.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.core.app.ActivityCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
import dev.drewhamilton.skylight.android.demo.location.Location
import dev.drewhamilton.skylight.android.demo.location.LocationRepository
import dev.drewhamilton.skylight.android.demo.rx.ui.RxActivity
import dev.drewhamilton.skylight.android.demo.settings.SettingsActivity
import dev.drewhamilton.skylight.android.demo.theme.MutableThemeRepository
import dev.drewhamilton.skylight.android.views.event.SkylightEventView
import dev.drewhamilton.skylight.android.views.event.setTime
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject
import kotlinx.android.synthetic.main.main_destination.dawn
import kotlinx.android.synthetic.main.main_destination.dusk
import kotlinx.android.synthetic.main.main_destination.locationSelector
import kotlinx.android.synthetic.main.main_destination.sunrise
import kotlinx.android.synthetic.main.main_destination.sunset
import kotlinx.android.synthetic.main.main_destination.toolbar
import kotlinx.android.synthetic.main.main_destination.version

@Suppress("ProtectedInFinal")
class MainActivity : RxActivity() {

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
        super.onCreate(savedInstanceState)
        AppComponent.instance.inject(this)

        setContentView(R.layout.main_destination)
        version.text = getString(R.string.version_info, BuildConfig.VERSION_NAME)
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
        (locationSelector.selectedItem as Location).display()
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
        locationSelector.adapter = LocationSpinnerAdapter(this, locationOptions)

        locationSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
                locationOptions[position].display()

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        locationSelector.setSelection(0)
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

        dawn.setTime(dawnDateTime, R.string.never)
        sunrise.setTime(sunriseDateTime, R.string.never)
        sunset.setTime(sunsetDateTime, R.string.never)
        dusk.setTime(duskDateTime, R.string.never)

        dawn.showDetailsOnClick(timeZone)
        sunrise.showDetailsOnClick(timeZone)
        sunset.showDetailsOnClick(timeZone)
        dusk.showDetailsOnClick(timeZone)
    }

    private fun SkylightEventView.showDetailsOnClick(timeZone: ZoneId) {
        val clickListener: View.OnClickListener? = if (timeText.isNotEmpty())
            View.OnClickListener {
                MaterialAlertDialogBuilder(this@MainActivity)
                    .setTitle(labelText)
                    .setMessage("$timeText, ${timeZone.getDisplayName(TextStyle.FULL, Locale.getDefault())}")
                    .setPositiveButton(R.string.good_to_know) { _, _ -> }
                    .show()
            }
        else
            null
        setOnClickListener(clickListener)
    }

    private fun initializeMenu() {
        val settingsItem = toolbar.menu.findItem(R.id.settings)
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
        const val LOCATION_REQUEST = 9
    }
}
