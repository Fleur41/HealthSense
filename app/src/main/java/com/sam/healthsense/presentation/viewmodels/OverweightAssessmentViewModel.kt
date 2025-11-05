package com.sam.healthsense.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sam.healthsense.Utils.Result
import com.sam.healthsense.data.repository.IPatientRepository
import com.sam.healthsense.data.repository.IVisitRepository
import com.sam.healthsense.domain.model.GeneralHealth
import com.sam.healthsense.domain.model.OverweightAssessment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import javax.inject.Inject

@HiltViewModel
class OverweightAssessmentViewModel @Inject constructor(
    private val patientRepository: IPatientRepository,
    private val visitRepository: IVisitRepository
) : ViewModel() {

    private val _patientName = MutableStateFlow<String?>(null)
    val patientName: StateFlow<String?> = _patientName.asStateFlow()

    private val _saveResult = MutableStateFlow<Result<Unit>?>(null)
    val saveResult: StateFlow<Result<Unit>?> = _saveResult.asStateFlow()

    // Form state
    private var visitDate: LocalDate = LocalDate(2024, 1, 1) // Placeholder
    private var generalHealth: GeneralHealth = GeneralHealth.GOOD
    private var isTakingDrugs: Boolean = false
    private var comments: String = ""

    fun loadPatientName(patientId: String) {
        viewModelScope.launch {
            when (val result = patientRepository.getPatientById(patientId)) {
                is Result.Success -> {
                    _patientName.value = result.data.fullName
                }
                is Result.Error -> {
                    _saveResult.value = Result.Error(result.message)
                }
                else -> {
                }
            }
        }
    }

    fun onGeneralHealthChange(generalHealth: GeneralHealth) {
        this.generalHealth = generalHealth
    }

    fun onTakingDrugsChange(takingDrugs: Boolean) {
        this.isTakingDrugs = takingDrugs
    }

    fun onCommentsChange(comments: String) {
        this.comments = comments
    }

    fun saveAssessment(patientId: String) {
        viewModelScope.launch {
            _saveResult.value = Result.Loading

            val assessment = OverweightAssessment(
                patientId = patientId,
                visitDate = visitDate,
                generalHealth = generalHealth,
                isTakingDrugs = isTakingDrugs,
                comments = comments
            )

            val result = visitRepository.saveOverweightAssessment(assessment)
            _saveResult.value = result
        }
    }

    fun clearSaveResult() {
        _saveResult.value = null
    }
}

