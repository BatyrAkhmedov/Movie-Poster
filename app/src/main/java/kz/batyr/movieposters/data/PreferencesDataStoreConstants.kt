package kz.batyr.movieposters.data

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesDataStoreConstants {
    val onboardingCompleted = booleanPreferencesKey("onboardingCompleted")
    val INT_KEY = intPreferencesKey("INT_KEY")
    val STRING_KEY = stringPreferencesKey("STRING_KEY")
    val LONG_KEY = longPreferencesKey("LONG_NUMBER")
}