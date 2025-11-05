package com.sam.healthsense.presentation.screens

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sam.healthsense.Utils.Result
import com.sam.healthsense.presentation.viewmodels.PatientVitalsViewModel
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientVitalsScreen(
    patientId: String,
    onBack: () -> Unit,
    onVitalsSaved: (String, Double) -> Unit,
    viewModel: PatientVitalsViewModel = hiltViewModel()
) {
    // Local states
    var patientName by remember { mutableStateOf("") }
    var visitDate by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var bmi by remember { mutableStateOf("") }

    // Error states
    var isPatientNameError by remember { mutableStateOf(false) }
    var isVisitDateError by remember { mutableStateOf(false) }
    var isHeightError by remember { mutableStateOf(false) }
    var isWeightError by remember { mutableStateOf(false) }

    // ViewModel states
    val saveVitalsResult by viewModel.saveVitalsResult.collectAsState()
    val loadedPatientName by viewModel.patientName.collectAsState()
    val loadingState by viewModel.loadingState.collectAsState()

    // Context for Toast
    val context = LocalContext.current

    // Debug logging for the patientId parameter - ACTUAL CODE IMPLEMENTATION
    LaunchedEffect(patientId) {
        println("🟡 DEBUG: PatientVitalsScreen received patientId: '$patientId'")
        println("🟡 DEBUG: patientId length: ${patientId.length}")
        println("🟡 DEBUG: patientId is blank: ${patientId.isBlank()}")

        viewModel.loadPatientName(patientId)

        // Temporary: Debug all patient IDs - ACTUAL CODE IMPLEMENTATION
        viewModel.viewModelScope.launch {
            viewModel.debugGetAllPatients()
            viewModel.debugCheckPatient(patientId)
        }
    }

    // Update the patient name field when loaded from ViewModel
    LaunchedEffect(loadedPatientName) {
        if (loadedPatientName != null) {
            println("🟢 DEBUG: Setting patient name field to: '${loadedPatientName}'")
            patientName = loadedPatientName!!
        } else {
            println("🔴 DEBUG: loadedPatientName is null")
        }
    }

    // Clear error function
    fun clearError() {
        isPatientNameError = false
        isVisitDateError = false
        isHeightError = false
        isWeightError = false
    }

    // Calculate BMI function
    fun calculateBMI() {
        clearError()
        val heightValue = height.toDoubleOrNull() ?: 0.0
        val weightValue = weight.toDoubleOrNull() ?: 0.0

        if (heightValue > 0 && weightValue > 0) {
            val calculatedBMI = viewModel.calculateBMI(heightValue, weightValue)
            bmi = String.format("%.2f", calculatedBMI)
            println("🟡 DEBUG: BMI calculated: $bmi from height: $heightValue, weight: $weightValue")
        } else {
            bmi = ""
        }
    }

    // Save vitals function
    fun saveVitals() {
        clearError()

        var hasError = false

        if (patientName.isBlank()) {
            isPatientNameError = true
            hasError = true
        }
        if (visitDate.isBlank()) {
            isVisitDateError = true
            hasError = true
        }
        if (height.isBlank()) {
            isHeightError = true
            hasError = true
        }
        if (weight.isBlank()) {
            isWeightError = true
            hasError = true
        }

        if (hasError) {
            println("🔴 DEBUG: Form validation failed - missing required fields")
            Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_LONG).show()
            return
        }

        val heightValue = height.toDoubleOrNull() ?: 0.0
        val weightValue = weight.toDoubleOrNull() ?: 0.0
        val bmiValue = bmi.toDoubleOrNull() ?: 0.0

        if (heightValue <= 0 || weightValue <= 0 || bmiValue <= 0) {
            println("🔴 DEBUG: Invalid height/weight values: height=$heightValue, weight=$weightValue")
            Toast.makeText(context, "Please enter valid height and weight", Toast.LENGTH_LONG).show()
            return
        }

        // Use current date as placeholder for LocalDate
        val currentDate = LocalDate(2024, 1, 1) // Placeholder date
        println("🟡 DEBUG: Calling savePatientVitals with patientId: '$patientId'")
        viewModel.savePatientVitals(
            patientId = patientId,
            visitDate = currentDate,
            height = heightValue,
            weight = weightValue,
            bmi = bmiValue
        )
    }

    // Handle successful vitals save
    LaunchedEffect(saveVitalsResult) {
        when (saveVitalsResult) {
            is Result.Success -> {
                println("🟢 DEBUG: Vitals saved successfully, triggering navigation...")
                val bmiValue = bmi.toDoubleOrNull() ?: 0.0
                onVitalsSaved(patientId, bmiValue)
            }
            is Result.Error -> {
                println("🔴 DEBUG: Save failed: ${(saveVitalsResult as Result.Error).message}")
                // Error is already shown in the UI
            }
            is Result.Loading -> {
                println("🟡 DEBUG: Save in progress...")
            }
            null -> {
                // Initial state, do nothing
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Patient Vitals Form",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        println("🟡 DEBUG: Back button pressed")
                        onBack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Yellow.copy(alpha = 0.3f) // Light yellow background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Vitals Form - Exact order as per mockup
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Patient Name (First field as per mockup)
                OutlinedTextField(
                    value = patientName,
                    onValueChange = {
                        patientName = it
                        isPatientNameError = it.isBlank()
                    },
                    label = { Text("Patient Name") },
                    placeholder = {
                        if (loadingState) {
                            Text("Loading patient name...")
                        } else {
                            Text("Enter patient name")
                        }
                    },
                    isError = isPatientNameError,
                    supportingText = {
                        if (isPatientNameError) {
                            Text("Patient name is required", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = loadingState // Read-only while loading
                )

                // Visit Date
                OutlinedTextField(
                    value = visitDate,
                    onValueChange = {
                        visitDate = it
                        isVisitDateError = it.isBlank()
                    },
                    label = { Text("Visit Date") },
                    placeholder = { Text("DD/MM/YYYY") },
                    isError = isVisitDateError,
                    supportingText = {
                        if (isVisitDateError) {
                            Text("Visit date is required", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Height
                OutlinedTextField(
                    value = height,
                    onValueChange = {
                        height = it
                        isHeightError = it.isBlank()
                        calculateBMI()
                    },
                    label = { Text("Height") },
                    placeholder = { Text("Enter height in centimeters") },
                    isError = isHeightError,
                    supportingText = {
                        if (isHeightError) {
                            Text("Height is required", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Weight
                OutlinedTextField(
                    value = weight,
                    onValueChange = {
                        weight = it
                        isWeightError = it.isBlank()
                        calculateBMI()
                    },
                    label = { Text("Weight") },
                    placeholder = { Text("Enter weight in kilograms") },
                    isError = isWeightError,
                    supportingText = {
                        if (isWeightError) {
                            Text("Weight is required", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // BMI (Auto-calculated, read-only)
                OutlinedTextField(
                    value = bmi,
                    onValueChange = { },
                    label = { Text("BMI") },
                    placeholder = { Text("BMI will be calculated automatically") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    supportingText = {
                        if (bmi.isNotBlank()) {
                            val bmiValue = bmi.toDoubleOrNull() ?: 0.0
                            val status = when {
                                bmiValue < 18.5 -> "Underweight"
                                bmiValue < 25 -> "Normal"
                                else -> "Overweight"
                            }
                            Text("BMI Status: $status")
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Two Buttons in a Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Close Button (Left)
                    Button(
                        onClick = {
                            println("🟡 DEBUG: Close button pressed")
                            onBack()
                        },
                        modifier = Modifier.weight(1f),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Text("Close")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Save Button (Right)
                    Button(
                        onClick = {
                            println("🟡 DEBUG: Save button pressed")
                            saveVitals()
                        },
                        modifier = Modifier.weight(1f),
                        enabled = saveVitalsResult !is Result.Loading
                    ) {
                        Text("Save")
                    }
                }

                // Show save result
                saveVitalsResult?.let { result ->
                    Spacer(modifier = Modifier.height(16.dp))
                    when (result) {
                        is Result.Success -> {
                            Text(
                                text = "Vitals saved successfully!",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        is Result.Error -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Error: ${result.message}",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        println("🟡 DEBUG: Dismiss error button pressed")
                                        viewModel.clearSaveVitalsResult()
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Dismiss")
                                }
                            }
                        }
                        is Result.Loading -> {
                            Text("Saving vitals...")
                        }
                    }
                }
            }
        }
    }
}

//import android.widget.Toast
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import com.sam.healthsense.Utils.Result
//import com.sam.healthsense.presentation.viewmodels.PatientVitalsViewModel
//import kotlinx.datetime.LocalDate
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun PatientVitalsScreen(
//    patientId: String,
//    onBack: () -> Unit,
//    onVitalsSaved: (String, Double) -> Unit,
//    viewModel: PatientVitalsViewModel = hiltViewModel()
//) {
//    // Local states
//    var patientName by remember { mutableStateOf("") }
//    var visitDate by remember { mutableStateOf("") }
//    var height by remember { mutableStateOf("") }
//    var weight by remember { mutableStateOf("") }
//    var bmi by remember { mutableStateOf("") }
//
//
//    // Error states
//    var isPatientNameError by remember { mutableStateOf(false) }
//    var isVisitDateError by remember { mutableStateOf(false) }
//    var isHeightError by remember { mutableStateOf(false) }
//    var isWeightError by remember { mutableStateOf(false) }
//
//    // ViewModel states
//    val saveVitalsResult by viewModel.saveVitalsResult.collectAsState()
//    val loadedPatientName by viewModel.patientName.collectAsState()
//    val loadingState by viewModel.loadingState.collectAsState()
//
//    // Context for Toast
//    val context = LocalContext.current
//
//    // Load patient name when screen opens and set it to the field
//    LaunchedEffect(patientId) {
//        viewModel.loadPatientName(patientId)
//    }
//
//    // Update the patient name field when loaded from ViewModel
//    LaunchedEffect(loadedPatientName) {
//        if (loadedPatientName != null) {
//            patientName = loadedPatientName!!
//        }
//    }
//
//    // Clear error function
//    fun clearError() {
//        isPatientNameError = false
//        isVisitDateError = false
//        isHeightError = false
//        isWeightError = false
//    }
//
//    // Calculate BMI function
//    fun calculateBMI() {
//        clearError()
//        val heightValue = height.toDoubleOrNull() ?: 0.0
//        val weightValue = weight.toDoubleOrNull() ?: 0.0
//
//        if (heightValue > 0 && weightValue > 0) {
//            val calculatedBMI = viewModel.calculateBMI(heightValue, weightValue)
//            bmi = String.format("%.2f", calculatedBMI)
//        } else {
//            bmi = ""
//        }
//    }
//
//    // Save vitals function
//    fun saveVitals() {
//        clearError()
//
//        var hasError = false
//
//        if (patientName.isBlank()) {
//            isPatientNameError = true
//            hasError = true
//        }
//        if (visitDate.isBlank()) {
//            isVisitDateError = true
//            hasError = true
//        }
//        if (height.isBlank()) {
//            isHeightError = true
//            hasError = true
//        }
//        if (weight.isBlank()) {
//            isWeightError = true
//            hasError = true
//        }
//
//        if (hasError) {
//            Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_LONG).show()
//            return
//        }
//
//        val heightValue = height.toDoubleOrNull() ?: 0.0
//        val weightValue = weight.toDoubleOrNull() ?: 0.0
//        val bmiValue = bmi.toDoubleOrNull() ?: 0.0
//
//        if (heightValue <= 0 || weightValue <= 0 || bmiValue <= 0) {
//            Toast.makeText(context, "Please enter valid height and weight", Toast.LENGTH_LONG).show()
//            return
//        }
//
//        // Use current date as placeholder for LocalDate
//        val currentDate = LocalDate(2024, 1, 1) // Placeholder date
//        viewModel.savePatientVitals(
//            patientId = patientId,
//            visitDate = currentDate,
//            height = heightValue,
//            weight = weightValue,
//            bmi = bmiValue
//        )
//    }
//
//    // Handle successful vitals save
//    LaunchedEffect(saveVitalsResult) {
//        if (saveVitalsResult is Result.Success) {
//            val bmiValue = bmi.toDoubleOrNull() ?: 0.0
//            onVitalsSaved(patientId, bmiValue)
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = "Patient Vitals Form",
//                        style = MaterialTheme.typography.headlineSmall,
//                        fontWeight = FontWeight.Bold
//                    )
//                },
//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(
//                            imageVector = Icons.Default.ArrowBack,
//                            contentDescription = "Back"
//                        )
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = Color.Yellow.copy(alpha = 0.3f) // Light yellow background
//                )
//            )
//        }
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .padding(16.dp)
//                .verticalScroll(rememberScrollState())
//        ) {
//            // Vitals Form - Exact order as per mockup
//            Column(
//                verticalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                // Patient Name (First field as per mockup)
//                OutlinedTextField(
//                    value = patientName,
//                    onValueChange = {
//                        patientName = it
//                        isPatientNameError = it.isBlank()
//                    },
//                    label = { Text("Patient Name") },
//                    placeholder = {
//                        if (loadingState) {
//                            Text("Loading patient name...")
//                        } else {
//                            Text("Enter patient name")
//                        }
//                    },
//                    isError = isPatientNameError,
//                    supportingText = {
//                        if (isPatientNameError) {
//                            Text("Patient name is required", color = MaterialTheme.colorScheme.error)
//                        }
//                    },
//                    modifier = Modifier.fillMaxWidth(),
//                    readOnly = loadingState // Read-only while loading
//                )
//
//                // Visit Date
//                OutlinedTextField(
//                    value = visitDate,
//                    onValueChange = {
//                        visitDate = it
//                        isVisitDateError = it.isBlank()
//                    },
//                    label = { Text("Visit Date") },
//                    placeholder = { Text("DD/MM/YYYY") },
//                    isError = isVisitDateError,
//                    supportingText = {
//                        if (isVisitDateError) {
//                            Text("Visit date is required", color = MaterialTheme.colorScheme.error)
//                        }
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                // Height
//                OutlinedTextField(
//                    value = height,
//                    onValueChange = {
//                        height = it
//                        isHeightError = it.isBlank()
//                        calculateBMI()
//                    },
//                    label = { Text("Height") },
//                    placeholder = { Text("Enter height in centimeters") },
//                    isError = isHeightError,
//                    supportingText = {
//                        if (isHeightError) {
//                            Text("Height is required", color = MaterialTheme.colorScheme.error)
//                        }
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                // Weight
//                OutlinedTextField(
//                    value = weight,
//                    onValueChange = {
//                        weight = it
//                        isWeightError = it.isBlank()
//                        calculateBMI()
//                    },
//                    label = { Text("Weight") },
//                    placeholder = { Text("Enter weight in kilograms") },
//                    isError = isWeightError,
//                    supportingText = {
//                        if (isWeightError) {
//                            Text("Weight is required", color = MaterialTheme.colorScheme.error)
//                        }
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                // BMI (Auto-calculated, read-only)
//                OutlinedTextField(
//                    value = bmi,
//                    onValueChange = { },
//                    label = { Text("BMI") },
//                    placeholder = { Text("BMI will be calculated automatically") },
//                    modifier = Modifier.fillMaxWidth(),
//                    readOnly = true,
//                    supportingText = {
//                        if (bmi.isNotBlank()) {
//                            val bmiValue = bmi.toDoubleOrNull() ?: 0.0
//                            val status = when {
//                                bmiValue < 18.5 -> "Underweight"
//                                bmiValue < 25 -> "Normal"
//                                else -> "Overweight"
//                            }
//                            Text("BMI Status: $status")
//                        }
//                    }
//                )
//
//                Spacer(modifier = Modifier.height(24.dp))
//
//                // Two Buttons in a Row
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    // Close Button (Left)
//                    Button(
//                        onClick = {
//                            onBack()
//                        },
//                        modifier = Modifier.weight(1f),
//                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
//                            containerColor = MaterialTheme.colorScheme.errorContainer,
//                            contentColor = MaterialTheme.colorScheme.onErrorContainer
//                        )
//                    ) {
//                        Text("Close")
//                    }
//
//                    Spacer(modifier = Modifier.width(16.dp))
//
//                    // Save Button (Right)
//                    Button(
//                        onClick = { saveVitals() },
//                        modifier = Modifier.weight(1f),
//                        enabled = saveVitalsResult !is Result.Loading
//                    ) {
//                        Text("Save")
//                    }
//                }
//
//                // Show save result
//                saveVitalsResult?.let { result ->
//                    Spacer(modifier = Modifier.height(16.dp))
//                    when (result) {
//                        is Result.Success -> {
//                            Text(
//                                text = "Vitals saved successfully!",
//                                color = MaterialTheme.colorScheme.primary,
//                                style = MaterialTheme.typography.bodyLarge,
//                                fontWeight = FontWeight.Bold
//                            )
//                        }
//                        is Result.Error -> {
//                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                                Text(
//                                    text = "Error: ${result.message}",
//                                    color = MaterialTheme.colorScheme.error,
//                                    style = MaterialTheme.typography.bodyMedium
//                                )
//                                Spacer(modifier = Modifier.height(8.dp))
//                                Button(
//                                    onClick = {
//                                        viewModel.clearSaveVitalsResult()
//                                    },
//                                    modifier = Modifier.fillMaxWidth()
//                                ) {
//                                    Text("Dismiss")
//                                }
//                            }
//                        }
//                        is Result.Loading -> {
//                            Text("Saving vitals...")
//                        }
//                    }
//                }
//            }
//        }
//    }
//}

