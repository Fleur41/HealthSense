package com.sam.healthsense.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

@Entity(
    tableName = "overweight_assessments",
    foreignKeys = [
        ForeignKey(
            entity = PatientEntity::class,
            parentColumns = ["id"],
            childColumns = ["patientId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class OverweightAssessmentEntity(
    @PrimaryKey
    val id: String,
    val patientId: String,
    val visitDate: LocalDate,
    val generalHealth: String, // "GOOD" or "POOR"
    val isTakingDrugs: Boolean,
    val comments: String
)