package dev.drewhamilton.skylight.android.demo.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.Reusable
import dev.drewhamilton.skylight.android.demo.getEnum
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

@Reusable
open class SkylightRepository @Inject constructor(
    protected val dataStore: DataStore<Preferences>,
) {

    fun getSelectedSkylightTypeFlow(): Flow<SkylightType> = dataStore.data.map {
        it.getEnum<SkylightType>(Keys.SKYLIGHT_TYPE) ?: Defaults.SKYLIGHT_TYPE
    }

    suspend fun getSelectedSkylightType(): SkylightType {
        return dataStore.data.first().getEnum<SkylightType>(Keys.SKYLIGHT_TYPE) ?: Defaults.SKYLIGHT_TYPE
    }

    enum class SkylightType {
        SSO,
        CALCULATOR,
        FAKE;
    }

    protected object Keys {
        val SKYLIGHT_TYPE = stringPreferencesKey("SkylightType")
    }

    protected object Defaults {
        val SKYLIGHT_TYPE = SkylightType.SSO
    }
}
