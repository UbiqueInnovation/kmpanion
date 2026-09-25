package ch.ubique.libs.kmpanion.compose.adaptive

class AdaptiveRoleConfig(policies: Map<AdaptiveRole, AdaptivePolicy>) {
	private val policies = policies.toMap()

	fun policyFor(role: AdaptiveRole): AdaptivePolicy = requireNotNull(policies[role]) {
		"No adaptive policy registered for $role"
	}

	fun withPolicy(role: AdaptiveRole, policy: AdaptivePolicy): AdaptiveRoleConfig =
		AdaptiveRoleConfig(policies + (role to policy))
}