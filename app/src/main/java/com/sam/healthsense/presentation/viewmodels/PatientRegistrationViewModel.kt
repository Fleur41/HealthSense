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
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class PatientRegistrationViewModel @Inject constructor(
    private val patientRepository: IPatientRepository
) : ViewModel() {

    private val _registrationResult = MutableStateFlow<Result<String>?>(null) // Returns patient ID
    val registrationResult: StateFlow<Result<String>?> = _registrationResult.asStateFlow()

    fun registerPatient(
        patientNumber: String,
        registrationDate: LocalDate,
        firstName: String,
        lastName: String,
        dateOfBirth: LocalDate,
        gender: String
    ) {
        viewModelScope.launch {
            _registrationResult.value = Result.Loading

            // Check if patient number already exists
            if (patientRepository.checkPatientNumberExists(patientNumber)) {
                _registrationResult.value = Result.Error("Patient number already exists")
                return@launch
            }

            // Create patient with proper UUID
            val patient = Patient(
                id = UUID.randomUUID().toString(), // Generate proper UUID
                patientNumber = patientNumber,
                registrationDate = registrationDate,
                firstName = firstName,
                lastName = lastName,
                dateOfBirth = dateOfBirth,
                gender = gender
            )

            // Register patient and get the ID
            val result = patientRepository.registerPatient(patient)
            _registrationResult.value = result
        }
    }

    fun clearRegistrationResult() {
        _registrationResult.value = null
    }

    fun clearForm() {
        _registrationResult.value = null
    }
}
