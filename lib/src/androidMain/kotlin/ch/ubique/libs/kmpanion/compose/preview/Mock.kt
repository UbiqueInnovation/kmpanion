package ch.ubique.libs.kmpanion.compose.preview

import java.lang.reflect.Array as ReflectArray
import java.lang.reflect.GenericArrayType
import java.lang.reflect.Proxy
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable
import kotlin.coroutines.Continuation

/**
 * Creates a no-op implementation of an interface for Android Compose previews.
 *
 * Returns use zero/false, empty arrays and standard collections, Unit,
 * or a public zero-argument constructor (including String's empty constructor).
 * Unsupported returns fail when invoked. Suspend methods and unresolved generic returns are not supported.
 * This is a preview helper, not a general-purpose mocking framework.
 */
inline fun <reified T : Any> mock(): T = createPreviewMock(T::class.java)

@PublishedApi
internal fun <T : Any> createPreviewMock(type: Class<T>): T {
	require(type.isInterface) { "${type.simpleName} must be an interface" }
	val proxy = Proxy.newProxyInstance(type.classLoader, arrayOf(type)) { proxy, method, args ->
		when (method.name) {
			"equals" if method.parameterTypes.contentEquals(arrayOf(Any::class.java)) -> proxy === args?.firstOrNull()
			"hashCode" if method.parameterCount == 0 -> System.identityHashCode(proxy)
			"toString" if method.parameterCount == 0 -> "Mock<${type.simpleName}>"
			else -> {
				val description = "${type.simpleName}.${method.name}"
				check(method.returnType != Any::class.java || method.parameterTypes.lastOrNull() != Continuation::class.java) {
					"$description: suspend methods are not supported"
				}
				check(!hasErasedComponent(method.genericReturnType)) {
					"$description: cannot create a default for unresolved generic return ${method.genericReturnType}"
				}
				defaultValue(method.returnType, description)
			}
		}
	}
	return checkNotNull(type.cast(proxy))
}

private fun hasErasedComponent(type: Type): Boolean = when (type) {
	is TypeVariable<*> -> true
	is GenericArrayType -> hasErasedComponent(type.genericComponentType)
	else -> false
}

private fun defaultValue(type: Class<*>, method: String): Any? = when (type) {
	Void.TYPE -> null
	Unit::class.java -> Unit
	Boolean::class.javaPrimitiveType, Boolean::class.javaObjectType -> false
	Byte::class.javaPrimitiveType, Byte::class.javaObjectType -> 0.toByte()
	Short::class.javaPrimitiveType, Short::class.javaObjectType -> 0.toShort()
	Int::class.javaPrimitiveType, Int::class.javaObjectType -> 0
	Long::class.javaPrimitiveType, Long::class.javaObjectType -> 0L
	Float::class.javaPrimitiveType, Float::class.javaObjectType -> 0f
	Double::class.javaPrimitiveType, Double::class.javaObjectType -> 0.0
	Char::class.javaPrimitiveType, Char::class.javaObjectType -> '\u0000'
	Iterable::class.java, Collection::class.java, List::class.java -> ArrayList<Any>()
	Set::class.java -> LinkedHashSet<Any>()
	Map::class.java -> LinkedHashMap<Any, Any>()
	else -> {
		if (type.isArray) {
			ReflectArray.newInstance(checkNotNull(type.componentType), 0)
		} else {
			try {
				type.getConstructor().newInstance()
			} catch (exception: NoSuchMethodException) {
				throw IllegalStateException(
					"$method: cannot create a default for ${type.typeName}; a public zero-argument constructor is required",
					exception,
				)
			} catch (exception: ReflectiveOperationException) {
				throw IllegalStateException(
					"$method: cannot create a default for ${type.typeName}; its zero-argument constructor could not be invoked",
					exception,
				)
			}
		}
	}
}
