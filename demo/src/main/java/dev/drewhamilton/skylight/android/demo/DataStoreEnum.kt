package dev.drewhamilton.skylight.android.demo

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences

inline fun <reified E : Enum<E>> Preferences.getEnum(key: Preferences.Key<String>): E? {
    return get(key)?.let {
        enumValueOf<E>(it)
    }
}

fun <E : Enum<E>> MutablePreferences.setEnum(key: Preferences.Key<String>, value: E) {
    set(key, value.name)
}
