package drewhamilton.skylight.android.sample.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import drewhamilton.skylight.Coordinates
import drewhamilton.skylight.Skylight
import drewhamilton.skylight.SkylightDay
import drewhamilton.skylight.android.nightmode.AutoNightDelegate
import drewhamilton.skylight.android.sample.AppComponent
import drewhamilton.skylight.android.sample.BuildConfig
import drewhamilton.skylight.android.sample.R
import drewhamilton.skylight.android.sample.location.Location
import drewhamilton.skylight.android.sample.location.LocationRepository
import drewhamilton.skylight.android.sample.rx.ui.RxActivity
import drewhamilton.skylight.android.sample.settings.SettingsActivity
import drewhamilton.skylight.android.sample.styles.StylesActivity
import drewhamilton.skylight.android.views.event.SkylightEventView
import drewhamilton.skylight.android.views.event.setTime
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.main_destination.dawn
import kotlinx.android.synthetic.main.main_destination.dusk
import kotlinx.android.synthetic.main.main_destination.locationSelector
import kotlinx.android.synthetic.main.main_destination.sunrise
import kotlinx.android.synthetic.main.main_destination.sunset
import kotlinx.android.synthetic.main.main_destination.toolbar
import kotlinx.android.synthetic.main.main_destination.version
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

class MainActivity : RxActivity() {

    @Suppress("ProtectedInFinal")
    @Inject protected lateinit var locationRepository: LocationRepository

    private val autoNightDelegate: AutoNightDelegate by lazy {
        AutoNightDelegate.fallback(delegate)
    }

    @Suppress("ProtectedInFinal")
    @Inject protected lateinit var skylight: Skylight

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Restore use of autoNightDelegate
//        lifecycle.addObserver(autoNightDelegate)
        setContentView(R.layout.main_destination)
        version.text = getString(R.string.version_info, BuildConfig.VERSION_NAME)
        initializeMenu()

        AppComponent.instance.inject(this)
        initializeLocationOptions()
    }

    override fun onResume() {
        super.onResume()
        // Inject again in onResume because settings activity may change what is injected:
        AppComponent.instance.inject(this)

        // Re-display selected item in case something has changed:
        (locationSelector.selectedItem as Location).display()
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
                dawnDateTime = dawn?.withZoneSameInstant(timeZone)
                sunriseDateTime = sunrise?.withZoneSameInstant(timeZone)
                sunsetDateTime = sunset?.withZoneSameInstant(timeZone)
                duskDateTime = dusk?.withZoneSameInstant(timeZone)
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
        val stylesItem = toolbar.menu.findItem(R.id.styles)
        stylesItem.setOnMenuItemClickListener {
            startActivity(Intent(this, StylesActivity::class.java))
            true
        }

        val settingsItem = toolbar.menu.findItem(R.id.settings)
        settingsItem.setOnMenuItemClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            true
        }
    }
}
