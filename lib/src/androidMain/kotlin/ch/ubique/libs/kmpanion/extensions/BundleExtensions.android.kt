@file:Suppress("NOTHING_TO_INLINE")

package ch.ubique.libs.kmpanion.extensions

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.BundleCompat
import java.io.Serializable

@Suppress("DEPRECATION")
inline fun <reified T : Parcelable> Bundle.optionalParcelable(key: String) =
	BundleCompat.getParcelable(this, key, T::class.java)

@Suppress("DEPRECATION")
inline fun <reified T : Parcelable> Bundle.optionalParcelableArrayList(key: String) =
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
		getParcelableArrayList(key, T::class.java)
	} else {
		getParcelableArrayList(key)
	}

@Suppress("DEPRECATION")
inline fun <reified T : Serializable> Bundle.optionalSerializable(key: String) =
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
		getSerializable(key, T::class.java)
	} else {
		getSerializable(key) as T?
	}

@Suppress("DEPRECATION", "UNCHECKED_CAST")
inline fun <reified T> Bundle.optionalSerializableList(key: String) =
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
		getSerializable(key, Serializable::class.java) as List<T>?
	} else {
		getSerializable(key) as List<T>?
	}

@Suppress("DEPRECATION", "UNCHECKED_CAST")
inline fun <reified K, reified V> Bundle.optionalSerializableMap(key: String) =
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
		getSerializable(key, Serializable::class.java) as Map<K, V>?
	} else {
		getSerializable(key) as Map<K, V>?
	}

inline fun <reified T : Parcelable> Bundle.requireParcelable(key: String) =
	requireNotNull(optionalParcelable<T>(key))

inline fun <reified T : Parcelable> Bundle.requireParcelableArrayList(key: String) =
	requireNotNull(optionalParcelableArrayList<T>(key))

inline fun <reified T : Serializable> Bundle.requireSerializable(key: String) =
	requireNotNull(optionalSerializable<T>(key))

inline fun <reified T> Bundle.requireSerializableList(key: String) =
	requireNotNull(optionalSerializableList<T>(key))

inline fun <reified K, reified V> Bundle.requireSerializableMap(key: String) =
	requireNotNull(optionalSerializableMap<K, V>(key))

inline fun Bundle.requireBundle(key: String) = requireNotNull(getBundle(key))

inline fun Bundle.requireString(key: String) = requireNotNull(getString(key))

inline fun buildBundle(block: Bundle.() -> Unit): Bundle {
	return Bundle().also(block)
}

inline fun Bundle.getOptionalInt(key: String): Int? = if (containsKey(key)) getInt(key) else null

