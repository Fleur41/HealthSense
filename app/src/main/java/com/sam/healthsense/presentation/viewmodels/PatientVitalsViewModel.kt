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
import java.util.UUID
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
            println("🟡 DEBUG: loadPatientName called with patientId: '$patientId'")
            println("🟡 DEBUG: patientId length: ${patientId.length}")
            println("🟡 DEBUG: patientId is blank: ${patientId.isBlank()}")

            _loadingState.value = true
            when (val result = patientRepository.getPatientById(patientId)) {
                is Result.Success -> {
                    println("🟢 DEBUG: Patient FOUND!")
                    println("🟢 DEBUG: Patient name: ${result.data.fullName}")
                    println("🟢 DEBUG: Patient ID from DB: ${result.data.id}")
                    println("🟢 DEBUG: Patient ID match: ${result.data.id == patientId}")

                    _patientName.value = result.data.fullName
                    _loadingState.value = false
                }
                is Result.Error -> {
                    println("🔴 DEBUG: Patient NOT found for ID: '$patientId'")
                    println("🔴 DEBUG: Error message: ${result.message}")

                    // Debug: Let's check all patients in database
                    debugGetAllPatients()

                    _patientName.value = "Patient Not Found"
                    _loadingState.value = false
                }
                is Result.Loading -> {
                    println("🟡 DEBUG: Loading patient...")
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
            println("🟡 DEBUG: savePatientVitals called with patientId: '$patientId'")
            println("🟡 DEBUG: Parameters - height: $height, weight: $weight, bmi: $bmi")

            _saveVitalsResult.value = Result.Loading

            // First, let's verify the patient exists
            println("🟡 DEBUG: Step 1 - Verifying patient exists...")
            when (val patientResult = patientRepository.getPatientById(patientId)) {
                is Result.Success -> {
                    println("🟢 DEBUG: Step 1 - Patient VERIFIED!")
                    println("🟢 DEBUG: Patient name: ${patientResult.data.fullName}")
                    println("🟢 DEBUG: Patient ID in DB: ${patientResult.data.id}")

                    val vitals = PatientVitals(
                        id = UUID.randomUUID().toString(),
                        patientId = patientId,
                        visitDate = visitDate,
                        height = height,
                        weight = weight,
                        bmi = bmi
                    )

                    println("🟡 DEBUG: Step 2 - Created vitals object:")
                    println("🟡 DEBUG: Vitals ID: ${vitals.id}")
                    println("🟡 DEBUG: Vitals patientId: ${vitals.patientId}")
                    println("🟡 DEBUG: Vitals visitDate: ${vitals.visitDate}")

                    println("🟡 DEBUG: Step 3 - Attempting to save vitals to database...")
                    val result = visitRepository.savePatientVitals(vitals)
                    _saveVitalsResult.value = result

                    when (result) {
                        is Result.Success -> {
                            println("🟢 DEBUG: ✅ Vitals saved SUCCESSFULLY!")
                        }
                        is Result.Error -> {
                            println("🔴 DEBUG: ❌ Error saving vitals: ${result.message}")
                            println("🔴 DEBUG: Full error details:")
                            result.message?.let { message ->
                                println("🔴 DEBUG: Error message: $message")
                            }
                        }
                        is Result.Loading -> {
                            println("🟡 DEBUG: Still saving vitals...")
                        }
                    }
                }
                is Result.Error -> {
                    println("🔴 DEBUG: ❌ Patient verification FAILED!")
                    println("🔴 DEBUG: Error: ${patientResult.message}")

                    // Debug all patients again
                    debugGetAllPatients()

                    _saveVitalsResult.value = Result.Error("Patient not found: ${patientResult.message}")
                }
                else -> {
                    println("🔴 DEBUG: ❌ Unknown patient verification state")
                    _saveVitalsResult.value = Result.Error("Unable to verify patient")
                }
            }
        }
    }

    fun clearSaveVitalsResult() {
        println("🟡 DEBUG: Clearing save vitals result")
        _saveVitalsResult.value = null
    }

    fun calculateBMI(height: Double, weight: Double): Double {
        if (height <= 0 || weight <= 0) return 0.0
        val heightMeters = height / 100
        return String.format("%.2f", weight / (heightMeters * heightMeters)).toDouble()
    }

    // Debug function to check all patients in database
    suspend fun debugGetAllPatients() {
        println("🔍 DEBUG: Checking all patients in database...")
        when (val result = patientRepository.getAllPatients()) {
            is Result.Success -> {
                val patients = result.data
                println("🔍 DEBUG: Found ${patients.size} patients in database:")
                patients.forEachIndexed { index, patient ->
                    println("🔍 DEBUG: Patient $index: ID='${patient.id}', Name='${patient.fullName}'")
                }

                if (patients.isEmpty()) {
                    println("🔍 DEBUG: ⚠️ Database is EMPTY - no patients found!")
                }
            }
            is Result.Error -> {
                println("🔍 DEBUG: ❌ Error fetching patients: ${result.message}")
            }
            else -> {
                println("🔍 DEBUG: ⚠️ Unknown state when fetching patients")
            }
        }
    }

    // Additional debug function to check patient by ID directly
    fun debugCheckPatient(patientId: String) {
        viewModelScope.launch {
            println("🔍 DEBUG: Direct patient check for ID: '$patientId'")
            when (val result = patientRepository.getPatientById(patientId)) {
                is Result.Success -> {
                    println("🔍 DEBUG: ✅ Direct check - Patient EXISTS: ${result.data.fullName}")
                }
                is Result.Error -> {
                    println("🔍 DEBUG: ❌ Direct check - Patient NOT FOUND")
                    debugGetAllPatients()
                }
                else -> {
                    println("🔍 DEBUG: ⚠️ Direct check - Unknown state")
                }
            }
        }
    }
}
//@HiltViewModel
//class PatientVitalsViewModel @Inject constructor(
//    private val visitRepository: IVisitRepository,
//    private val patientRepository: IPatientRepository
//) : ViewModel() {
//
//    private val _saveVitalsResult = MutableStateFlow<Result<Unit>?>(null)
//    val saveVitalsResult: StateFlow<Result<Unit>?> = _saveVitalsResult.asStateFlow()
//
//    private val _patientName = MutableStateFlow<String?>(null)
//    val patientName: StateFlow<String?> = _patientName.asStateFlow()
//
//    private val _loadingState = MutableStateFlow(false)
//    val loadingState: StateFlow<Boolean> = _loadingState.asStateFlow()
//
//    fun loadPatientName(patientId: String) {
//        viewModelScope.launch {
//            _loadingState.value = true
//            when (val result = patientRepository.getPatientById(patientId)) {
//                is Result.Success -> {
//                    _patientName.value = result.data.fullName
//                    _loadingState.value = false
//                }
//                is Result.Error -> {
//                    _patientName.value = "Patient Not Found"
//                    _loadingState.value = false
//                }
//                is Result.Loading -> {
//                    _loadingState.value = true
//                }
//            }
//        }
//    }
//
//    fun savePatientVitals(
//        patientId: String,
//        visitDate: LocalDate,
//        height: Double,
//        weight: Double,
//        bmi: Double
//    ) {
//        viewModelScope.launch {
//            _saveVitalsResult.value = Result.Loading
//
//            val vitals = PatientVitals(
//                patientId = patientId,
//                visitDate = visitDate,
//                height = height,
//                weight = weight,
//                bmi = bmi
//            )
//
//            val result = visitRepository.savePatientVitals(vitals)
//            _saveVitalsResult.value = result
//        }
//    }
//
//    fun clearSaveVitalsResult() {
//        _saveVitalsResult.value = null
//    }
//
//    fun calculateBMI(height: Double, weight: Double): Double {
//        if (height <= 0 || weight <= 0) return 0.0
//        val heightMeters = height / 100
//        return String.format("%.2f", weight / (heightMeters * heightMeters)).toDouble()
//    }
//}
