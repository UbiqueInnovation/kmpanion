package ch.ubique.libs.kmpanion.compose.adaptive.config

/** Stores the adaptive width policies assigned to different content roles. */
class AdaptiveRoleConfig(private val policies: Map<AdaptiveRole, AdaptivePolicy>) {

	/** Returns the policy for [role], or fails if no policy is registered. */
	fun policyFor(role: AdaptiveRole): AdaptivePolicy = requireNotNull(policies[role]) {
		"No adaptive policy registered for $role"
	}

	/** Returns a new configuration with additional [policy] assigned to [role]. */
	fun withPolicy(role: AdaptiveRole, policy: AdaptivePolicy): AdaptiveRoleConfig =
		AdaptiveRoleConfig(policies + (role to policy))
}
