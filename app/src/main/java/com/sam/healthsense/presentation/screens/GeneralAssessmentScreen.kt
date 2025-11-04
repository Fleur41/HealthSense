//package com.sam.healthsense.presentation.screens

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import com.sam.healthsense.domain.model.GeneralHealth
import com.sam.healthsense.presentation.viewmodels.GeneralAssessmentViewModel
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralAssessmentScreen(
    patientId: String,
    onBack: () -> Unit,
    onAssessmentSaved: () -> Unit,
    viewModel: GeneralAssessmentViewModel = hiltViewModel()
) {
    // Local states
    var visitDate by remember { mutableStateOf("") }
    var generalHealth by remember { mutableStateOf<GeneralHealth?>(null) }
    var hasBeenOnDiet by remember { mutableStateOf<Boolean?>(null) }
    var comments by remember { mutableStateOf("") }

    // Error states
    var isVisitDateError by remember { mutableStateOf(false) }
    var isGeneralHealthError by remember { mutableStateOf(false) }
    var isHasBeenOnDietError by remember { mutableStateOf(false) }
    var isCommentsError by remember { mutableStateOf(false) }

    // ViewModel states
    val saveAssessmentResult by viewModel.saveAssessmentResult.collectAsState()
    val patientName by viewModel.patientName.collectAsState()
    val loadingState by viewModel.loadingState.collectAsState()

    // Context for Toast
    val context = LocalContext.current

    // Load patient name when screen opens
    LaunchedEffect(patientId) {
        viewModel.loadPatientName(patientId)
    }

    // Clear error function
    fun clearError() {
        isVisitDateError = false
        isGeneralHealthError = false
        isHasBeenOnDietError = false
        isCommentsError = false
    }

    // Save assessment function
    fun saveAssessment() {
        clearError()

        var hasError = false

        if (visitDate.isBlank()) {
            isVisitDateError = true
            hasError = true
        }
        if (generalHealth == null) {
            isGeneralHealthError = true
            hasError = true
        }
        if (hasBeenOnDiet == null) {
            isHasBeenOnDietError = true
            hasError = true
        }
        if (comments.isBlank()) {
            isCommentsError = true
            hasError = true
        }

        if (hasError) {
            Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_LONG).show()
            return
        }

        // Use current date as placeholder for LocalDate
        val currentDate = LocalDate(2024, 1, 1) // Placeholder date
        viewModel.saveGeneralAssessment(
            patientId = patientId,
            visitDate = currentDate,
            generalHealth = generalHealth!!,
            hasBeenOnDiet = hasBeenOnDiet!!,
            comments = comments
        )
    }

    // Handle successful assessment save
    LaunchedEffect(saveAssessmentResult) {
        if (saveAssessmentResult is Result.Success) {
            onAssessmentSaved()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "General Assessment Form",
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
            // Patient Name Display
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
                        text = "Patient Name:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (loadingState) {
                        Text(
                            text = "Loading...",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    } else {
                        Text(
                            text = patientName ?: "Unknown Patient",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Assessment Form
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
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

                // General Health Question
                Text(
                    text = "General health?",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Column {
                    GeneralHealth.values().forEach { health ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = generalHealth == health,
                                onClick = {
                                    generalHealth = health
                                    isGeneralHealthError = false
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = health.name,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    if (isGeneralHealthError) {
                        Text(
                            text = "General health selection is required",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }

                // Have you ever been on diet to lose weight? Question
                Text(
                    text = "Have you ever been on diet to lose weight?",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = hasBeenOnDiet == true,
                            onClick = {
                                hasBeenOnDiet = true
                                isHasBeenOnDietError = false
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Yes",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = hasBeenOnDiet == false,
                            onClick = {
                                hasBeenOnDiet = false
                                isHasBeenOnDietError = false
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "No",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    if (isHasBeenOnDietError) {
                        Text(
                            text = "This selection is required",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }

                // Comments
                OutlinedTextField(
                    value = comments,
                    onValueChange = {
                        comments = it
                        isCommentsError = it.isBlank()
                    },
                    label = { Text("Comments") },
                    placeholder = { Text("Enter any additional comments") },
                    isError = isCommentsError,
                    supportingText = {
                        if (isCommentsError) {
                            Text("Comments are required", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 4
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
                        onClick = { saveAssessment() },
                        modifier = Modifier.weight(1f),
                        enabled = saveAssessmentResult !is Result.Loading
                    ) {
                        Text("Save")
                    }
                }

                // Show save result
                saveAssessmentResult?.let { result ->
                    Spacer(modifier = Modifier.height(16.dp))
                    when (result) {
                        is Result.Success -> {
                            Text(
                                text = "Assessment saved successfully!",
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
                                        viewModel.clearSaveAssessmentResult()
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Dismiss")
                                }
                            }
                        }
                        is Result.Loading -> {
                            Text("Saving assessment...")
                        }
                    }
                }
            }
        }
    }
}