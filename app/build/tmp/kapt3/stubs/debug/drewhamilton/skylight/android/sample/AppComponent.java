package drewhamilton.skylight.android.sample;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bg\u0018\u0000 \b2\u00020\u0001:\u0002\b\tJ\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0007H&\u00a8\u0006\n"}, d2 = {"Ldrewhamilton/skylight/android/sample/AppComponent;", "", "inject", "", "mainActivity", "Ldrewhamilton/skylight/android/sample/main/MainActivity;", "settingsActivity", "Ldrewhamilton/skylight/android/sample/settings/SettingsActivity;", "Companion", "Factory", "app_debug"})
@dagger.Component(modules = {drewhamilton.skylight.android.sample.ApplicationModule.class, drewhamilton.skylight.android.sample.SkylightModule.class})
@javax.inject.Singleton()
public abstract interface AppComponent {
    public static final drewhamilton.skylight.android.sample.AppComponent.Companion Companion = null;
    
    public abstract void inject(@org.jetbrains.annotations.NotNull()
    drewhamilton.skylight.android.sample.main.MainActivity mainActivity);
    
    public abstract void inject(@org.jetbrains.annotations.NotNull()
    drewhamilton.skylight.android.sample.settings.SettingsActivity settingsActivity);
    
    @kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bg\u0018\u00002\u00020\u0001J&\u0010\u0002\u001a\u00020\u00032\b\b\u0001\u0010\u0004\u001a\u00020\u00052\b\b\u0001\u0010\u0006\u001a\u00020\u00072\b\b\u0001\u0010\b\u001a\u00020\tH&\u00a8\u0006\n"}, d2 = {"Ldrewhamilton/skylight/android/sample/AppComponent$Factory;", "", "create", "Ldrewhamilton/skylight/android/sample/AppComponent;", "application", "Landroid/app/Application;", "ssoSkylightComponent", "Ldrewhamilton/skylight/backport/sso/dagger/SsoSkylightComponent;", "dummySkylightComponent", "Ldrewhamilton/skylight/backport/dummy/dagger/DummySkylightComponent;", "app_debug"})
    @dagger.Component.Factory()
    public static abstract interface Factory {
        
        @org.jetbrains.annotations.NotNull()
        public abstract drewhamilton.skylight.android.sample.AppComponent create(@org.jetbrains.annotations.NotNull()
        @dagger.BindsInstance()
        android.app.Application application, @org.jetbrains.annotations.NotNull()
        @dagger.BindsInstance()
        drewhamilton.skylight.backport.sso.dagger.SsoSkylightComponent ssoSkylightComponent, @org.jetbrains.annotations.NotNull()
        @dagger.BindsInstance()
        drewhamilton.skylight.backport.dummy.dagger.DummySkylightComponent dummySkylightComponent);
    }
    
    @kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010R\u0014\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\b\n\u0000\u0012\u0004\b\u0005\u0010\u0002R\u0011\u0010\u0006\u001a\u00020\u00048F\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\b\u00a8\u0006\u0011"}, d2 = {"Ldrewhamilton/skylight/android/sample/AppComponent$Companion;", "", "()V", "_instance", "Ldrewhamilton/skylight/android/sample/AppComponent;", "_instance$annotations", "instance", "getInstance", "()Ldrewhamilton/skylight/android/sample/AppComponent;", "create", "", "application", "Landroid/app/Application;", "ssoSkylightComponent", "Ldrewhamilton/skylight/backport/sso/dagger/SsoSkylightComponent;", "dummySkylightComponent", "Ldrewhamilton/skylight/backport/dummy/dagger/DummySkylightComponent;", "app_debug"})
    public static final class Companion {
        private static drewhamilton.skylight.android.sample.AppComponent _instance;
        
        @org.jetbrains.annotations.NotNull()
        public final drewhamilton.skylight.android.sample.AppComponent getInstance() {
            return null;
        }
        
        @kotlin.Suppress(names = {"ObjectPropertyName"})
        private static void _instance$annotations() {
        }
        
        public final void create(@org.jetbrains.annotations.NotNull()
        android.app.Application application, @org.jetbrains.annotations.NotNull()
        drewhamilton.skylight.backport.sso.dagger.SsoSkylightComponent ssoSkylightComponent, @org.jetbrains.annotations.NotNull()
        drewhamilton.skylight.backport.dummy.dagger.DummySkylightComponent dummySkylightComponent) {
        }
        
        private Companion() {
            super();
        }
    }
}