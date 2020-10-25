package dev.drewhamilton.skylight.android.demo

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dev.drewhamilton.skylight.Skylight
import dev.drewhamilton.skylight.android.SkylightForCoordinatesFactory
import dev.drewhamilton.skylight.android.SkylightForMostRecentCoordinatesFactory
import dev.drewhamilton.skylight.android.demo.source.SkylightRepository
import dev.drewhamilton.skylight.calculator.CalculatorSkylight
import dev.drewhamilton.skylight.fake.FakeSkylight
import dev.drewhamilton.skylight.sunrise_sunset_org.SunriseSunsetOrgSkylight
import java.time.LocalTime
import java.time.ZoneId
import okhttp3.OkHttpClient

@Module
object SkylightModule {

    @Provides
    @Reusable
    fun sunriseSunsetOrgSkylight(): SunriseSunsetOrgSkylight = SunriseSunsetOrgSkylight(OkHttpClient())

    @Provides
    @Reusable
    fun fakeSkylight(): FakeSkylight = FakeSkylight.Typical(
        zone = ZoneId.systemDefault(),
        dawn = LocalTime.now().plusSeconds(5),
        sunrise = null, sunset = null,
        dusk = LocalTime.now().plusSeconds(10)
    )

    @Provides
    @Reusable
    fun calculatorSkylight(): CalculatorSkylight = CalculatorSkylight()

    @Provides
    fun skylight(
        skylightRepository: SkylightRepository,
        ssoSkylight: SunriseSunsetOrgSkylight,
        calculatorSkylight: CalculatorSkylight,
        fakeSkylight: FakeSkylight
    ): Skylight = when (skylightRepository.getSelectedSkylightTypeOnce().blockingGet()!!) {
        SkylightRepository.SkylightType.SSO -> ssoSkylight
        SkylightRepository.SkylightType.CALCULATOR -> calculatorSkylight
        SkylightRepository.SkylightType.DUMMY -> fakeSkylight
    }

    @Provides
    fun skylightForCoordinatesFactory(
        skylight: Skylight,
        fallbackSkylight: FakeSkylight
    ): SkylightForCoordinatesFactory = SkylightForMostRecentCoordinatesFactory(skylight, fallbackSkylight)
}
