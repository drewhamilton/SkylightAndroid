package dev.drewhamilton.skylight.android.demo.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import dagger.Reusable
import dev.drewhamilton.skylight.android.demo.setEnum
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Reusable
class MutableSkylightRepository @Inject constructor(
    dataStore: DataStore<Preferences>,
    @Named("database") private val databaseDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : SkylightRepository(dataStore) {

    suspend fun selectSkylightType(skylightType: SkylightType) {
        withContext(databaseDispatcher) {
            dataStore.edit {
                it.setEnum(Keys.SKYLIGHT_TYPE, skylightType)
            }
        }
    }
}
