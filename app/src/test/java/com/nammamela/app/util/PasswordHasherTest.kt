package com.nammamela.app.util

import org.junit.Assert.assertEquals
import org.junit.Test

class PasswordHasherTest {

    @Test
    fun hashThenVerify_match() {
        val h = PasswordHasher.hash("secret123")
        assertEquals(PasswordHasher.VerifyResult.Match, PasswordHasher.verify(h, "secret123"))
        assertEquals(PasswordHasher.VerifyResult.NoMatch, PasswordHasher.verify(h, "wrong"))
    }

    @Test
    fun legacyPlaintext_recognized() {
        assertEquals(PasswordHasher.VerifyResult.LegacyPlaintextMatch, PasswordHasher.verify("plain", "plain"))
        assertEquals(PasswordHasher.VerifyResult.NoMatch, PasswordHasher.verify("plain", "other"))
    }
}
