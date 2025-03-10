package com.uiel.clickergame

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.uiel.clickergame.component.BottomNavItem
import com.uiel.clickergame.component.CkBottomNavigation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun CkApp(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val _selectedItem = MutableStateFlow<BottomNavItem>(BottomNavItem.Home)
    val selectedItem: StateFlow<BottomNavItem> = _selectedItem.asStateFlow()

    Scaffold(
        modifier = modifier,
        bottomBar = {
            CkBottomNavigation(
                navController = navController,
                selectedItem = selectedItem.collectAsState().value,
                onItemSelected = { _selectedItem.value = it }
            )
        }
    ) { innerPadding ->
        rememberSystemUiController().setSystemBarsColor(color = Color.Transparent)
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = "home"
        )
        {
            composable("home") {
                HomeScreen()
            }
            composable("ranking") {
                RankingScreen()
            }
        }
    }
}