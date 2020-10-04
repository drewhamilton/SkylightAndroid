package dev.drewhamilton.skylight.android.demo.source

import dagger.Reusable
import drewhamilton.rxpreferences.RxPreferences
import drewhamilton.rxpreferences.getEnumOnce
import drewhamilton.rxpreferences.getEnumStream
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

@Reusable
open class SkylightRepository @Inject constructor(protected val preferences: RxPreferences) {

    fun getSelectedSkylightTypeStream(): Observable<SkylightType> =
        preferences.getEnumStream(Keys.SKYLIGHT_TYPE, Defaults.SKYLIGHT_TYPE)

    fun getSelectedSkylightTypeOnce(): Single<SkylightType> =
        preferences.getEnumOnce(Keys.SKYLIGHT_TYPE, Defaults.SKYLIGHT_TYPE)

    enum class SkylightType {
        SSO,
        CALCULATOR,
        DUMMY
    }

    protected object Keys {
        const val SKYLIGHT_TYPE = "SkylightType"
    }

    protected object Defaults {
        val SKYLIGHT_TYPE = SkylightType.SSO
    }
}
