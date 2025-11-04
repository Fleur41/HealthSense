package com.sam.healthsense.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sam.healthsense.Utils.Result
import com.sam.healthsense.data.repository.IPatientRepository
import com.sam.healthsense.data.repository.IVisitRepository
import com.sam.healthsense.domain.model.PatientVitals
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import javax.inject.Inject

@HiltViewModel
class PatientVitalsViewModel @Inject constructor(
    private val visitRepository: IVisitRepository,
    private val patientRepository: IPatientRepository
) : ViewModel() {

    private val _saveVitalsResult = MutableStateFlow<Result<Unit>?>(null)
    val saveVitalsResult: StateFlow<Result<Unit>?> = _saveVitalsResult.asStateFlow()

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

    fun savePatientVitals(
        patientId: String,
        visitDate: LocalDate,
        height: Double,
        weight: Double,
        bmi: Double
    ) {
        viewModelScope.launch {
            _saveVitalsResult.value = Result.Loading

            val vitals = PatientVitals(
                patientId = patientId,
                visitDate = visitDate,
                height = height,
                weight = weight,
                bmi = bmi
            )

            val result = visitRepository.savePatientVitals(vitals)
            _saveVitalsResult.value = result
        }
    }

    fun clearSaveVitalsResult() {
        _saveVitalsResult.value = null
    }

    fun calculateBMI(height: Double, weight: Double): Double {
        if (height <= 0 || weight <= 0) return 0.0
        val heightMeters = height / 100
        return String.format("%.2f", weight / (heightMeters * heightMeters)).toDouble()
    }
}
