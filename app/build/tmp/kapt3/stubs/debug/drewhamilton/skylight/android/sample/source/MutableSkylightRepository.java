package drewhamilton.skylight.android.sample.source;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b\u00a8\u0006\t"}, d2 = {"Ldrewhamilton/skylight/android/sample/source/MutableSkylightRepository;", "Ldrewhamilton/skylight/android/sample/source/SkylightRepository;", "preferences", "Ldrewhamilton/rxpreferences/RxPreferences;", "(Ldrewhamilton/rxpreferences/RxPreferences;)V", "selectSkylightType", "Lio/reactivex/Completable;", "skylightType", "Ldrewhamilton/skylight/android/sample/source/SkylightRepository$SkylightType;", "app_debug"})
@dagger.Reusable()
public final class MutableSkylightRepository extends drewhamilton.skylight.android.sample.source.SkylightRepository {
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable selectSkylightType(@org.jetbrains.annotations.NotNull()
    drewhamilton.skylight.android.sample.source.SkylightRepository.SkylightType skylightType) {
        return null;
    }
    
    @javax.inject.Inject()
    public MutableSkylightRepository(@org.jetbrains.annotations.NotNull()
    drewhamilton.rxpreferences.RxPreferences preferences) {
        super(null);
    }
}