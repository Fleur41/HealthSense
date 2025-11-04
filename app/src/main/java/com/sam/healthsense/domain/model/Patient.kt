package com.sam.healthsense.domain.model

import kotlinx.datetime.LocalDate
import java.util.UUID

data class Patient(
    val id: String = UUID.randomUUID().toString(),
    val patientNumber: String,
    val registrationDate: LocalDate,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: LocalDate,
    val gender: String
) {
    val fullName: String
        get() = "$firstName $lastName"

    val age: Int
        get() = calculateAge(dateOfBirth)
}

// Helper function to calculate age
private fun calculateAge(dateOfBirth: LocalDate): Int {
    // return a simple calculation
    // implement proper age calculation when needed
    return kotlin.math.max(0, 2024 - dateOfBirth.year) // Using current year as placeholder
}