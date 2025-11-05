# HealthSense
# HealthSense - Patient Management System 

<div align="center">

*A comprehensive healthcare application for efficient patient management and vital signs tracking*

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.2-blue.svg)](https://kotlinlang.org)
[![Android](https://img.shields.io/badge/Android-API+34+-green.svg)](https://developer.android.com)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-1.5+-purple.svg)](https://developer.android.com/jetpack/compose)
[![Firebase](https://img.shields.io/badge/Firebase-Auth%20%26%20Firestore-orange.svg)](https://firebase.google.com)

</div>

## App Overview

HealthSense is a modern Android application built with **Kotlin** and **Jetpack Compose** that revolutionizes patient management in healthcare facilities. The app provides a seamless digital solution for healthcare practitioners to register patients, record vital signs, conduct health assessments, and maintain comprehensive patient records.

## Technology Stack

### Core Technologies
- **Kotlin** - Modern, concise programming language
- **Jetpack Compose** - Declarative UI toolkit
- **Material Design 3** - Modern design system
- **Coroutines** - Asynchronous programming

### Architecture & DI
- **MVVM Architecture** - Clean separation of concerns
- **Repository Pattern** - Data abstraction layer
- **Dagger Hilt** - Dependency injection
- **Android Architecture Components** - Lifecycle-aware components

### Local Data & Storage
- **Room Database** - Local SQLite abstraction
- **DataStore Preferences** - Type-safe data storage
- **Kotlinx DateTime** - Modern date/time handling
- **Desugaring** - Java 8+ API support

### Networking & APIs
- **Retrofit** - Type-safe HTTP client
- **Moshi** - JSON serialization/deserialization
- **OkHttp** - HTTP client with logging interceptor
- **Firebase APIs** - Authentication, Firestore, Analytics, Storage

### Navigation & UI
- **Compose Navigation** - Type-safe navigation
- **Custom Animations** - Smooth screen transitions
- **Accompanist** - Additional Compose utilities

## Key Features

### Secure Authentication
- **Firebase Authentication** with email/password
- Secure login and registration system
- Persistent session management with DataStore
- Automatic navigation based on authentication status

### Core Functionality

#### 1. **Patient Registration**
- Unique patient number generation
- Complete patient profile creation
- Date of birth and demographic information
- One-time registration per patient

#### 2. **Vital Signs Recording**
- Height and weight measurements
- **Automatic BMI calculation**
- Visit date tracking
- Multiple submissions with date validation

#### 3. **Smart Assessment System**
- **BMI-based routing** to appropriate assessment forms
- **General Assessment** (BMI < 25)
- **Overweight Assessment** (BMI ≥ 25)
- Comprehensive health questionnaires

#### 4. **Patient Management**
- Complete patient listing with vital statistics
- BMI status categorization (Underweight/Normal/Overweight)
- Date-based filtering capabilities
- Age calculation and patient overview

### User Experience
- **Modern Material Design 3** interface
- Smooth navigation with custom animations
- Real-time form validation
- Loading states and error handling
- Offline-first architecture with Room database

## Quick Start Guide

### Prerequisites
- Android Studio (Hedgehog or later)
- Android SDK API 24+
- Kotlin 1.9.0+
- An Android device or emulator (API 24+)

### Installation Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/Fleur41/HealthSense.git
   cd HealthSense


2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned HealthSense folder
   - Click "OK"

3. **Build and Run**
   - Connect your Android device or start an emulator
   - Click the "Run" button (▶️) or press Shift + F10
   - Wait for the build to complete and app to install

4. **Test the Application**
   - Use these credentials for testing:
     - Email: sam1234@gmail.com
     - Password: sam1234
   - Or register a new account

## Testing Workflow

### Authentication
- Sign up with a new account or use test credentials
- Experience seamless login with session persistence

### Patient Registration
- Navigate to "Register New Patient" from HomeScreen
- Fill in patient details (Patient Number, Name, DOB, Gender)
- Submit to proceed to vitals recording

### Vital Signs Entry
- Enter height (cm) and weight (kg)
- Watch BMI auto-calculate in real-time
- Submit to route to appropriate assessment

### Health Assessment
- Complete the assessment form based on BMI
- Answer health-related questions
- Add relevant comments

### View Patient List
- Access comprehensive patient overview
- View BMI status and last visit dates
- Filter records by date


## Key Screens & Functionality

### HomeScreen (HomeScreen.kt)
- Dashboard with patient statistics
- Quick actions for patient registration and listing
- Real-time data showing patient overview
- Professional UI with HealthSense branding

### Patient Registration (PatientRegistrationScreen.kt)
- Form validation with error handling
- Date pickers for registration and birth dates
- Unique patient ID validation
- Seamless navigation to vitals screen

### Patient Vitals (PatientVitalsScreen.kt)
- BMI auto-calculation as you type
- Input validation for medical ranges
- Smart routing to appropriate assessment
- Date validation for multiple submissions

### Health Assessments

#### General Assessment (GeneralAssessmentScreen.kt)
- Diet history and general health
- For patients with normal BMI

#### Overweight Assessment (OverweightAssessmentScreen.kt)
- Medication usage and health status
- For patients with elevated BMI

### Patient Listing (PatientListingScreen.kt)
- Comprehensive overview of all patients
- BMI status categorization with color coding
- Age calculation from date of birth
- Date filtering capabilities

## Data Flow

- **Local First**: Data saved immediately to Room database
- **Sync to Backend**: Automatic synchronization with REST API
- **Offline Capable**: Full functionality without internet
- **Real-time Updates**: Live data observation with Flow

## BMI Classification

The app automatically categorizes patients based on WHO standards:

- **Underweight**: BMI < 18.5
- **Normal**: BMI 18.5 - 24.9
- **Overweight**: BMI ≥ 25

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Developer

**Simon Muriithi**
- GitHub: [@Fleur41](https://github.com/Fleur41)
- Project: HealthSense

<div align="center">

Built with passion using Modern Android Development

*Transforming healthcare through digital innovation*

</div>

