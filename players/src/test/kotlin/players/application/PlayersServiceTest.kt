package com.jatec.players.application

import com.jatec.players.application.dtos.MessageOptions
import com.jatec.players.application.service.PlayersService
import com.jatec.players.application.exceptions.PlayerExistingException
import com.jatec.players.domain.exceptions.InvalidUUID
import com.jatec.players.domain.exceptions.InvalidName
import com.jatec.players.domain.model.Player
import com.jatec.players.domain.options.WhereOptions
import com.jatec.players.domain.repository.PlayersRepository
import com.jatec.players.repository.PlayersRepositoryTest
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import java.util.UUID
import kotlin.getValue
import kotlin.test.Test
import kotlin.test.assertContentEquals


val playersModule = module {
    singleOf(::PlayersRepositoryTest) bind PlayersRepository::class
    singleOf(::PlayersService)
}

class PlayersServiceTest {
    var playersServiceTest: PlayersService? = null
    @Test
    fun testFindAllNull() = testApplication {
        runBlocking {
            install(Koin) {
                modules(playersModule)
            }
            application {
                val playersService by inject<PlayersService>()
                playersServiceTest = playersService

                val expected = listOf<Player>(
                    Player(id = "05fb3246-9387-4d04-a27f-fab107c33883", name = "Player test 1"),
                    Player(id = "05fb3246-9387-4d04-a2cf-fa0107433883", name = "Player test 2")
                )
                val players = playersServiceTest!!.find(
                    MessageOptions(
                        WhereOptions(
                            playerId = null,
                            name = null,
                            creatureId = null
                        )
                    )
                )
                assertContentEquals(players, expected)
            }
        }
    }

    @Test
    fun testFindAllPlayerIdPlayer1Set() = testApplication {
        install(Koin) {
            modules(playersModule)
        }
        application {
            val playersService by inject<PlayersService>()
            playersServiceTest = playersService

            val expected = listOf<Player>(
                Player(id = "05fb3246-9387-4d04-a27f-fab107c33883", name = "Player test 1")
            )
            val players = playersServiceTest!!.find(
                MessageOptions(
                    WhereOptions(
                        playerId = "05fb3246-9387-4d04-a27f-fab107c33883",
                        name = null,
                        creatureId = null
                    )
                )
            )
            assertContentEquals(players, expected)
        }
    }

    @Test
    fun testFindAllPlayerNamePlayer2Set() = testApplication {
        install(Koin) {
            modules(playersModule)
        }
        application {
            val playersService by inject<PlayersService>()
            playersServiceTest = playersService

            val expected = listOf<Player>(
                Player(id = "05fb3246-9387-4d04-a2cf-fa0107433883", name = "Player test 2")
            )
            val players = playersServiceTest!!.find(
                MessageOptions(
                    WhereOptions(
                        playerId = null,
                        name = "Player test 2",
                        creatureId = null
                    )
                )
            )
            assertContentEquals(players, expected)
        }
    }

    @Test
    fun testFindAllPlayerNamePlayer2IdPlayer1Set() = testApplication {
        install(Koin) {
            modules(playersModule)
        }
        application {
            val playersService by inject<PlayersService>()
            playersServiceTest = playersService

            val expected = emptyList<Player>()
            val players = playersServiceTest!!.find(
                MessageOptions(
                    WhereOptions(
                        playerId = "05fb3246-9387-4d04-a27f-fab107c33883",
                        name = "Player test 2",
                        creatureId = null
                    )
                )
            )
            assertContentEquals(players, expected)
        }
    }

    @Test
    fun testFindAllThrowBadNameException() = testApplication {
        install(Koin) {
            modules(playersModule)
        }
        application {
            val playersService by inject<PlayersService>()
            playersServiceTest = playersService

            try {
                playersServiceTest!!.find(
                    MessageOptions(
                        WhereOptions(
                            playerId = null,
                            name = "Pl",
                            creatureId = null
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
    fun testFindAllThrowBadUUIDException() = testApplication {
        install(Koin) {
            modules(playersModule)
        }
        application {
            val playersService by inject<PlayersService>()
            playersServiceTest = playersService

            try {
                playersServiceTest!!.find(
                    MessageOptions(
                        WhereOptions(
                            playerId = "0987-4d04-a27f-fab107c33883",
                            name = null,
                            creatureId = null
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
    fun testCreateOnePlayerNewNameSet() = testApplication {
        install(Koin) {
            modules(playersModule)
        }
        application {
            val playersService by inject<PlayersService>()
            playersServiceTest = playersService

            val result = playersServiceTest!!.create(
                Player(id = UUID.randomUUID().toString(), name = "New player")
            )
            assert(result)
        }
    }

    @Test
    fun testCreateOnePlayerNoNameSet() = testApplication {
        install(Koin) {
            modules(playersModule)
        }
        application {
            val playersService by inject<PlayersService>()
            playersServiceTest = playersService

            try {
                playersServiceTest!!.create(
                    Player(id = UUID.randomUUID().toString(), name = "")
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
    fun testCreateOnePlayerBadNameSet() = testApplication {
        install(Koin) {
            modules(playersModule)
        }
        application {
            val playersService by inject<PlayersService>()
            playersServiceTest = playersService

            try {
                playersServiceTest!!.create(
                    Player(id = UUID.randomUUID().toString(), name = "Play")
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
    fun testCreateOnePlayerExistingNameSet() = testApplication {
        install(Koin) {
            modules(playersModule)
        }
        application {
            val playersService by inject<PlayersService>()
            playersServiceTest = playersService

            try {
                playersServiceTest!!.create(
                    Player(id = UUID.randomUUID().toString(), name = "Player test 1")
                )
                assert(false)
            } catch (e: Exception) {
                when(e) {
                    is PlayerExistingException -> assert(true)
                    else -> assert(false)
                }
            }
        }
    }

    @Test
    fun testCreateOnePlayerNoIdSet() = testApplication {
        install(Koin) {
            modules(playersModule)
        }
        application {
            val playersService by inject<PlayersService>()
            playersServiceTest = playersService

            try {
                playersServiceTest!!.create(
                    Player(id = "", name = "Player creation")
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
    fun testCreateOnePlayerBadIdSet() = testApplication {
        install(Koin) {
            modules(playersModule)
        }
        application {
            val playersService by inject<PlayersService>()
            playersServiceTest = playersService

            try {
                playersServiceTest!!.create(
                    Player(id = "00978675758-86", name = "Player creation")
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
}
