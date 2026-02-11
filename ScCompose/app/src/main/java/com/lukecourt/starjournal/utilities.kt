package com.lukecourt.starjournal

import androidx.core.text.isDigitsOnly

fun verifyInt(value: String): Boolean {
    return value.isDigitsOnly()
}
