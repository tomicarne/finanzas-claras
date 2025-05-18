package co.edu.unab.overa32.finanzasclaras
import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "saldo_prefs")


class SaldoDataStore(private val context: Context) {
    companion object {
        val SALDO_KEY = doublePreferencesKey("saldo")
    }

    val getSaldo: Flow<Double> = context.dataStore.data.map { prefs ->
        prefs[SALDO_KEY] ?: 0.0
    }

    suspend fun saveSaldo(saldo: Double) {
        context.dataStore.edit { prefs ->
            prefs[SALDO_KEY] = saldo
        }
    }
}