package ch.ubique.libs.kmpanion.extensions

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.jvm.JvmOverloads


/**
 * A [Regex] which allows only letters/marks of the Unicode categories, "-" and whitespace.
 *
 * See [compart - Unicode Character Categories](https://www.compart.com/en/unicode/category) for details.
 */
val allowedNamesPattern: Regex = "[\\p{L}\\p{M}\\-\\s]+".toRegex()

/**
 * Taken from android.util.Patterns.EMAIL_ADDRESS
 */

//language=RegExp
private val EMAIL_ADDRESS_REGEX: Regex = (
		"""[a-zA-Z0-9\+\.\_\%\-\+]{1,256}\@[a-zA-Z0-9][a-zA-Z0-9\-]{0,64}(\.[a-zA-Z0-9][a-zA-Z0-9\-]{0,25})+"""
		).toRegex()

/**
 * Taken from android.util.Patterns.PHONE
 */
//language=RegExp
private val PHONE_REGEX: Regex = (                // sdd = space, dot, or dash
		"(\\+[0-9]+[\\- \\.]*)?"                // +<digits><sdd>*
				+ "(\\([0-9]+\\)[\\- \\.]*)?"   // (<digits>)<sdd>*
				+ "([0-9][0-9\\- \\.]+[0-9])"    // <digit><digit|sdd>+<digit>
		).toRegex()

/**
 * `true` if it matches [pattern], otherwise `false`.
 * @param pattern Default to [allowedNamesPattern]
 */
@JvmOverloads
fun CharSequence.isValidName(pattern: Regex = allowedNamesPattern): Boolean = pattern.matches(this)

fun CharSequence.isValidMail(): Boolean = EMAIL_ADDRESS_REGEX.matches(this)

fun CharSequence.isValidPhone(): Boolean = PHONE_REGEX.matches(this)

/**
 * Checks whether this [String] contains characters of the `Cs`
 * [Unicode Character Categories](https://www.compart.com/en/unicode/category).
 * The surrogate category contains the emoticons.
 */
fun CharSequence.containsSurrogateChars(): Boolean = find { it.isSurrogate() } != null

@OptIn(ExperimentalContracts::class)
fun CharSequence?.isNotNullOrEmpty(): Boolean {
	contract {
		returns(true) implies (this@isNotNullOrEmpty != null)
	}
	return !isNullOrEmpty()
}
