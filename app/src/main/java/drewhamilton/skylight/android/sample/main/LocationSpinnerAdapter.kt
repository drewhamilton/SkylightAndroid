package drewhamilton.skylight.android.sample.main

import android.content.Context

import android.widget.ArrayAdapter
import drewhamilton.skylight.android.sample.R
import drewhamilton.skylight.android.sample.location.Location

class LocationSpinnerAdapter(context: Context, locations: List<Location>)
    : ArrayAdapter<Location>(context, R.layout.item_spinner, locations)
