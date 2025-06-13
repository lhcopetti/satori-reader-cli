package com.copetti.srcli.domain.usecase.cli

import com.copetti.srcli.domain.gateway.SatoriReaderProvider
import com.copetti.srcli.domain.model.LoginApplicationCredentials
import com.copetti.srcli.domain.model.SatoriReaderLoginToken
import com.copetti.srcli.domain.model.TokenApplicationCredentials
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExtendWith(MockKExtension::class)
class AuthenticateUserTest {

    @MockK
    private lateinit var satoriReaderProvider: SatoriReaderProvider

    @InjectMockKs
    private lateinit var authenticateUser: AuthenticateUser

    @Test
    fun `should authenticate user successfully with login credentials`() = runTest {
        // Given
        val credentials = LoginApplicationCredentials(username = "test-user", password = "test-pass")
        val expectedToken = SatoriReaderLoginToken(sessionToken = "test-token")
        coEvery { satoriReaderProvider.login(credentials) } returns expectedToken

        // When
        val result = authenticateUser.authenticate(credentials)

        // Then
        assertEquals(expectedToken, result)
        coVerify { satoriReaderProvider.login(credentials) }
    }

    @Test
    fun `should return token directly when using token credentials`() = runTest {
        // Given
        val credentials = TokenApplicationCredentials(token = "existing-token")
        val expectedToken = SatoriReaderLoginToken(sessionToken = "existing-token")

        // When
        val result = authenticateUser.authenticate(credentials)

        // Then
        assertEquals(expectedToken, result)
        coVerify(exactly = 0) { satoriReaderProvider.login(any()) }
    }

    @Test
    fun `should propagate provider errors`() = runTest {
        // Given
        val credentials = LoginApplicationCredentials(username = "test-user", password = "test-pass")
        val expectedError = RuntimeException("Authentication failed")
        coEvery { satoriReaderProvider.login(credentials) } throws expectedError

        // When/Then
        val actualError = assertFailsWith<RuntimeException> {
            authenticateUser.authenticate(credentials)
        }
        assertEquals(expectedError, actualError)
        coVerify { satoriReaderProvider.login(credentials) }
    }

}