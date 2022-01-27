package dev.drewhamilton.skylight.android.demo.location

import android.app.Application
import android.location.Geocoder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.drewhamilton.skylight.Coordinates
import java.time.ZoneId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocationRepository(
    application: Application,
    includeSvalbard: Boolean,
) : AndroidViewModel(application) {

    @Suppress("unused") // ViewModel reflective instantiation
    constructor(application: Application) : this(application, includeSvalbard = false)

    val locationsFlow: StateFlow<List<DisplayedLocation>>
        get() = _locationsFlow

    private val _locationsFlow = MutableStateFlow(
        value = ExampleLocation.values().map { it.toLocation(application) }.let { exampleList ->
            if (includeSvalbard)
                exampleList + DisplayedLocation("Svalbard, Norway", ZoneId.of("Europe/Oslo"), Coordinates(77.8750, 20.9752))
            else
                exampleList
        }
    )

    private val geocoder = Geocoder(application)

    fun onCurrentLocationDetermined(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val address = withContext(Dispatchers.Default) {
                @Suppress("BlockingMethodInNonBlockingContext")
                geocoder.getFromLocation(latitude, longitude, 1).firstOrNull()
            }
            // TODO: Fallback digit limit
            val name = address?.locality ?: "$latitude, $longitude"

            val location = DisplayedLocation(
                longDisplayName = name,
                timeZone = ZoneId.systemDefault(),
                coordinates = Coordinates(latitude, longitude),
                isHighlighted = true
            )
            _locationsFlow.value = _locationsFlow.value.toMutableList().apply {
                if (first().isHighlighted) {
                    removeFirst()
                }
                add(0, location)
            }
        }
    }
}
