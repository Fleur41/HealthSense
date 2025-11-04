package com.sam.healthsense.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "patients")
data class PatientEntity(
    @PrimaryKey
    val id: String,
    val patientNumber: String,
    val registrationDate: LocalDate,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: LocalDate,
    val gender: String
)