package com.sam.healthsense.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "general_assessments",
    foreignKeys = [
        ForeignKey(
            entity = PatientEntity::class,
            parentColumns = ["id"],
            childColumns = ["patientId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GeneralAssessmentEntity(
    @PrimaryKey
    val id: String,
    val patientId: String,
    val visitDate: LocalDate,
    val generalHealth: String, // "GOOD" or "POOR"
    val hasBeenOnDiet: Boolean,
    val comments: String
)