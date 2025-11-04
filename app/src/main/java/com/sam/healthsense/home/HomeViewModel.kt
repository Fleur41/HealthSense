package com.sam.healthsense.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sam.healthsense.data.repository.IPatientRepository
import com.sam.healthsense.data.repository.PatientWithStatus
import com.sam.healthsense.domain.model.BMIStatus
import com.sam.healthsense.Utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val patientRepository: IPatientRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _patientStats = MutableStateFlow<PatientStats?>(null)
    val patientStats: StateFlow<PatientStats?> = _patientStats.asStateFlow()

    init {
        loadPatientStats()
    }

    fun loadPatientStats() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            when (val result = patientRepository.getPatientsWithLatestStatus()) {
                is Result.Success -> {
                    val patients = result.data
                    val stats = calculatePatientStats(patients)
                    _patientStats.value = stats
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = null
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Result.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    private fun calculatePatientStats(patients: List<PatientWithStatus>): PatientStats {
        val totalPatients = patients.size
        val normalWeight = patients.count { it.latestBmiStatus == BMIStatus.NORMAL }
        val overweight = patients.count { it.latestBmiStatus == BMIStatus.OVERWEIGHT }
        val underweight = patients.count { it.latestBmiStatus == BMIStatus.UNDERWEIGHT }

        return PatientStats(
            totalPatients = totalPatients,
            normalWeight = normalWeight,
            overweight = overweight,
            underweight = underweight
        )
    }
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)

data class PatientStats(
    val totalPatients: Int,
    val normalWeight: Int,
    val overweight: Int,
    val underweight: Int
) {
    val hasPatients: Boolean get() = totalPatients > 0
}
//@HiltViewModel
//class HomeViewModel @Inject constructor(
//    private val homeRepository: HomeRepository
//) : ViewModel() {
//    fun logDetailScreenViewEvent(){
//        homeRepository.logDetailScreenViewEvent()
//    }
//}
