package com.netchess.pkiykov.core.utils

import android.util.Patterns
import java.util.regex.Pattern

class TextValidator {

    private val usernamePattern = Pattern.compile(USERNAME_PATTERN)
    private val passwordPattern = Pattern.compile(PASSWORD_PATTERN)

    fun validateUsername(username: CharSequence): Boolean =
            username.isNotEmpty() && usernamePattern.matcher(username).matches()

    fun validatePassword(password: CharSequence): Boolean =
            password.isNotEmpty() && passwordPattern.matcher(password).matches()

    fun validateEmail(email: CharSequence): Boolean =
            email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun softCredentialValidation(vararg text: CharSequence): Boolean = text.firstOrNull { it.length < 4 } == null

    companion object {

        const val USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$"
        const val PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})"
    }

}