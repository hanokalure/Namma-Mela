package com.nammamela.app.util

import java.util.Base64
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object PasswordHasher {

    private const val PREFIX = "nm:v1:"
    private const val ITERATIONS = 120_000
    private const val KEY_LENGTH_BITS = 256
    private const val SALT_BYTES = 16

    enum class VerifyResult { Match, LegacyPlaintextMatch, NoMatch }

    fun hash(plainPassword: String): String {
        val salt = ByteArray(SALT_BYTES).also { SecureRandom().nextBytes(it) }
        val hash = pbkdf2(plainPassword.toCharArray(), salt)
        val saltB64 = Base64.getEncoder().encodeToString(salt)
        val hashB64 = Base64.getEncoder().encodeToString(hash)
        return "$PREFIX$saltB64:$hashB64"
    }

    fun verify(stored: String, plainPassword: String): VerifyResult {
        if (stored.isEmpty()) return VerifyResult.NoMatch
        if (!stored.startsWith(PREFIX)) {
            return if (stored == plainPassword) VerifyResult.LegacyPlaintextMatch
            else VerifyResult.NoMatch
        }
        val payload = stored.removePrefix(PREFIX)
        val idx = payload.indexOf(':')
        if (idx <= 0 || idx >= payload.lastIndex) return VerifyResult.NoMatch
        val saltB64 = payload.substring(0, idx)
        val hashB64 = payload.substring(idx + 1)
        return try {
            val salt = Base64.getDecoder().decode(saltB64)
            val expected = Base64.getDecoder().decode(hashB64)
            val actual = pbkdf2(plainPassword.toCharArray(), salt)
            if (actual.contentEquals(expected)) VerifyResult.Match else VerifyResult.NoMatch
        } catch (_: IllegalArgumentException) {
            VerifyResult.NoMatch
        }
    }

    private fun pbkdf2(password: CharArray, salt: ByteArray): ByteArray {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH_BITS)
        return factory.generateSecret(spec).encoded.also { spec.clearPassword() }
    }
}
