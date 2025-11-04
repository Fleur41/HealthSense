package com.sam.healthsense.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sam.healthsense.Utils.Result
import com.sam.healthsense.data.repository.IPatientRepository
import com.sam.healthsense.data.repository.IVisitRepository
import com.sam.healthsense.domain.model.GeneralAssessment
import com.sam.healthsense.domain.model.GeneralHealth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import javax.inject.Inject

@HiltViewModel
class GeneralAssessmentViewModel @Inject constructor(
    private val visitRepository: IVisitRepository,
    private val patientRepository: IPatientRepository
) : ViewModel() {

    private val _saveAssessmentResult = MutableStateFlow<Result<Unit>?>(null)
    val saveAssessmentResult: StateFlow<Result<Unit>?> = _saveAssessmentResult.asStateFlow()

    private val _patientName = MutableStateFlow<String?>(null)
    val patientName: StateFlow<String?> = _patientName.asStateFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState.asStateFlow()

    fun loadPatientName(patientId: String) {
        viewModelScope.launch {
            _loadingState.value = true
            when (val result = patientRepository.getPatientById(patientId)) {
                is Result.Success -> {
                    _patientName.value = result.data.fullName
                    _loadingState.value = false
                }
                is Result.Error -> {
                    _patientName.value = "Patient Not Found"
                    _loadingState.value = false
                }
                is Result.Loading -> {
                    _loadingState.value = true
                }
            }
        }
    }

    fun saveGeneralAssessment(
        patientId: String,
        visitDate: LocalDate,
        generalHealth: GeneralHealth,
        hasBeenOnDiet: Boolean,
        comments: String
    ) {
        viewModelScope.launch {
            _saveAssessmentResult.value = Result.Loading

            val assessment = GeneralAssessment(
                patientId = patientId,
                visitDate = visitDate,
                generalHealth = generalHealth,
                hasBeenOnDiet = hasBeenOnDiet,
                comments = comments
            )

            val result = visitRepository.saveGeneralAssessment(assessment)
            _saveAssessmentResult.value = result
        }
    }

    fun clearSaveAssessmentResult() {
        _saveAssessmentResult.value = null
    }
}