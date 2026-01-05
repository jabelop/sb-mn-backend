package com.jatec.creatures.application

import com.jatec.creatures.application.dtos.MessageOptions
import com.jatec.creatures.application.service.CreaturesService
import com.jatec.creatures.domain.constants.ATTACK_INCREASE
import com.jatec.creatures.domain.constants.HP_INCREASE
import com.jatec.creatures.domain.constants.SPEED_INCREASE
import com.jatec.creatures.domain.exceptions.InvalidName
import com.jatec.creatures.domain.exceptions.InvalidUUID
import com.jatec.creatures.domain.exceptions.InvalidXPGain
import com.jatec.creatures.domain.model.Creature
import com.jatec.creatures.domain.model.PlayerCreature
import com.jatec.creatures.domain.options.CreatePlayerCreatureOptions
import com.jatec.creatures.domain.options.WhereOptions
import com.jatec.creatures.domain.repository.CreaturesRepository
import com.jatec.creatures.domain.rules.LevelUpManager
import com.jatec.creatures.domain.rules.LevelUpManagerImpl
import com.jatec.creatures.repository.CreaturesRepositoryTest
import creatures.application.exceptions.CreatureNotExistingException
import creatures.domain.options.UpdatePlayerCreatureOptions
import io.ktor.server.application.log
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import kotlin.getValue
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals


val creaturesModule = module {
    singleOf(::LevelUpManagerImpl) bind LevelUpManager::class
    singleOf(::CreaturesRepositoryTest) bind CreaturesRepository::class
    singleOf(::CreaturesService)
}

class CreaturesServiceTest {
    var creaturesServiceTest: CreaturesService? = null

    @Test
    fun testFindAllCreaturesNull() = testApplication {
        runBlocking {
            install(Koin) {
                modules(creaturesModule)
            }
            application {
                val creaturesService by inject< CreaturesService>()
                creaturesServiceTest = creaturesService

                val expected = listOf<Creature>(
                    Creature(
                        id = "05fb3246-9387-4d04-a27f-fab107c33883",
                        name = "Creature test 1",
                        creatureClass = "warrior",
                        level = 0,
                        xp = 89,
                        hp = 100,
                        speed = 15,
                        attack = 18
                    ),
                    Creature(
                        id="05fb3246-9387-4d04-a2cf-fa0107433883",
                        name = "Creature test 2",
                        creatureClass = "warrior",
                        level = 1,
                        xp = 167,
                        hp = 115,
                        speed = 20,
                        attack = 24
                    ),
                    Creature(
                        id="88fb3246-9387-4d04-a2cf-fa0107433883",
                        name = "Creature test 3",
                        creatureClass = "defender",
                        level = 0,
                        xp = 46,
                        hp = 88,
                        speed = 13,
                        attack = 10
                    ),
                    Creature(
                        id="a7fb3246-0087-4d04-a2cf-fa0107433883",
                        name = "Creature test 4",
                        creatureClass = "enchanter",
                        level = 1,
                        xp = 196,
                        hp = 105,
                        speed = 18,
                        attack = 22
                    )
                )
                val creatures = creaturesServiceTest!!.find(
                    MessageOptions(
                        WhereOptions(
                            playerId = null,
                            name = null,
                            creatureId = null,
                            enemyId = null
                        )
                    )
                )
                assertContentEquals(creatures, expected)
            }
        }
    }

    @Test
    fun testFindAllCreaturesIdCreature3Set() = testApplication {
        install(Koin) {
            modules(creaturesModule)
        }
        application {
            val creaturesService by inject<CreaturesService>()
            creaturesServiceTest = creaturesService

            val expected = listOf<Creature>(
                Creature(
                    id="88fb3246-9387-4d04-a2cf-fa0107433883",
                    name = "Creature test 3",
                    creatureClass = "defender",
                    level = 0,
                    xp = 46,
                    hp = 88,
                    speed = 13,
                    attack = 10
                )
            )
            val creatures = creaturesServiceTest!!.find(
                MessageOptions(
                    WhereOptions(
                        playerId = null,
                        name = null,
                        creatureId = "88fb3246-9387-4d04-a2cf-fa0107433883",
                        enemyId = null
                    )
                )
            )
            assertContentEquals(expected, creatures)
        }
    }

    @Test
    fun testFindAllCreaturesNameCreature2Set() = testApplication {
        install(Koin) {
            modules(creaturesModule)
        }
        application {
            val creaturesService by inject< CreaturesService>()
            creaturesServiceTest = creaturesService

            val expected = listOf<Creature>(
                Creature(
                    id="05fb3246-9387-4d04-a2cf-fa0107433883",
                    name = "Creature test 2",
                    creatureClass = "warrior",
                    level = 1,
                    xp = 167,
                    hp = 115,
                    speed = 20,
                    attack = 24
                )
            )
            val creatures = creaturesServiceTest!!.find(
                MessageOptions(
                    WhereOptions(
                        playerId = null,
                        name = "Creature test 2",
                        creatureId = null,
                        enemyId = null
                    )
                )
            )
            assertContentEquals(expected, creatures)
        }
    }


    @Test
    fun testFindAllCreaturesThrowBadNameException() = testApplication {
        install(Koin) {
            modules(creaturesModule)
        }
        application {
            val creaturesService by inject< CreaturesService>()
            creaturesServiceTest = creaturesService

            try {
                creaturesServiceTest!!.find(
                    MessageOptions(
                        WhereOptions(
                            playerId = null,
                            name = "Pl",
                            creatureId = null,
                            enemyId = null
                        )
                    )
                )
                assert(false)
            } catch (e: Exception) {
                when(e) {
                    is InvalidName -> assert(true)
                    else -> assert(false)
                }
            }
        }
    }

    @Test
    fun testFindAllCreaturesThrowBadUUIDException() = testApplication {
        install(Koin) {
            modules(creaturesModule)
        }
        application {
            val creaturesService by inject<CreaturesService>()
            creaturesServiceTest = creaturesService

            try {
                creaturesServiceTest!!.find(
                    MessageOptions(
                        WhereOptions(
                            playerId = "0987-4d04-a27f-fab107c33883",
                            name = null,
                            creatureId = null,
                            enemyId = null
                        )
                    )
                )
                assert(false)
            } catch (e: Exception) {
                when(e) {
                    is InvalidUUID -> assert(true)
                    else -> assert(false)
                }
            }
        }
    }

    // Find all player creatures
    @Test
    fun testFindAllPlayerCreaturesNull() = testApplication {
        runBlocking {
            install(Koin) {
                modules(creaturesModule)
            }
            application {
                val creaturesService by inject< CreaturesService>()
                creaturesServiceTest = creaturesService

                val expected = listOf<PlayerCreature>(
                    PlayerCreature(
                        id = "05fb3246-9387-4d04-a27f-fab107c33883",
                        name = "Creature test 1",
                        idPlayer = "05003246-9387-4d04-a27f-fab107c33883",
                        creatureClass = "warrior",
                        level = 0,
                        xp = 89,
                        hp = 100,
                        speed = 15,
                        attack = 18,
                    ),
                    PlayerCreature(
                        id="05fb3246-9387-4d04-a2cf-fa0107433883",
                        name = "Creature test 2",
                        idPlayer = "05c93246-9387-4d04-a27f-fab107c33883",
                        creatureClass = "warrior",
                        level = 1,
                        xp = 167,
                        hp = 115,
                        speed = 20,
                        attack = 24
                    ),
                    PlayerCreature(
                        id="88fb3246-9387-4d04-a2cf-fa0107433883",
                        name = "Creature test 3",
                        idPlayer = "05c93246-9387-4d04-a27f-fab107c33883",
                        creatureClass = "defender",
                        level = 0,
                        xp = 46,
                        hp = 88,
                        speed = 13,
                        attack = 10
                    ),
                    PlayerCreature(
                        id="a7fb3246-0087-4d04-a2cf-fa0107433883",
                        name = "Creature test 4",
                        idPlayer = "05003246-9387-4d04-a27f-fab107c33883",
                        creatureClass = "enchanter",
                        level = 1,
                        xp = 196,
                        hp = 105,
                        speed = 18,
                        attack = 22
                    )
                )
                val creatures = creaturesServiceTest!!.findPlayerCreatures(
                    MessageOptions(
                        WhereOptions(
                            playerId = null,
                            name = null,
                            creatureId = null,
                            enemyId = null
                        )
                    )
                )
                assertContentEquals(expected, creatures)
            }
        }
    }

    @Test
    fun testFindAllPlayerCreaturesIdPlayerCreature1And4Set() = testApplication {
        install(Koin) {
            modules(creaturesModule)
        }
        application {
            val creaturesService by inject<CreaturesService>()
            creaturesServiceTest = creaturesService

            val expected = listOf<PlayerCreature>(
                PlayerCreature(
                    id = "05fb3246-9387-4d04-a27f-fab107c33883",
                    name = "Creature test 1",
                    idPlayer = "05003246-9387-4d04-a27f-fab107c33883",
                    creatureClass = "warrior",
                    level = 0,
                    xp = 89,
                    hp = 100,
                    speed = 15,
                    attack = 18,
                ),
                PlayerCreature(
                    id="a7fb3246-0087-4d04-a2cf-fa0107433883",
                    name = "Creature test 4",
                    idPlayer = "05003246-9387-4d04-a27f-fab107c33883",
                    creatureClass = "enchanter",
                    level = 1,
                    xp = 196,
                    hp = 105,
                    speed = 18,
                    attack = 22
                )
            )
            val playerCreatures = creaturesServiceTest!!.findPlayerCreatures(
                MessageOptions(
                    WhereOptions(
                        playerId = "05003246-9387-4d04-a27f-fab107c33883",
                        name = null,
                        creatureId = null,
                        enemyId = null
                    )
                )
            )
            assertContentEquals(expected, playerCreatures)
        }
    }

    @Test
    fun testFindAllPlayerCreaturesNameCreature2Set() = testApplication {
        install(Koin) {
            modules(creaturesModule)
        }
        application {
            val creaturesService by inject< CreaturesService>()
            creaturesServiceTest = creaturesService

            val expected = listOf<PlayerCreature>(
                PlayerCreature(
                    id="05fb3246-9387-4d04-a2cf-fa0107433883",
                    name = "Creature test 2",
                    idPlayer = "05c93246-9387-4d04-a27f-fab107c33883",
                    creatureClass = "warrior",
                    level = 1,
                    xp = 167,
                    hp = 115,
                    speed = 20,
                    attack = 24
                )
            )
            val creatures = creaturesServiceTest!!.findPlayerCreatures(
                MessageOptions(
                    WhereOptions(
                        playerId = null,
                        name = "Creature test 2",
                        creatureId = null,
                        enemyId = null
                    )
                )
            )
            assertContentEquals(expected, creatures)
        }
    }


    @Test
    fun testFindAllPlayerCreaturesThrowBadNameException() = testApplication {
        install(Koin) {
            modules(creaturesModule)
        }
        application {
            val creaturesService by inject<CreaturesService>()
            creaturesServiceTest = creaturesService

            try {
                creaturesServiceTest!!.findPlayerCreatures(
                    MessageOptions(
                        WhereOptions(
                            playerId = null,
                            name = "Pl",
                            creatureId = null,
                            enemyId = null
                        )
                    )
                )
                assert(false)
            } catch (e: Exception) {
                when(e) {
                    is InvalidName -> assert(true)
                    else -> assert(false)
                }
            }
        }
    }

    @Test
    fun testFindAllPlayerCreaturesThrowBadUUIDException() = testApplication {
        install(Koin) {
            modules(creaturesModule)
        }
        application {
            val creaturesService by inject<CreaturesService>()
            creaturesServiceTest = creaturesService

            try {
                creaturesServiceTest!!.findPlayerCreatures(
                    MessageOptions(
                        WhereOptions(
                            playerId = "0987-4d04-a27f-fab107c33883",
                            name = null,
                            creatureId = null,
                            enemyId = null
                        )
                    )
                )
                assert(false)
            } catch (e: Exception) {
                when(e) {
                    is InvalidUUID -> assert(true)
                    else -> assert(false)
                }
            }
        }
    }


    // Create one
    @Test
    fun testCreateOneCreature() = testApplication {
        install(Koin) {
            modules(creaturesModule)
        }
        application {
            val creaturesService by inject<CreaturesService>()
            creaturesServiceTest = creaturesService

            val result = creaturesServiceTest!!.create(
                CreatePlayerCreatureOptions(
                    playerId = "8bf03a46-dd87-4104-a2c7-fa01b7433d83",
                    creatureId = "a7fb3246-0087-4d04-a2cf-fa0107433883"
                )
            )
            assert(result)
        }
    }

    @Test
    fun testCreateOneCreatureNoCreatureIdSet() = testApplication {
        install(Koin) {
            modules(creaturesModule)
        }
        application {
            val creaturesService by inject<CreaturesService>()
            creaturesServiceTest = creaturesService

            try {
                creaturesServiceTest!!.create(
                    CreatePlayerCreatureOptions(
                        playerId = "8bf03a46-dd87-4104-a2c7-fa01b7433d83",
                        creatureId = ""
                    )
                )
                assert(false)
            } catch (e: Exception) {
                when(e) {
                    is InvalidUUID -> assert(true)
                    else -> assert(false)
                }
            }
        }
    }

    @Test
    fun testCreateOneCreatureNoPlayerIdSet() = testApplication {
        install(Koin) {
            modules(creaturesModule)
        }
        application {
            val creaturesService by inject<CreaturesService>()
            creaturesServiceTest = creaturesService

            try {
                creaturesServiceTest!!.create(
                    CreatePlayerCreatureOptions(
                        playerId = "",
                        creatureId = "a7fb3246-0087-4d04-a2cf-fa0107433883"
                    )
                )
                assert(false)
            } catch (e: Exception) {
                when(e) {
                    is InvalidUUID -> assert(true)
                    else -> assert(false)
                }
            }
        }
    }

    @Test
    fun testCreateOneCreatureNoExistingIdSet() = testApplication {
        install(Koin) {
            modules(creaturesModule)
        }
        application {
            val creaturesService by inject<CreaturesService>()
            creaturesServiceTest = creaturesService

            try {
                creaturesServiceTest!!.create(
                    CreatePlayerCreatureOptions(
                        playerId = "8bf03a46-dd87-4104-a2c7-fa01b7433d83",
                        creatureId = "8bf03a46-dd87-4104-a2c7-fa01b7433d83"
                    )
                )
                assert(false)
            } catch (e: Exception) {
                log.info(e.message)
                log.info("exception")
                when(e) {
                    is CreatureNotExistingException -> assert(true)
                    else -> assert(false)
                }
            }
        }
    }

    // Update one
    @Test
    fun testUpdateOneCreature() = testApplication {
        install(Koin) {
            modules(creaturesModule)
        }
        application {
            val creaturesService by inject<CreaturesService>()
            creaturesServiceTest = creaturesService

            val expected = PlayerCreature(
                id = "05fb3246-9387-4d04-a27f-fab107c33883",
                name = "Creature test 1",
                idPlayer = "05003246-9387-4d04-a27f-fab107c33883",
                creatureClass = "warrior",
                level = 1,
                xp = 114,
                hp = 100 + HP_INCREASE,
                speed = 15 + SPEED_INCREASE,
                attack = 18 + ATTACK_INCREASE,
            )
            val result = creaturesServiceTest!!.updatePlayerCreature(
                UpdatePlayerCreatureOptions(
                    creatureId = "05fb3246-9387-4d04-a27f-fab107c33883",
                    xp = 25
                )
            )
            assertEquals(expected,result)
        }
    }

    @Test
    fun testUpdateOnePlayerCreatureNoCreatureIdSet() = testApplication {
        install(Koin) {
            modules(creaturesModule)
        }
        application {
            val creaturesService by inject<CreaturesService>()
            creaturesServiceTest = creaturesService

            try {
                creaturesServiceTest!!.updatePlayerCreature(
                    UpdatePlayerCreatureOptions(
                        creatureId = "05fb3246-9387-4004-a27f-fab107c33883",
                        xp = 25
                    )
                )
                assert(false)
            } catch (e: Exception) {
                when(e) {
                    is CreatureNotExistingException -> assert(true)
                    else -> assert(false)
                }
            }
        }
    }

    @Test
    fun testUpdateOnePlayerCreatureNotExistingIdSet() = testApplication {
        install(Koin) {
            modules(creaturesModule)
        }
        application {
            val creaturesService by inject<CreaturesService>()
            creaturesServiceTest = creaturesService

            try {
                creaturesServiceTest!!.updatePlayerCreature(
                    UpdatePlayerCreatureOptions(
                        creatureId = "",
                        xp = 25
                    )
                )
                assert(false)
            } catch (e: Exception) {
                when(e) {
                    is InvalidUUID -> assert(true)
                    else -> assert(false)
                }
            }
        }
    }

    @Test
    fun testUpdateOnePlayerCreatureMoreXPThanAllowedSet() = testApplication {
        install(Koin) {
            modules(creaturesModule)
        }
        application {
            val creaturesService by inject<CreaturesService>()
            creaturesServiceTest = creaturesService

            try {
                creaturesServiceTest!!.updatePlayerCreature(
                    UpdatePlayerCreatureOptions(
                        creatureId = "05fb3246-9387-4d04-a27f-fab107c33883",
                        xp = 36
                    )
                )
                assert(false)
            } catch (e: Exception) {
                when(e) {
                    is InvalidXPGain -> assert(true)
                    else -> assert(false)
                }
            }
        }
    }
}
