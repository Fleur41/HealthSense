package com.sam.healthsense.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sam.healthsense.authentication.signin.SignInScreen
import com.sam.healthsense.authentication.signup.SignUpScreen
import com.sam.healthsense.components.slideIntoContainerAnimation
import com.sam.healthsense.components.slideOutOfContainerAnimation
import com.sam.healthsense.detail.DetailScreen
import com.sam.healthsense.home.HomeScreen
import com.sam.healthsense.home.HomeViewModel
import com.sam.healthsense.presentation.screens.GeneralAssessmentScreen
import com.sam.healthsense.presentation.screens.PatientRegistrationScreen
import com.sam.healthsense.presentation.screens.PatientVitalsScreen
import com.sam.healthsense.splash.SplashScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    startDestination: NavigationDestination
) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination.route

    ) {
        composable(
            route = NavigationDestination.SignIn.route,
            enterTransition = { slideIntoContainerAnimation() },

            exitTransition = { slideOutOfContainerAnimation() }


        ) { backStackEntry ->
            SignInScreen(
                authViewModel = hiltViewModel(backStackEntry),
                onSignUpClick = {
                    navController.navigate(NavigationDestination.SignUp.route)
                }
            )
        }

        composable(
            route = NavigationDestination.SignUp.route,
            enterTransition = { slideIntoContainerAnimation(towards = SlideDirection.Left) },
            exitTransition = { slideOutOfContainerAnimation(towards = SlideDirection.Right) }
        ) { backStackEntry ->
            val parentEntry =
                remember(backStackEntry) { navController.getBackStackEntry(NavigationDestination.SignIn.route) }
            SignUpScreen(
                authViewModel = hiltViewModel(parentEntry),
                onBack = {
                    navController.popBackStack()
                },
//                onSignUpSuccess = {
//                    navController.navigate(NavigationDestination.Home.route)
//                }
            )
        }

        composable(
            route = NavigationDestination.Home.route,
            enterTransition = { slideIntoContainerAnimation(towards = SlideDirection.Left) },
            exitTransition = { slideOutOfContainerAnimation(towards = SlideDirection.Right) }
        ) {
//            val homeViewModel: HomeViewModel = hiltViewModel()
//            homeViewModel = homeViewModel //it was in HomeScreen
            HomeScreen(
                onPatientRegistration = {
                    navController.navigate(NavigationDestination.PatientRegistration.route)
                },
                onPatientListing = {
                    navController.navigate(NavigationDestination.PatientListing.route)
                }
            )
        }

        composable(
            route = NavigationDestination.Splash.route,
        ) {
            SplashScreen()
        }

        composable(
            route = NavigationDestination.Detail.route,
            enterTransition = { slideIntoContainerAnimation() },

            exitTransition = { slideOutOfContainerAnimation() }


        ) { backStackEntry ->
            val parentEntry =
                remember(backStackEntry) { navController.getBackStackEntry(NavigationDestination.Home.route) }
            DetailScreen(
                homeViewModel = hiltViewModel(parentEntry),
                onBackClick = { navController.popBackStack() }
            )
        }


        //New navigation composables
        composable(
            route = NavigationDestination.PatientRegistration.route,
            enterTransition = { slideIntoContainerAnimation() },
            exitTransition = { slideOutOfContainerAnimation() }
        ) { backStackEntry ->
            PatientRegistrationScreen(
                onBack = {
                    navController.navigate(NavigationDestination.Home.route){
                        popUpTo(NavigationDestination.Home.route) { inclusive = true }
                    }
                },
                onPatientRegistered = { patientId ->
                    navController.navigate(NavigationDestination.PatientVitals.createRoute(patientId))
                }
            )
        }

        composable(
            route = NavigationDestination.PatientVitals.route,
            arguments = listOf(navArgument("patientId") { type = NavType.StringType }),
            enterTransition = { slideIntoContainerAnimation() },
            exitTransition = { slideOutOfContainerAnimation() }
        ) { backStackEntry ->
            val patientId = backStackEntry.arguments?.getString("patientId") ?: ""
            PatientVitalsScreen(
                patientId = patientId,
                onBack = {
                    // Navigate back to Home instead of previous screen
                    navController.navigate(NavigationDestination.Home.route) {
                        popUpTo(NavigationDestination.Home.route) { inclusive = true }
                    }
                    //navController.popBackStack()
                },
                onVitalsSaved = { patientId, bmi ->
                    if (bmi < 25) {
                        navController.navigate(NavigationDestination.GeneralAssessment.createRoute(patientId))
                    } else {
                        navController.navigate(NavigationDestination.OverweightAssessment.createRoute(patientId))
                    }
                }
            )
        }
//
        composable(
            route = NavigationDestination.GeneralAssessment.route,
            arguments = listOf(navArgument("patientId") { type = NavType.StringType }),
            enterTransition = { slideIntoContainerAnimation() },
            exitTransition = { slideOutOfContainerAnimation() }
        ) { backStackEntry ->
            val patientId = backStackEntry.arguments?.getString("patientId") ?: ""
            GeneralAssessmentScreen(
                patientId = patientId,
                onBack = {
                    navController.popBackStack()
                },
                onAssessmentSaved = {
                    navController.navigate(NavigationDestination.PatientListing.route) {
                        popUpTo(NavigationDestination.Home.route) { inclusive = false }
                    }
                }
            )
        }
//
//        composable(
//            route = NavigationDestination.OverweightAssessment.route,
//            arguments = listOf(navArgument("patientId") { type = NavType.StringType }),
//            enterTransition = { slideIntoContainerAnimation() },
//            exitTransition = { slideOutOfContainerAnimation() }
//        ) { backStackEntry ->
//            val patientId = backStackEntry.arguments?.getString("patientId") ?: ""
//            OverweightAssessmentScreen(
//                patientId = patientId,
//                onBack = {
//                    navController.popBackStack()
//                },
//                onAssessmentSaved = {
//                    navController.navigate(NavigationDestination.PatientListing.route) {
//                        popUpTo(NavigationDestination.Home.route) { inclusive = false }
//                    }
//                }
//            )
//        }
//
//        composable(
//            route = NavigationDestination.PatientListing.route,
//            enterTransition = { slideIntoContainerAnimation() },
//            exitTransition = { slideOutOfContainerAnimation() }
//        ) {
//            PatientListingScreen(
//                onBack = {
//                    navController.popBackStack()
//                }
//            )
//        }

    }
}