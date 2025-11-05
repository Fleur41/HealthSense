package com.sam.healthsense.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sam.healthsense.Utils.Result
import com.sam.healthsense.presentation.viewmodels.OverweightAssessmentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverweightAssessmentScreen(
    patientId: String,
    onBack: () -> Unit,
    onAssessmentSaved: () -> Unit,
    viewModel: OverweightAssessmentViewModel = hiltViewModel()
) {
    // Local states
    var visitDate by remember { mutableStateOf("") }
    var generalHealth by remember { mutableStateOf("Good") }
    var isTakingDrugs by remember { mutableStateOf(false) }
    var comments by remember { mutableStateOf("") }

    // Error states
    var isVisitDateError by remember { mutableStateOf(false) }

    // ViewModel state
    val patientName by viewModel.patientName.collectAsState()
    val saveResult by viewModel.saveResult.collectAsState()

    // Context for Toast
    val context = LocalContext.current

    // Clear error function
    fun clearError() {
        isVisitDateError = false
    }

    // Clear all data function
    fun clearData() {
        visitDate = ""
        generalHealth = "Good"
        isTakingDrugs = false
        comments = ""
        clearError()
    }

    // Validate and save assessment
    fun saveAssessment() {
        clearError()

        // Validate required fields
        var hasError = false

        if (visitDate.isBlank()) {
            isVisitDateError = true
            hasError = true
        }

        // If any error, show toast and return
        if (hasError) {
            Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_LONG).show()
            return
        }

        // Update ViewModel state with local state
        viewModel.onCommentsChange(comments)
        viewModel.onGeneralHealthChange(
            if (generalHealth == "Good") com.sam.healthsense.domain.model.GeneralHealth.GOOD
            else com.sam.healthsense.domain.model.GeneralHealth.POOR
        )
        viewModel.onTakingDrugsChange(isTakingDrugs)

        // Call ViewModel to save assessment
        viewModel.saveAssessment(patientId)
    }

    // Handle successful save
    LaunchedEffect(saveResult) {
        if (saveResult is Result.Success) {
            onAssessmentSaved()
            Toast.makeText(context, "Assessment saved successfully!", Toast.LENGTH_LONG).show()
        }
    }

    // Load patient name when screen opens
    LaunchedEffect(patientId) {
        viewModel.loadPatientName(patientId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Patient Visit Form Overweight",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Close"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Yellow.copy(alpha = 0.3f)
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
            // Patient Name Header
            Text(
                text = "Patient Name: ${patientName ?: "Loading..."}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                textAlign = TextAlign.Center
            )

            // Visit Date
            Text(
                text = "Visit Date",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = visitDate,
                onValueChange = {
                    visitDate = it
                    isVisitDateError = it.isBlank()
                },
                placeholder = { Text("DD/MM/YYYY") },
                isError = isVisitDateError,
                supportingText = {
                    if (isVisitDateError) {
                        Text("Visit date is required", color = MaterialTheme.colorScheme.error)
                    } else {
                        Text("Enter date as DD/MM/YYYY")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // General Health Question
            Text(
                text = "General Health?",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Column {
                // Good option
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { generalHealth = "Good" }
                ) {
                    RadioButton(
                        selected = generalHealth == "Good",
                        onClick = { generalHealth = "Good" }
                    )
                    Text(
                        text = "Good",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                // Poor option
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { generalHealth = "Poor" }
                ) {
                    RadioButton(
                        selected = generalHealth == "Poor",
                        onClick = { generalHealth = "Poor" }
                    )
                    Text(
                        text = "Poor",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Taking Drugs Question
            Text(
                text = "Are you currently taking any drugs?",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Column {
                // Yes option
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isTakingDrugs = true }
                ) {
                    RadioButton(
                        selected = isTakingDrugs,
                        onClick = { isTakingDrugs = true }
                    )
                    Text(
                        text = "Yes",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                // No option
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isTakingDrugs = false }
                ) {
                    RadioButton(
                        selected = !isTakingDrugs,
                        onClick = { isTakingDrugs = false }
                    )
                    Text(
                        text = "No",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Comments
            Text(
                text = "Comments",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = comments,
                onValueChange = { comments = it },
                placeholder = { Text("Enter comments...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                singleLine = false,
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        clearData()
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

                Button(
                    onClick = { saveAssessment() },
                    modifier = Modifier.weight(1f),
                    enabled = saveResult !is Result.Loading
                ) {
                    if (saveResult is Result.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Save")
                    }
                }
            }

            // Show save result
            saveResult?.let { result ->
                Spacer(modifier = Modifier.height(16.dp))
                when (result) {
                    is Result.Success -> {
                        Text(
                            text = "Assessment saved successfully!",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                    is Result.Error -> {
                        Text(
                            text = "Error: ${result.message}",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                    is Result.Loading -> {
                    }
                }
            }
        }
    }
}

