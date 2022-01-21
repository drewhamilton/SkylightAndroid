package dev.drewhamilton.skylight.android.demo

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
object ApplicationModule {

    private val Context.applicationDataStore by preferencesDataStore(name = "drewhamilton.skylight.sample")

    @JvmStatic
    @Provides
    @Singleton
    fun applicationContext(application: Application): Context = application.applicationContext

    @JvmStatic
    @Provides
    @Reusable
    fun sharedPreferences(application: Application): SharedPreferences =
        application.getSharedPreferences("drewhamilton.skylight.sample", Context.MODE_PRIVATE)

    @JvmStatic
    @Provides
    @Reusable
    fun prefsDataStore(application: Application): DataStore<Preferences> = application.applicationDataStore

    @JvmStatic
    @Provides
    @Named("database")
    fun databaseDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @JvmStatic
    @Provides
    @Named("main")
    fun mainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}

