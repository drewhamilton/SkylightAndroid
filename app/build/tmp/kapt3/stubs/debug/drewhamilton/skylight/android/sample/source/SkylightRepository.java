package drewhamilton.skylight.android.sample.source;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0017\u0018\u00002\u00020\u0001:\u0003\f\r\u000eB\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bJ\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\u000bR\u0014\u0010\u0002\u001a\u00020\u0003X\u0084\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u000f"}, d2 = {"Ldrewhamilton/skylight/android/sample/source/SkylightRepository;", "", "preferences", "Ldrewhamilton/rxpreferences/RxPreferences;", "(Ldrewhamilton/rxpreferences/RxPreferences;)V", "getPreferences", "()Ldrewhamilton/rxpreferences/RxPreferences;", "getSelectedSkylightType", "Lio/reactivex/Single;", "Ldrewhamilton/skylight/android/sample/source/SkylightRepository$SkylightType;", "observeSelectedSkylightType", "Lio/reactivex/Observable;", "Defaults", "Keys", "SkylightType", "app_debug"})
@dagger.Reusable()
public class SkylightRepository {
    @org.jetbrains.annotations.NotNull()
    private final drewhamilton.rxpreferences.RxPreferences preferences = null;
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Observable<drewhamilton.skylight.android.sample.source.SkylightRepository.SkylightType> observeSelectedSkylightType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Single<drewhamilton.skylight.android.sample.source.SkylightRepository.SkylightType> getSelectedSkylightType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    protected final drewhamilton.rxpreferences.RxPreferences getPreferences() {
        return null;
    }
    
    @javax.inject.Inject()
    public SkylightRepository(@org.jetbrains.annotations.NotNull()
    drewhamilton.rxpreferences.RxPreferences preferences) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005\u00a8\u0006\u0006"}, d2 = {"Ldrewhamilton/skylight/android/sample/source/SkylightRepository$SkylightType;", "", "(Ljava/lang/String;I)V", "SSO", "CALCULATOR", "DUMMY", "app_debug"})
    public static enum SkylightType {
        /*public static final*/ SSO /* = new SSO() */,
        /*public static final*/ CALCULATOR /* = new CALCULATOR() */,
        /*public static final*/ DUMMY /* = new DUMMY() */;
        
        SkylightType() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u00c4\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Ldrewhamilton/skylight/android/sample/source/SkylightRepository$Keys;", "", "()V", "SKYLIGHT_TYPE", "", "app_debug"})
    public static final class Keys {
        @org.jetbrains.annotations.NotNull()
        public static final java.lang.String SKYLIGHT_TYPE = "SkylightType";
        public static final drewhamilton.skylight.android.sample.source.SkylightRepository.Keys INSTANCE = null;
        
        private Keys() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c4\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Ldrewhamilton/skylight/android/sample/source/SkylightRepository$Defaults;", "", "()V", "SKYLIGHT_TYPE", "Ldrewhamilton/skylight/android/sample/source/SkylightRepository$SkylightType;", "getSKYLIGHT_TYPE", "()Ldrewhamilton/skylight/android/sample/source/SkylightRepository$SkylightType;", "app_debug"})
    public static final class Defaults {
        @org.jetbrains.annotations.NotNull()
        private static final drewhamilton.skylight.android.sample.source.SkylightRepository.SkylightType SKYLIGHT_TYPE = null;
        public static final drewhamilton.skylight.android.sample.source.SkylightRepository.Defaults INSTANCE = null;
        
        @org.jetbrains.annotations.NotNull()
        public final drewhamilton.skylight.android.sample.source.SkylightRepository.SkylightType getSKYLIGHT_TYPE() {
            return null;
        }
        
        private Defaults() {
            super();
        }
    }
}