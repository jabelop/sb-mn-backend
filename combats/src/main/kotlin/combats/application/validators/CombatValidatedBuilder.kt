package com.jatec.combats.application.validators

import com.jatec.combats.application.utils.DateUtils
import com.jatec.combats.domain.model.Combat
import com.jatec.combats.domain.value_objects.ValidDateTime
import com.jatec.combats.domain.value_objects.ValidIp
import com.jatec.combats.domain.value_objects.ValidMatch
import com.jatec.combats.domain.value_objects.ValidPort
import com.jatec.combats.domain.value_objects.ValidUUID

class CombatValidatedBuilder {

    companion object {
        fun buildValidatedCombat(combat: Combat): Combat {
            val validatedMatch = ValidMatch(
                ValidUUID(combat.idPlayer1.toString()).validatedUuid.toString(),
                ValidUUID(combat.idPlayer2.toString()).validatedUuid.toString(),
            ).validatedMatch
            return Combat(
                id = ValidUUID(combat.id.toString()).validatedUuid.toString(),
                idPlayer1 = validatedMatch!!.idPlayer1,
                idPlayer2 = validatedMatch.idPlayer2,
                winner = ValidUUID(combat.winner.toString()).validatedUuid.toString(),
                ip = ValidIp(combat.ip).validatedIp,
                port = ValidPort(combat.port).validatedPort,
                startedAt = if (combat.startedAt != null) ValidDateTime(combat.startedAt).validatedDateTime else null,
                updatedAt = if (combat.updatedAt != null) ValidDateTime(combat.updatedAt).validatedDateTime else null,
                finishedAt = if (combat.finishedAt != null) DateUtils.timeStringToUnixDate(ValidDateTime(DateUtils.unixTimeToDateString(combat.finishedAt)).validatedDateTime) else null,
            )
        }
    }
}