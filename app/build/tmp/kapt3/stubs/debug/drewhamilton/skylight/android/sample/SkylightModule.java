package drewhamilton.skylight.android.sample;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J(\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0003\u001a\u00020\u0004H\u0007J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u0010H\u0007\u00a8\u0006\u0011"}, d2 = {"Ldrewhamilton/skylight/android/sample/SkylightModule;", "", "()V", "dummySkylight", "Ldrewhamilton/skylight/backport/dummy/DummySkylight;", "dummySkylightComponent", "Ldrewhamilton/skylight/backport/dummy/dagger/DummySkylightComponent;", "skylight", "Ldrewhamilton/skylight/backport/Skylight;", "skylightRepository", "Ldrewhamilton/skylight/android/sample/source/SkylightRepository;", "ssoSkylight", "Ldrewhamilton/skylight/backport/sso/SsoSkylight;", "calculatorSkylight", "Ldrewhamilton/skylight/backport/calculator/CalculatorSkylight;", "ssoSkylightComponent", "Ldrewhamilton/skylight/backport/sso/dagger/SsoSkylightComponent;", "app_debug"})
@dagger.Module()
public final class SkylightModule {
    public static final drewhamilton.skylight.android.sample.SkylightModule INSTANCE = null;
    
    @org.jetbrains.annotations.NotNull()
    @dagger.Reusable()
    @dagger.Provides()
    public static final drewhamilton.skylight.backport.sso.SsoSkylight ssoSkylight(@org.jetbrains.annotations.NotNull()
    drewhamilton.skylight.backport.sso.dagger.SsoSkylightComponent ssoSkylightComponent) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @dagger.Reusable()
    @dagger.Provides()
    public static final drewhamilton.skylight.backport.dummy.DummySkylight dummySkylight(@org.jetbrains.annotations.NotNull()
    drewhamilton.skylight.backport.dummy.dagger.DummySkylightComponent dummySkylightComponent) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @dagger.Provides()
    public static final drewhamilton.skylight.backport.Skylight skylight(@org.jetbrains.annotations.NotNull()
    drewhamilton.skylight.android.sample.source.SkylightRepository skylightRepository, @org.jetbrains.annotations.NotNull()
    drewhamilton.skylight.backport.sso.SsoSkylight ssoSkylight, @org.jetbrains.annotations.NotNull()
    drewhamilton.skylight.backport.calculator.CalculatorSkylight calculatorSkylight, @org.jetbrains.annotations.NotNull()
    drewhamilton.skylight.backport.dummy.DummySkylight dummySkylight) {
        return null;
    }
    
    private SkylightModule() {
        super();
    }
}