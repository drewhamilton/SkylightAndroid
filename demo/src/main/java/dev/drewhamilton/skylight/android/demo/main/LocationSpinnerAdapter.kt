package dev.drewhamilton.skylight.android.demo.main

import android.content.Context

import android.widget.ArrayAdapter
import dev.drewhamilton.skylight.android.demo.R
import dev.drewhamilton.skylight.android.demo.location.Location

class LocationSpinnerAdapter(context: Context, locations: List<Location>)
    : ArrayAdapter<Location>(context, R.layout.item_spinner, locations)
