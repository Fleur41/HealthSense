package com.sam.healthsense.domain.model

import kotlinx.datetime.LocalDate
import java.util.UUID

data class PatientVitals(
    val id: String = UUID.randomUUID().toString(),
    val patientId: String,
    val visitDate: LocalDate,
    val height: Double, // in cm
    val weight: Double, // in kg
    val bmi: Double
) {
    val bmiStatus: BMIStatus
        get() = when {
            bmi < 18.5 -> BMIStatus.UNDERWEIGHT
            bmi < 25 -> BMIStatus.NORMAL
            else -> BMIStatus.OVERWEIGHT
        }
}