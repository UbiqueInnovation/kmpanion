package ch.ubique.libs.kmpanion.flow

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
class SharedPreferenceMutableStateFlow<T> private constructor(
	private val sharedPreferences: SharedPreferences,
	private val key: String,
	private val base: MutableStateFlow<T>,
) : MutableStateFlow<T> by base {

	constructor(sharedPreferences: SharedPreferences, key: String, initialValue: T) : this(
		sharedPreferences,
		key,
		MutableStateFlow(
			if (sharedPreferences.contains(key)) readValue(sharedPreferences, key, initialValue) else initialValue
		)
	)

	override var value: T
		get() = base.value
		set(value) {
			writeValue(sharedPreferences, key, value)
			base.value = value
		}

	companion object {
		fun <T> readValue(sharedPreferences: SharedPreferences, key: String, initialValue: T): T {
			return sharedPreferences.run {
				@Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
				when (initialValue) {
					is Long -> getLong(key, initialValue)
					is String -> getString(key, initialValue)
					is String? -> getString(key, initialValue)
					is Int -> getInt(key, initialValue)
					is Boolean -> getBoolean(key, initialValue)
					is Float -> getFloat(key, initialValue)
					is Set<*> -> getStringSet(key, initialValue as Set<String>)
					else -> throw IllegalArgumentException("$key has incompatible preference type")
				} as T
			}
		}

		fun <T> writeValue(sharedPreferences: SharedPreferences, key: String, value: T) {
			sharedPreferences.edit {
				@Suppress("UNCHECKED_CAST")
				when (value) {
					is Long -> putLong(key, value)
					is String -> putString(key, value)
					is String? -> putString(key, value)
					is Int -> putInt(key, value)
					is Boolean -> putBoolean(key, value)
					is Float -> putFloat(key, value)
					is Set<*> -> putStringSet(key, value as Set<String>)
					else -> throw IllegalArgumentException("$key has incompatible preference type")
				}
			}
		}
	}

}