package ch.ubique.libs.kmpanion.compose.adaptive

import androidx.compose.ui.unit.dp

object ContentRole : AdaptiveRole
object ActionRole : AdaptiveRole
object SheetRole : AdaptiveRole

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
