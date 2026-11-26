package com.example.a1004bala.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.userPrefsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "bookworm_prefs"
)

class DataStoreUserPreferencesRepository(
    private val context: Context
) : UserPreferencesRepository {

    private object Keys {
        val THEME = stringPreferencesKey("theme_mode")
        val SORT = stringPreferencesKey("sort_order")
    }

    private val prefs: Flow<Preferences> = context.userPrefsDataStore.data
        .catch { e ->
            if (e is IOException) emit(emptyPreferences()) else throw e
        }

    override val themeMode: Flow<ThemeMode> = prefs
        .map { enumOrDefault(it[Keys.THEME], ThemeMode.SYSTEM) }
        .distinctUntilChanged()

    override val sortOrder: Flow<SortOrder> = prefs
        .map { enumOrDefault(it[Keys.SORT], SortOrder.RECENT) }
        .distinctUntilChanged()

    override suspend fun setThemeMode(mode: ThemeMode) {
        context.userPrefsDataStore.edit { it[Keys.THEME] = mode.name }
    }

    override suspend fun setSortOrder(order: SortOrder) {
        context.userPrefsDataStore.edit { it[Keys.SORT] = order.name }
    }
}

internal inline fun <reified E : Enum<E>> enumOrDefault(name: String?, default: E): E =
    enumValues<E>().firstOrNull { it.name == name } ?: default
