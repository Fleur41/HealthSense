package com.sam.healthsense.domain.model

import com.sam.healthsense.Utils.DateUtils
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
        get() = DateUtils.calculateAge(dateOfBirth)

}