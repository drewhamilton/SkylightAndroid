package drewhamilton.skylight.android.sample.location;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000f\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B!\b\u0002\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000ej\u0002\b\u000fj\u0002\b\u0010j\u0002\b\u0011j\u0002\b\u0012j\u0002\b\u0013j\u0002\b\u0014j\u0002\b\u0015\u00a8\u0006\u0016"}, d2 = {"Ldrewhamilton/skylight/android/sample/location/ExampleLocation;", "", "longDisplayName", "", "timeZone", "Lorg/threeten/bp/ZoneId;", "coordinates", "Ldrewhamilton/skylight/backport/Coordinates;", "(Ljava/lang/String;IILorg/threeten/bp/ZoneId;Ldrewhamilton/skylight/backport/Coordinates;)V", "getCoordinates", "()Ldrewhamilton/skylight/backport/Coordinates;", "getLongDisplayName", "()I", "getTimeZone", "()Lorg/threeten/bp/ZoneId;", "AMSTERDAM", "BANGKOK", "HONOLULU", "ISTANBUL", "LIMA", "MARRAKESH", "NASHVILLE", "app_debug"})
public enum ExampleLocation {
    /*public static final*/ AMSTERDAM /* = new AMSTERDAM(0, null, null) */,
    /*public static final*/ BANGKOK /* = new BANGKOK(0, null, null) */,
    /*public static final*/ HONOLULU /* = new HONOLULU(0, null, null) */,
    /*public static final*/ ISTANBUL /* = new ISTANBUL(0, null, null) */,
    /*public static final*/ LIMA /* = new LIMA(0, null, null) */,
    /*public static final*/ MARRAKESH /* = new MARRAKESH(0, null, null) */,
    /*public static final*/ NASHVILLE /* = new NASHVILLE(0, null, null) */;
    private final int longDisplayName = 0;
    @org.jetbrains.annotations.NotNull()
    private final org.threeten.bp.ZoneId timeZone = null;
    @org.jetbrains.annotations.NotNull()
    private final drewhamilton.skylight.backport.Coordinates coordinates = null;
    
    public final int getLongDisplayName() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final org.threeten.bp.ZoneId getTimeZone() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final drewhamilton.skylight.backport.Coordinates getCoordinates() {
        return null;
    }
    
    ExampleLocation(@androidx.annotation.StringRes()
    int longDisplayName, org.threeten.bp.ZoneId timeZone, drewhamilton.skylight.backport.Coordinates coordinates) {
    }
}