package combats.domain.options

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePlayerCombatOptions (val combatId: String, val xp: Int)