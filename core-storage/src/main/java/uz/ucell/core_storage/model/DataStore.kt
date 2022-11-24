package uz.ucell.core_storage.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

private const val PREFS_NAME: String = "app_config"

internal const val DEFAULT_EMPTY_STRING = ""

internal val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFS_NAME)
