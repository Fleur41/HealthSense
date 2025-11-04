package com.sam.healthsense.domain.model

import kotlinx.datetime.LocalDate
import java.util.UUID

data class OverweightAssessment(
    val id: String = UUID.randomUUID().toString(),
    val patientId: String,
    val visitDate: LocalDate,
    val generalHealth: GeneralHealth,
    val isTakingDrugs: Boolean,
    val comments: String
)