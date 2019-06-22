package drewhamilton.skylight.android.sample.main;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0002J\b\u0010\u0013\u001a\u00020\u0012H\u0002J\u0012\u0010\u0014\u001a\u00020\u00122\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0014J\b\u0010\u0017\u001a\u00020\u0012H\u0014J\f\u0010\u0018\u001a\u00020\u0012*\u00020\u0019H\u0002J\u0014\u0010\u0018\u001a\u00020\u0012*\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J\u0014\u0010\u001d\u001a\u00020\u001e*\u00020\u001e2\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J\u0014\u0010\u001f\u001a\u00020\u0012*\u00020 2\u0006\u0010\u001b\u001a\u00020\u001cH\u0002R$\u0010\u0003\u001a\u00020\u00048\u0004@\u0004X\u0085.\u00a2\u0006\u0014\n\u0000\u0012\u0004\b\u0005\u0010\u0002\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR$\u0010\n\u001a\u00020\u000b8\u0004@\u0004X\u0085.\u00a2\u0006\u0014\n\u0000\u0012\u0004\b\f\u0010\u0002\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010\u00a8\u0006!"}, d2 = {"Ldrewhamilton/skylight/android/sample/main/MainActivity;", "Ldrewhamilton/skylight/android/sample/rx/ui/RxActivity;", "()V", "locationRepository", "Ldrewhamilton/skylight/android/sample/location/LocationRepository;", "locationRepository$annotations", "getLocationRepository", "()Ldrewhamilton/skylight/android/sample/location/LocationRepository;", "setLocationRepository", "(Ldrewhamilton/skylight/android/sample/location/LocationRepository;)V", "skylight", "Ldrewhamilton/skylight/backport/Skylight;", "skylight$annotations", "getSkylight", "()Ldrewhamilton/skylight/backport/Skylight;", "setSkylight", "(Ldrewhamilton/skylight/backport/Skylight;)V", "initializeLocationOptions", "", "initializeMenu", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onResume", "display", "Ldrewhamilton/skylight/android/sample/location/Location;", "Ldrewhamilton/skylight/backport/SkylightDay;", "timeZone", "Lorg/threeten/bp/ZoneId;", "inTimeZone", "Lorg/threeten/bp/OffsetTime;", "showDetailsOnClick", "Ldrewhamilton/skylight/views/event/SkylightEventView;", "app_debug"})
public final class MainActivity extends drewhamilton.skylight.android.sample.rx.ui.RxActivity {
    @org.jetbrains.annotations.NotNull()
    @javax.inject.Inject()
    protected drewhamilton.skylight.android.sample.location.LocationRepository locationRepository;
    @org.jetbrains.annotations.NotNull()
    @javax.inject.Inject()
    protected drewhamilton.skylight.backport.Skylight skylight;
    private java.util.HashMap _$_findViewCache;
    
    @kotlin.Suppress(names = {"ProtectedInFinal"})
    protected static void locationRepository$annotations() {
    }
    
    @org.jetbrains.annotations.NotNull()
    protected final drewhamilton.skylight.android.sample.location.LocationRepository getLocationRepository() {
        return null;
    }
    
    protected final void setLocationRepository(@org.jetbrains.annotations.NotNull()
    drewhamilton.skylight.android.sample.location.LocationRepository p0) {
    }
    
    @kotlin.Suppress(names = {"ProtectedInFinal"})
    protected static void skylight$annotations() {
    }
    
    @org.jetbrains.annotations.NotNull()
    protected final drewhamilton.skylight.backport.Skylight getSkylight() {
        return null;
    }
    
    protected final void setSkylight(@org.jetbrains.annotations.NotNull()
    drewhamilton.skylight.backport.Skylight p0) {
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    protected void onResume() {
    }
    
    private final void initializeLocationOptions() {
    }
    
    private final void display(@org.jetbrains.annotations.NotNull()
    drewhamilton.skylight.android.sample.location.Location $this$display) {
    }
    
    private final void display(@org.jetbrains.annotations.NotNull()
    drewhamilton.skylight.backport.SkylightDay $this$display, org.threeten.bp.ZoneId timeZone) {
    }
    
    private final org.threeten.bp.OffsetTime inTimeZone(@org.jetbrains.annotations.NotNull()
    org.threeten.bp.OffsetTime $this$inTimeZone, org.threeten.bp.ZoneId timeZone) {
        return null;
    }
    
    private final void showDetailsOnClick(@org.jetbrains.annotations.NotNull()
    drewhamilton.skylight.views.event.SkylightEventView $this$showDetailsOnClick, org.threeten.bp.ZoneId timeZone) {
    }
    
    private final void initializeMenu() {
    }
    
    public MainActivity() {
        super();
    }
}