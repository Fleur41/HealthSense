package com.sam.healthsense.Utils

import kotlinx.datetime.LocalDate

object Validators {
    fun isValidPatientNumber(patientNumber: String): Boolean {
        return patientNumber.isNotBlank() && patientNumber.length in 3..20
    }

    fun isValidName(name: String): Boolean {
        return name.isNotBlank() && name.length in 2..50
    }

    fun isValidDate(date: LocalDate?): Boolean {
        return date != null
    }

    fun isValidHeight(height: Double): Boolean {
        return height in 50.0..250.0 // 50cm to 250cm
    }

    fun isValidWeight(weight: Double): Boolean {
        return weight in 2.0..300.0 // 2kg to 300kg
    }

    fun isValidGender(gender: String): Boolean {
        return gender.isNotBlank() && gender.length in 3..20
    }
}