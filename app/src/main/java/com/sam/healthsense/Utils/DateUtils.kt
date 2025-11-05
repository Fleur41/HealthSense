package com.sam.healthsense.Utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

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

    //kutoka hapa
    // Calculate age from birth date properly
    fun calculateAge(dateOfBirth: LocalDate): Int {
        val birthYear = dateOfBirth.year
        val currentYear = 2025 // Use actual current year

        return currentYear - birthYear
    }

    // Format date for display
    fun formatDateForDisplay(date: LocalDate): String {
        return "${date.dayOfMonth}/${date.monthNumber}/${date.year}"
    }
}
