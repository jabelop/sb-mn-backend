package combats.application.dtos

import com.jatec.combats.domain.model.CombatData
import kotlinx.serialization.Serializable

@Serializable
data class MessagePlayerCombatUpdate(
    var combat: CombatData?
)