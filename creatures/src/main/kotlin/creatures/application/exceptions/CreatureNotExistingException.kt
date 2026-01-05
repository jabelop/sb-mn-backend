package creatures.application.exceptions

import com.jatec.creatures.domain.exceptions.ClientException

class CreatureNotExistingException(override val message: String): ClientException(message)