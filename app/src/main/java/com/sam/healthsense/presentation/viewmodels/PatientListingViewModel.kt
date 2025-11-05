package com.sam.healthsense.presentation.viewmodels

import com.sam.healthsense.data.repository.PatientWithStatus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sam.healthsense.Utils.Result
import com.sam.healthsense.data.repository.IPatientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import javax.inject.Inject

@HiltViewModel
class PatientListingViewModel @Inject constructor(
    private val patientRepository: IPatientRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PatientListingUiState())
    val uiState: StateFlow<PatientListingUiState> = _uiState.asStateFlow()

    private val _patients = MutableStateFlow<List<PatientWithStatus>>(emptyList())
    val patients: StateFlow<List<PatientWithStatus>> = _patients.asStateFlow()

    private val _filterDate = MutableStateFlow<LocalDate?>(null)
    val filterDate: StateFlow<LocalDate?> = _filterDate.asStateFlow()

    init {
        loadPatients()
    }

    fun loadPatients() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val result = patientRepository.getPatientsWithLatestStatus()) {
                is Result.Success -> {
                    _patients.value = result.data
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

    fun onFilterDateChange(date: LocalDate?) {
        _filterDate.value = date
    }

    fun clearFilter() {
        _filterDate.value = null
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun refreshData() {
        loadPatients()
    }
}

data class PatientListingUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)