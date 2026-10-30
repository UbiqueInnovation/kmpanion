package ch.ubique.libs.kmpanion.compose.adaptive.config

import androidx.compose.ui.unit.dp

object ContentRole : AdaptiveRole
object ActionRole : AdaptiveRole
object SheetRole : AdaptiveRole

/** Default width policies containing Content, Action and Sheet Roles as default */
val DefaultAdaptiveRoleConfig = AdaptiveRoleConfig(
	mapOf(
		ContentRole to AdaptivePolicy(
			mediumMaxWidth = 720.dp,
			expandedMaxWidth = 840.dp,
		),
		ActionRole to AdaptivePolicy(mediumMaxWidth = 420.dp),
		SheetRole to AdaptivePolicy(mediumMaxWidth = 420.dp),
	),
)
