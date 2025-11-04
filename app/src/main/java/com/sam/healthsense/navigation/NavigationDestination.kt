package com.sam.healthsense.navigation

sealed interface NavigationDestination {
    val title: String
    val route: String

    data object SignIn: NavigationDestination {
        override val title: String
            get() = "Sign In"
        override val route: String
            get() = "sign_in"
    }

    data object SignUp: NavigationDestination {
        override val title: String
            get() = "Sign Up"
        override val route: String
            get() = "sign_up"
    }

    data object Splash: NavigationDestination {
        override val title: String
            get() = "Splash"
        override val route: String
            get() = "plash"
    }

    data object Home: NavigationDestination {
        override val title: String
            get() = "Home"
        override val route: String
            get() = "home"
    }

    data object Detail: NavigationDestination {
        override val title: String
            get() = "Detail"
        override val route: String
            get() = "detail"
    }

    // NEW: HealthSense specific destinations
    data object PatientRegistration: NavigationDestination {
        override val title: String
            get() = "Register Patient"
        override val route: String
            get() = "patient_registration"
    }

    data object PatientVitals: NavigationDestination {
        override val title: String
            get() = "Patient Vitals"
        override val route: String
            get() = "patient_vitals/{patientId}"

        fun createRoute(patientId: String): String = "patient_vitals/$patientId"
    }

    data object GeneralAssessment: NavigationDestination {
        override val title: String
            get() = "General Assessment"
        override val route: String
            get() = "general_assessment/{patientId}"

        fun createRoute(patientId: String): String = "general_assessment/$patientId"
    }

    data object OverweightAssessment: NavigationDestination {
        override val title: String
            get() = "Overweight Assessment"
        override val route: String
            get() = "overweight_assessment/{patientId}"

        fun createRoute(patientId: String): String = "overweight_assessment/$patientId"
    }

    data object PatientListing: NavigationDestination {
        override val title: String
            get() = "Patient List"
        override val route: String
            get() = "patient_listing"
    }
}