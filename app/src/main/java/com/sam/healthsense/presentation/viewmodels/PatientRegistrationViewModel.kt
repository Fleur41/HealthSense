package com.sam.healthsense.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sam.healthsense.Utils.Result
import com.sam.healthsense.data.repository.IPatientRepository
import com.sam.healthsense.domain.model.Patient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import javax.inject.Inject

@HiltViewModel
class PatientRegistrationViewModel @Inject constructor(
    private val patientRepository: IPatientRepository
) : ViewModel() {

    private val _registrationResult = MutableStateFlow<Result<Unit>?>(null)
    val registrationResult: StateFlow<Result<Unit>?> = _registrationResult.asStateFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState.asStateFlow()

    fun registerPatient(
        patientNumber: String,
        registrationDate: LocalDate,
        firstName: String,
        lastName: String,
        dateOfBirth: LocalDate,
        gender: String
    ) {
        viewModelScope.launch {
            _loadingState.value = true
            _registrationResult.value = Result.Loading

            if (patientRepository.checkPatientNumberExists(patientNumber)) {
                _registrationResult.value = Result.Error("Patient number already exists")
                _loadingState.value = false
                return@launch
            }

            val patient = Patient(
                patientNumber = patientNumber,
                registrationDate = registrationDate,
                firstName = firstName,
                lastName = lastName,
                dateOfBirth = dateOfBirth,
                gender = gender
            )

            val result = patientRepository.registerPatient(patient)
            _registrationResult.value = result
            _loadingState.value = false
        }
    }

    fun clearRegistrationResult() {
        _registrationResult.value = null
    }

    fun clearForm() {
        _registrationResult.value = null
    }
}


//package com.sam.healthsense.presentation.viewmodels


//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.sam.healthsense.Utils.Result
//import com.sam.healthsense.data.repository.IPatientRepository
//import com.sam.healthsense.domain.model.Patient
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import kotlinx.datetime.LocalDate
//import javax.inject.Inject
//
//@HiltViewModel
//class PatientRegistrationViewModel @Inject constructor(
//    private val patientRepository: IPatientRepository
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(PatientRegistrationUiState())
//    val uiState: StateFlow<PatientRegistrationUiState> = _uiState.asStateFlow()
//
//    private val _registrationResult = MutableStateFlow<Result<Unit>?>(null)
//    val registrationResult: StateFlow<Result<Unit>?> = _registrationResult.asStateFlow()
//
//    fun updatePatientNumber(patientNumber: String) {
//        _uiState.value = _uiState.value.copy(
//            patientNumber = patientNumber,
//            patientNumberError = if (patientNumber.isBlank()) "Patient number is required" else null
//        )
//    }
//
//    fun updateRegistrationDate(date: LocalDate) {
//        _uiState.value = _uiState.value.copy(
//            registrationDate = date,
//            registrationDateError = if (date == null) "Registration date is required" else null
//        )
//    }
//
//    fun updateFirstName(firstName: String) {
//        _uiState.value = _uiState.value.copy(
//            firstName = firstName,
//            firstNameError = if (firstName.isBlank()) "First name is required" else null
//        )
//    }
//
//    fun updateLastName(lastName: String) {
//        _uiState.value = _uiState.value.copy(
//            lastName = lastName,
//            lastNameError = if (lastName.isBlank()) "Last name is required" else null
//        )
//    }
//
//    fun updateDateOfBirth(date: LocalDate) {
//        _uiState.value = _uiState.value.copy(
//            dateOfBirth = date,
//            dateOfBirthError = if (date == null) "Date of birth is required" else null
//        )
//    }
//
//    fun updateGender(gender: String) {
//        _uiState.value = _uiState.value.copy(
//            gender = gender,
//            genderError = if (gender.isBlank()) "Gender is required" else null
//        )
//    }
//
//    fun registerPatient() {
//        if (!isFormValid()) {
//            // Set errors for empty fields
//            _uiState.value = _uiState.value.copy(
//                patientNumberError = if (_uiState.value.patientNumber.isBlank()) "Patient number is required" else null,
//                registrationDateError = if (_uiState.value.registrationDate == null) "Registration date is required" else null,
//                firstNameError = if (_uiState.value.firstName.isBlank()) "First name is required" else null,
//                lastNameError = if (_uiState.value.lastName.isBlank()) "Last name is required" else null,
//                dateOfBirthError = if (_uiState.value.dateOfBirth == null) "Date of birth is required" else null,
//                genderError = if (_uiState.value.gender.isBlank()) "Gender is required" else null
//            )
//            return
//        }
//
//        viewModelScope.launch {
//            _registrationResult.value = Result.Loading
//
//            // Check if patient number already exists
//            if (patientRepository.checkPatientNumberExists(_uiState.value.patientNumber)) {
//                _registrationResult.value = Result.Error("Patient number already exists")
//                return@launch
//            }
//
//            val patient = Patient(
//                patientNumber = _uiState.value.patientNumber,
//                registrationDate = _uiState.value.registrationDate!!,
//                firstName = _uiState.value.firstName,
//                lastName = _uiState.value.lastName,
//                dateOfBirth = _uiState.value.dateOfBirth!!,
//                gender = _uiState.value.gender
//            )
//
//            val result = patientRepository.registerPatient(patient)
//            _registrationResult.value = result
//        }
//    }
//
//    fun clearRegistrationResult() {
//        _registrationResult.value = null
//    }
//
//    fun clearForm() {
//        _uiState.value = PatientRegistrationUiState()
//        _registrationResult.value = null
//    }
//
//    private fun isFormValid(): Boolean {
//        val state = _uiState.value
//        return state.patientNumber.isNotBlank() &&
//                state.registrationDate != null &&
//                state.firstName.isNotBlank() &&
//                state.lastName.isNotBlank() &&
//                state.dateOfBirth != null &&
//                state.gender.isNotBlank()
//    }
//}
//
//data class PatientRegistrationUiState(
//    val patientNumber: String = "",
//    val registrationDate: LocalDate? = null,
//    val firstName: String = "",
//    val lastName: String = "",
//    val dateOfBirth: LocalDate? = null,
//    val gender: String = "",
//    val patientNumberError: String? = null,
//    val registrationDateError: String? = null,
//    val firstNameError: String? = null,
//    val lastNameError: String? = null,
//    val dateOfBirthError: String? = null,
//    val genderError: String? = null
//)
//
