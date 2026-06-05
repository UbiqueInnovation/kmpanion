package ch.ubique.libs.kmpanion.flow

import android.content.SharedPreferences
import android.content.SharedPreferences.*
import androidx.core.content.edit
import kotlin.test.Test
import kotlin.test.assertEquals

class SharedPreferenceMutableStateFlowTests {

	@Test
	fun testInitializedWithDefault() {
		val key = "k42"
		val defaultValue = 12
		val prefs = TestSharedPreferences()
		val flow = SharedPreferenceMutableStateFlow(prefs, key, defaultValue)
		assertEquals(defaultValue, flow.value)
	}

	@Test
	fun testInitializedWithSaved() {
		val key = "k42"
		val storedValue = 34
		val defaultValue = 12
		val prefs = TestSharedPreferences()
		prefs.edit { putInt(key, storedValue) }
		val flow = SharedPreferenceMutableStateFlow(prefs, key, defaultValue)
		assertEquals(storedValue, flow.value)
	}

	@Test
	fun testChangedValue() {
		val key = "k42"
		val defaultValue = 12
		val changedValue = 34
		val prefs = TestSharedPreferences()
		val flow = SharedPreferenceMutableStateFlow(prefs, key, defaultValue)
		flow.value = changedValue
		assertEquals(changedValue, flow.value)
		assertEquals(changedValue, prefs.getInt(key, -1))
	}

}


private class TestSharedPreferences : SharedPreferences {

	private val ints = mutableMapOf<String?, Int>()

	override fun getAll(): MutableMap<String, *> = TODO("Not yet implemented")
	override fun getString(key: String?, defValue: String?): String? = TODO("Not yet implemented")
	override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String> = TODO("Not yet implemented")
	override fun getInt(key: String?, defValue: Int): Int = ints.getOrDefault(key, defValue)
	override fun getLong(key: String?, defValue: Long): Long = TODO("Not yet implemented")
	override fun getFloat(key: String?, defValue: Float): Float = TODO("Not yet implemented")
	override fun getBoolean(key: String?, defValue: Boolean): Boolean = TODO("Not yet implemented")
	override fun contains(key: String?): Boolean = ints.containsKey(key)
	override fun edit(): Editor = TestEditor()
	override fun registerOnSharedPreferenceChangeListener(listener: OnSharedPreferenceChangeListener) = TODO("Not yet implemented")
	override fun unregisterOnSharedPreferenceChangeListener(listener: OnSharedPreferenceChangeListener) =
		TODO("Not yet implemented")

	inner class TestEditor : Editor {

		private val newInts = mutableMapOf<String?, Int>()

		override fun putString(key: String?, value: String?): Editor {
			TODO("Not yet implemented")
		}

		override fun putStringSet(key: String?, values: MutableSet<String>?): Editor {
			TODO("Not yet implemented")
		}

		override fun putInt(key: String?, value: Int): Editor {
			newInts[key] = value;
			return this
		}

		override fun putLong(key: String?, value: Long): Editor {
			TODO("Not yet implemented")
		}

		override fun putFloat(key: String?, value: Float): Editor {
			TODO("Not yet implemented")
		}

		override fun putBoolean(key: String?, value: Boolean): Editor {
			TODO("Not yet implemented")
		}

		override fun remove(key: String?): Editor {
			TODO("Not yet implemented")
		}

		override fun clear(): Editor {
			TODO("Not yet implemented")
		}

		override fun commit(): Boolean {
			ints.putAll(newInts); return true
		}

		override fun apply() {
			commit()
		}

	}

}
