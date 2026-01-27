package com.lukecourt.scjournal

import androidx.core.text.isDigitsOnly

fun verifyInt(value: String): Boolean {
    return value.isDigitsOnly()
}
