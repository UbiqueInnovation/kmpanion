package ch.ubique.libs.kmpanion.compose.adaptive.config

/** Identifies a type of content that can have its own adaptive width policy.
 * Built-in roles are [ContentRole], [ActionRole], and [SheetRole].
 * Implement this interface to define a custom role.
 * */
interface AdaptiveRole

/**
 * Used for primary screen content, such as forms or lists.
 */
object ContentRole : AdaptiveRole

/**
 * Used for action controls, such as buttons or groups of buttons.
 */
object ActionRole : AdaptiveRole

/**
 * Used to constrain the width of dialogs or sheets.
 */
object SheetRole : AdaptiveRole
