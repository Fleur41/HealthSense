package com.sam.healthsense.presentation.screens

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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import com.sam.healthsense.presentation.viewmodels.PatientRegistrationViewModel
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientRegistrationScreen(
    onBack: () -> Unit,
    onPatientRegistered: (String) -> Unit,
    viewModel: PatientRegistrationViewModel = hiltViewModel()
) {
    // Local states
    var patientNumber by remember { mutableStateOf("") }
    var registrationDate by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }

    // Error states
    var isPatientNumberError by remember { mutableStateOf(false) }
    var isRegistrationDateError by remember { mutableStateOf(false) }
    var isFirstNameError by remember { mutableStateOf(false) }
    var isLastNameError by remember { mutableStateOf(false) }
    var isDateOfBirthError by remember { mutableStateOf(false) }
    var isGenderError by remember { mutableStateOf(false) }

    // Dropdown state
    var isGenderExpanded by remember { mutableStateOf(false) }

    // ViewModel state
    val registrationResult by viewModel.registrationResult.collectAsState()

    // Context for Toast
    val context = LocalContext.current

    // Clear error function
    fun clearError() {
        isPatientNumberError = false
        isRegistrationDateError = false
        isFirstNameError = false
        isLastNameError = false
        isDateOfBirthError = false
        isGenderError = false
    }

    // Clear all data function
    fun clearData() {
        patientNumber = ""
        registrationDate = ""
        firstName = ""
        lastName = ""
        dateOfBirth = ""
        gender = ""
        clearError()
    }

    // Validate and register patient
    fun registerPatient() {
        clearError()

        // Validate all fields
        var hasError = false

        if (patientNumber.isBlank()) {
            isPatientNumberError = true
            hasError = true
        }
        if (registrationDate.isBlank()) {
            isRegistrationDateError = true
            hasError = true
        }
        if (firstName.isBlank()) {
            isFirstNameError = true
            hasError = true
        }
        if (lastName.isBlank()) {
            isLastNameError = true
            hasError = true
        }
        if (dateOfBirth.isBlank()) {
            isDateOfBirthError = true
            hasError = true
        }
        if (gender.isBlank()) {
            isGenderError = true
            hasError = true
        }

        // If any error, show toast and return
        if (hasError) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_LONG).show()
            return
        }

        // Use current date as placeholder for LocalDate
        val currentDate = LocalDate(2024, 1, 1) // Placeholder date
        viewModel.registerPatient(
            patientNumber = patientNumber,
            registrationDate = currentDate,
            firstName = firstName,
            lastName = lastName,
            dateOfBirth = currentDate,
            gender = gender
        )
    }

    // Handle successful registration - FIXED: Use actual patient ID from ViewModel
    LaunchedEffect(registrationResult) {
        if (registrationResult is Result.Success) {
            val patientId = (registrationResult as Result.Success<String>).data
            onPatientRegistered(patientId)

            // Show success message
            Toast.makeText(context, "Patient registered successfully!", Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Register Patient",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
            // Form Header Card
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Patient Registration Form",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Please fill in all required patient details",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Patient Registration Form
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Patient Number
                OutlinedTextField(
                    value = patientNumber,
                    onValueChange = {
                        patientNumber = it
                        isPatientNumberError = it.isBlank()
                    },
                    label = { Text("Patient Number") },
                    placeholder = { Text("Enter unique patient number") },
                    isError = isPatientNumberError,
                    supportingText = {
                        if (isPatientNumberError) {
                            Text("Patient number is required", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // 2. Registration Date
                OutlinedTextField(
                    value = registrationDate,
                    onValueChange = {
                        registrationDate = it
                        isRegistrationDateError = it.isBlank()
                    },
                    label = { Text("Registration Date") },
                    placeholder = { Text("DD/MM/YYYY") },
                    isError = isRegistrationDateError,
                    supportingText = {
                        if (isRegistrationDateError) {
                            Text("Registration date is required", color = MaterialTheme.colorScheme.error)
                        } else {
                            Text("Enter date as DD/MM/YYYY")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // 3. First Name
                OutlinedTextField(
                    value = firstName,
                    onValueChange = {
                        firstName = it
                        isFirstNameError = it.isBlank()
                    },
                    label = { Text("First Name") },
                    placeholder = { Text("Enter first name") },
                    isError = isFirstNameError,
                    supportingText = {
                        if (isFirstNameError) {
                            Text("First name is required", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // 4. Last Name
                OutlinedTextField(
                    value = lastName,
                    onValueChange = {
                        lastName = it
                        isLastNameError = it.isBlank()
                    },
                    label = { Text("Last Name") },
                    placeholder = { Text("Enter last name") },
                    isError = isLastNameError,
                    supportingText = {
                        if (isLastNameError) {
                            Text("Last name is required", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // 5. Date of Birth
                OutlinedTextField(
                    value = dateOfBirth,
                    onValueChange = {
                        dateOfBirth = it
                        isDateOfBirthError = it.isBlank()
                    },
                    label = { Text("Date of Birth") },
                    placeholder = { Text("DD/MM/YYYY") },
                    isError = isDateOfBirthError,
                    supportingText = {
                        if (isDateOfBirthError) {
                            Text("Date of birth is required", color = MaterialTheme.colorScheme.error)
                        } else {
                            Text("Enter date as DD/MM/YYYY")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // 6. Gender Dropdown
                ExposedDropdownMenuBox(
                    expanded = isGenderExpanded,
                    onExpandedChange = { isGenderExpanded = it }
                ) {
                    OutlinedTextField(
                        value = gender,
                        onValueChange = { },
                        label = { Text("Gender") },
                        placeholder = { Text("Select gender") },
                        isError = isGenderError,
                        supportingText = {
                            if (isGenderError) {
                                Text("Gender is required", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isGenderExpanded)
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = isGenderExpanded,
                        onDismissRequest = { isGenderExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Male") },
                            onClick = {
                                gender = "Male"
                                isGenderError = false
                                isGenderExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Female") },
                            onClick = {
                                gender = "Female"
                                isGenderError = false
                                isGenderExpanded = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Two Buttons in a Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Close Button (Left)
                    Button(
                        onClick = {
                            clearData()
                            viewModel.clearForm()
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
                        onClick = { registerPatient() },
                        modifier = Modifier.weight(1f),
                        enabled = registrationResult !is Result.Loading
                    ) {
                        Text("Save")
                    }
                }

                // Show registration result
                registrationResult?.let { result ->
                    Spacer(modifier = Modifier.height(16.dp))
                    when (result) {
                        is Result.Success -> {
                            Text(
                                text = "Patient registered successfully!",
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
                                        viewModel.clearRegistrationResult()
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Dismiss")
                                }
                            }
                        }
                        is Result.Loading -> {
                            Text("Registering patient...")
                        }
                    }
                }
            }
        }
    }
}

//package com.sam.healthsense.presentation.screens

//
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
//import androidx.compose.material3.DropdownMenuItem
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.ExposedDropdownMenuBox
//import androidx.compose.material3.ExposedDropdownMenuDefaults
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
//import com.sam.healthsense.presentation.viewmodels.PatientRegistrationViewModel
//import kotlinx.datetime.LocalDate
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun PatientRegistrationScreen(
//    onBack: () -> Unit,
//    onPatientRegistered: (String) -> Unit,
//    viewModel: PatientRegistrationViewModel = hiltViewModel()
//) {
//    // Local states as per your pattern
//    var patientNumber by remember { mutableStateOf("") }
//    var registrationDate by remember { mutableStateOf("") }
//    var firstName by remember { mutableStateOf("") }
//    var lastName by remember { mutableStateOf("") }
//    var dateOfBirth by remember { mutableStateOf("") }
//    var gender by remember { mutableStateOf("") }
//
//    // Error states
//    var isPatientNumberError by remember { mutableStateOf(false) }
//    var isRegistrationDateError by remember { mutableStateOf(false) }
//    var isFirstNameError by remember { mutableStateOf(false) }
//    var isLastNameError by remember { mutableStateOf(false) }
//    var isDateOfBirthError by remember { mutableStateOf(false) }
//    var isGenderError by remember { mutableStateOf(false) }
//
//    // Dropdown state
//    var isGenderExpanded by remember { mutableStateOf(false) }
//
//    // ViewModel state
//    val registrationResult by viewModel.registrationResult.collectAsState()
//
//    // Context for Toast
//    val context = LocalContext.current
//
//    // Clear error function
//    fun clearError() {
//        isPatientNumberError = false
//        isRegistrationDateError = false
//        isFirstNameError = false
//        isLastNameError = false
//        isDateOfBirthError = false
//        isGenderError = false
//    }
//
//    // Clear all data function
//    fun clearData() {
//        patientNumber = ""
//        registrationDate = ""
//        firstName = ""
//        lastName = ""
//        dateOfBirth = ""
//        gender = ""
//        clearError()
//    }
//
//
//    // Validate and register patient
//    fun registerPatient() {
//        clearError()
//
//        // Validate all fields
//        var hasError = false
//
//        if (patientNumber.isBlank()) {
//            isPatientNumberError = true
//            hasError = true
//        }
//        if (registrationDate.isBlank()) {
//            isRegistrationDateError = true
//            hasError = true
//        }
//        if (firstName.isBlank()) {
//            isFirstNameError = true
//            hasError = true
//        }
//        if (lastName.isBlank()) {
//            isLastNameError = true
//            hasError = true
//        }
//        if (dateOfBirth.isBlank()) {
//            isDateOfBirthError = true
//            hasError = true
//        }
//        if (gender.isBlank()) {
//            isGenderError = true
//            hasError = true
//        }
//
//        // If any error, show toast and return
//        if (hasError) {
//            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_LONG).show()
//            return
//        }
//
//        // Use ViewModel function with all parameters
//        // For now, use current date as placeholder for LocalDate
//        val currentDate = LocalDate(2024, 1, 1) // Placeholder date
//        viewModel.registerPatient(
//            patientNumber = patientNumber,
//            registrationDate = currentDate,
//            firstName = firstName,
//            lastName = lastName,
//            dateOfBirth = currentDate,
//            gender = gender
//        )
//    }
//
//    // Handle successful registration
//    LaunchedEffect(registrationResult) {
//        if (registrationResult is Result.Success) {
//            // Generate a patient ID (in real app, this would come from backend)
//            val patientId = "PAT${System.currentTimeMillis()}"
//            onPatientRegistered(patientId)
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = "Register Patient",
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
//            // Form Header Card
//            Card(
//                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 24.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(16.dp)
//                ) {
//                    Text(
//                        text = "Patient Registration Form",
//                        style = MaterialTheme.typography.titleLarge,
//                        fontWeight = FontWeight.Bold,
//                        color = MaterialTheme.colorScheme.primary
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text(
//                        text = "Please fill in all required patient details",
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant
//                    )
//                }
//            }
//
//            // Patient Registration Form - STRICT ORDER as per Figma
//            Column(
//                verticalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                // 1. Patient Number
//                OutlinedTextField(
//                    value = patientNumber,
//                    onValueChange = {
//                        patientNumber = it
//                        isPatientNumberError = it.isBlank()
//                    },
//                    label = { Text("Patient Number") },
//                    placeholder = { Text("Enter unique patient number") },
//                    isError = isPatientNumberError,
//                    supportingText = {
//                        if (isPatientNumberError) {
//                            Text("Patient number is required", color = MaterialTheme.colorScheme.error)
//                        }
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                // 2. Registration Date (Now editable text field)
//                OutlinedTextField(
//                    value = registrationDate,
//                    onValueChange = {
//                        registrationDate = it
//                        isRegistrationDateError = it.isBlank()
//                    },
//                    label = { Text("Registration Date") },
//                    placeholder = { Text("DD/MM/YYYY") },
//                    isError = isRegistrationDateError,
//                    supportingText = {
//                        if (isRegistrationDateError) {
//                            Text("Registration date is required", color = MaterialTheme.colorScheme.error)
//                        } else {
//                            Text("Enter date as DD/MM/YYYY")
//                        }
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                // 3. First Name
//                OutlinedTextField(
//                    value = firstName,
//                    onValueChange = {
//                        firstName = it
//                        isFirstNameError = it.isBlank()
//                    },
//                    label = { Text("First Name") },
//                    placeholder = { Text("Enter first name") },
//                    isError = isFirstNameError,
//                    supportingText = {
//                        if (isFirstNameError) {
//                            Text("First name is required", color = MaterialTheme.colorScheme.error)
//                        }
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                // 4. Last Name
//                OutlinedTextField(
//                    value = lastName,
//                    onValueChange = {
//                        lastName = it
//                        isLastNameError = it.isBlank()
//                    },
//                    label = { Text("Last Name") },
//                    placeholder = { Text("Enter last name") },
//                    isError = isLastNameError,
//                    supportingText = {
//                        if (isLastNameError) {
//                            Text("Last name is required", color = MaterialTheme.colorScheme.error)
//                        }
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                // 5. Date of Birth (Now editable text field)
//                OutlinedTextField(
//                    value = dateOfBirth,
//                    onValueChange = {
//                        dateOfBirth = it
//                        isDateOfBirthError = it.isBlank()
//                    },
//                    label = { Text("Date of Birth") },
//                    placeholder = { Text("DD/MM/YYYY") },
//                    isError = isDateOfBirthError,
//                    supportingText = {
//                        if (isDateOfBirthError) {
//                            Text("Date of birth is required", color = MaterialTheme.colorScheme.error)
//                        } else {
//                            Text("Enter date as DD/MM/YYYY")
//                        }
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                // 6. Gender Dropdown
//                ExposedDropdownMenuBox(
//                    expanded = isGenderExpanded,
//                    onExpandedChange = { isGenderExpanded = it }
//                ) {
//                    OutlinedTextField(
//                        value = gender,
//                        onValueChange = { },
//                        label = { Text("Gender") },
//                        placeholder = { Text("Select gender") },
//                        isError = isGenderError,
//                        supportingText = {
//                            if (isGenderError) {
//                                Text("Gender is required", color = MaterialTheme.colorScheme.error)
//                            }
//                        },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .menuAnchor(),
//                        readOnly = true,
//                        trailingIcon = {
//                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isGenderExpanded)
//                        }
//                    )
//
//                    ExposedDropdownMenu(
//                        expanded = isGenderExpanded,
//                        onDismissRequest = { isGenderExpanded = false }
//                    ) {
//                        DropdownMenuItem(
//                            text = { Text("Male") },
//                            onClick = {
//                                gender = "Male"
//                                isGenderError = false
//                                isGenderExpanded = false
//                            }
//                        )
//                        DropdownMenuItem(
//                            text = { Text("Female") },
//                            onClick = {
//                                gender = "Female"
//                                isGenderError = false
//                                isGenderExpanded = false
//                            }
//                        )
//                    }
//                }
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
//                            clearData()
//                            viewModel.clearForm()
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
//                        onClick = { registerPatient() },
//                        modifier = Modifier.weight(1f),
//                        enabled = registrationResult !is Result.Loading
//                    ) {
//                        Text("Save")
//                    }
//                }
//
//                // Show registration result
//                registrationResult?.let { result ->
//                    Spacer(modifier = Modifier.height(16.dp))
//                    when (result) {
//                        is Result.Success -> {
//                            Text(
//                                text = "Patient registered successfully!",
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
//                                        viewModel.clearRegistrationResult()
//                                    },
//                                    modifier = Modifier.fillMaxWidth()
//                                ) {
//                                    Text("Dismiss")
//                                }
//                            }
//                        }
//                        is Result.Loading -> {
//                            Text("Registering patient...")
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
