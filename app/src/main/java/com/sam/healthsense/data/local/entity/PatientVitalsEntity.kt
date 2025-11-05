package com.sam.healthsense.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

@Entity(
    tableName = "patient_vitals",
    foreignKeys = [
        ForeignKey(
            entity = PatientEntity::class,
            parentColumns = ["id"],
            childColumns = ["patientId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PatientVitalsEntity(
    @PrimaryKey
    val id: String,
    val patientId: String,
    val visitDate: LocalDate,
    val height: Double, // in cm
    val weight: Double, // in kg
    val bmi: Double
)
//@Entity(
//    tableName = "patient_vitals",
//    foreignKeys = [
//        ForeignKey(
//            entity = PatientEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["patientId"],
//            onDelete = ForeignKey.CASCADE
//        )
//    ]
//)
//data class PatientVitalsEntity(
//    @PrimaryKey
//    val id: String,
//    val patientId: String,
//    val visitDate: LocalDate,
//    val height: Double, // in cm
//    val weight: Double, // in kg
//    val bmi: Double
//)