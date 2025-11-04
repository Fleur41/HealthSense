package com.sam.healthsense.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sam.healthsense.authentication.signin.SignInScreen
import com.sam.healthsense.authentication.signup.SignUpScreen
import com.sam.healthsense.components.slideIntoContainerAnimation
import com.sam.healthsense.components.slideOutOfContainerAnimation
import com.sam.healthsense.detail.DetailScreen
import com.sam.healthsense.home.HomeScreen
import com.sam.healthsense.home.HomeViewModel
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
                authViewModel =  hiltViewModel(backStackEntry),
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
            val parentEntry = remember(backStackEntry) {navController.getBackStackEntry(NavigationDestination.SignIn.route)}
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
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                homeViewModel = homeViewModel,
                onDetailClick = {navController.navigate(NavigationDestination.Detail.route)}
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
            val parentEntry = remember(backStackEntry) {navController.getBackStackEntry(NavigationDestination.Home.route)}
            DetailScreen(
                homeViewModel = hiltViewModel(parentEntry),
                onBackClick = {navController.popBackStack()}
            )
        }
    }
}