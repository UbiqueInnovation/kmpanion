package ch.ubique.libs.kmpanion.extensions

import android.database.Cursor

fun Cursor.getStringOrNull(columnName: String): String? = runCatching {
	getString(this.getColumnIndexOrThrow(columnName))
}.getOrNull()

fun Cursor.getLongOrNull(columnName: String): Long? = runCatching { getLong(getColumnIndexOrThrow(columnName)) }.getOrNull()

fun Cursor.getIntOrNull(columnName: String): Int? = runCatching { getInt(getColumnIndexOrThrow(columnName)) }.getOrNull()

fun Cursor.getDoubleOrNull(columnName: String): Double? = runCatching { getDouble(getColumnIndexOrThrow(columnName)) }.getOrNull()

fun Cursor.getShortOrNull(columnName: String): Short? = runCatching { getShort(getColumnIndexOrThrow(columnName)) }.getOrNull()

fun Cursor.getFloatOrNull(columnName: String): Float? = runCatching { getFloat(getColumnIndexOrThrow(columnName)) }.getOrNull()