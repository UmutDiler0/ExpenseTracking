package com.expense.expensetracking.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.expense.expensetracking.ui.theme.PrimaryGreen
import com.expense.expensetracking.ui.theme.SurfaceDark
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onNavigateOnboardingScreen: () -> Unit,
    onNavigateLoginScreen: () -> Unit,
    onNavigateHomeScreen: () -> Unit
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        delay(3000L)

        when {
            !state.isOnBoardingCompleted -> {
                onNavigateOnboardingScreen()
            }
            state.isRememberMe && state.email.isNotEmpty() && state.password.isNotEmpty() -> {
                onNavigateHomeScreen()
            }
            else -> {
                onNavigateLoginScreen()
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Rounded.AccountBalanceWallet,
            contentDescription = "",
            modifier = Modifier.size(160.dp),
            tint = PrimaryGreen
        )
    }
}
