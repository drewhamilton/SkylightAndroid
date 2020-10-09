package dev.drewhamilton.skylight.android

import dev.drewhamilton.skylight.fake.FakeSkylight
import java.time.LocalTime
import java.time.ZoneId

// TODO: Move default params to Skylight library
@Suppress("FunctionName") // Factory
internal fun DefaultFakeSkylight(): FakeSkylight = FakeSkylight.Typical(
    zone = ZoneId.systemDefault(),
    dawn = LocalTime.of(7, 0),
    sunrise = LocalTime.of(8, 0),
    sunset = LocalTime.of(21, 0),
    dusk = LocalTime.of(22, 0),
)
