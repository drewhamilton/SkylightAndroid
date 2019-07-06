package drewhamilton.skylight.android.sample

import dagger.Module
import dagger.Provides
import dagger.Reusable
import drewhamilton.skylight.backport.Skylight
import drewhamilton.skylight.backport.calculator.CalculatorSkylight
import drewhamilton.skylight.backport.dummy.DummySkylight
import drewhamilton.skylight.backport.dummy.dagger.DummySkylightComponent
import drewhamilton.skylight.backport.sso.SsoSkylight
import drewhamilton.skylight.backport.sso.dagger.SsoSkylightComponent
import drewhamilton.skylight.android.sample.source.SkylightRepository

@Module
object SkylightModule {

    @JvmStatic
    @Provides
    @Reusable
    fun ssoSkylight(ssoSkylightComponent: SsoSkylightComponent) = ssoSkylightComponent.skylight()

    @JvmStatic
    @Provides
    @Reusable
    fun dummySkylight(dummySkylightComponent: DummySkylightComponent) = dummySkylightComponent.skylight()

    @JvmStatic
    @Provides
    fun skylight(
        skylightRepository: SkylightRepository,
        ssoSkylight: SsoSkylight,
        calculatorSkylight: CalculatorSkylight,
        dummySkylight: DummySkylight
    ): Skylight = when (skylightRepository.getSelectedSkylightTypeOnce().blockingGet()!!) {
        SkylightRepository.SkylightType.SSO -> ssoSkylight
        SkylightRepository.SkylightType.CALCULATOR -> calculatorSkylight
        SkylightRepository.SkylightType.DUMMY -> dummySkylight
    }
}
