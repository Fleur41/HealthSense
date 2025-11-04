package com.sam.healthsense.Utils

import kotlinx.datetime.LocalDate

object DateUtils {
    fun isDateInPast(date: LocalDate): Boolean {
        return true // Placeholder
    }

    fun isDateValidForRegistration(date: LocalDate): Boolean {
        // Simple implementation
        return isDateInPast(date)
    }

    fun getCurrentDate(): LocalDate {
        // Return a placeholder
        return LocalDate(2024, 1, 1) // Placeholder
    }
}
