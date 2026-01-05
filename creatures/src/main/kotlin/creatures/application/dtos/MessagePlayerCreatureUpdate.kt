package creatures.application.dtos

import com.jatec.creatures.domain.model.PlayerCreature
import kotlinx.serialization.Serializable

@Serializable
data class MessagePlayerCreatureUpdate(
    var creature: PlayerCreature?
)