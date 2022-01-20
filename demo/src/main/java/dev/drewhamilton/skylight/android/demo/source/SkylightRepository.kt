package dev.drewhamilton.skylight.android.demo.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.Reusable
import dev.drewhamilton.skylight.android.demo.getEnum
import dev.drewhamilton.skylight.android.demo.setEnum
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@Reusable
open class SkylightRepository @Inject constructor(
    protected val dataStore: DataStore<Preferences>,
    @Named("database") private val databaseDispatcher: CoroutineDispatcher = Dispatchers.Default,
) {

    fun getSelectedSkylightTypeFlow(): Flow<SkylightType> = dataStore.data.map {
        it.getEnum<SkylightType>(Keys.SKYLIGHT_TYPE) ?: Defaults.SKYLIGHT_TYPE
    }

    suspend fun getSelectedSkylightType(): SkylightType {
        return dataStore.data.first().getEnum<SkylightType>(Keys.SKYLIGHT_TYPE) ?: Defaults.SKYLIGHT_TYPE
    }

    suspend fun selectSkylightType(skylightType: SkylightType) {
        withContext(databaseDispatcher) {
            dataStore.edit {
                it.setEnum(Keys.SKYLIGHT_TYPE, skylightType)
            }
        }
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
