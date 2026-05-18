package com.example.nammashasana.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

sealed class Screen(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Discover : Screen("discover", "Discover", Icons.Default.Home)
    object Map : Screen("map", "Trail Map", Icons.Default.Map)
    object Explore : Screen("explore", "Explore", Icons.Default.Explore)
    object Passport : Screen("passport", "Passport", Icons.Default.Badge)
}

@Composable
fun MainScreen(viewModel: ShasanaViewModel) {
    val navController = rememberNavController()
    val items = listOf(Screen.Discover, Screen.Map, Screen.Explore, Screen.Passport)

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            
            // Only show bottom bar on top-level screens
            if (currentDestination?.route in items.map { it.route }) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(screen.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Discover.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Discover.route) {
                DiscoverScreen(
                    viewModel = viewModel,
                    onInscriptionClick = { id ->
                        navController.navigate("story/$id")
                    }
                )
            }
            composable(Screen.Map.route) {
                MapScreen(
                    viewModel = viewModel,
                    onInscriptionClick = { id ->
                        navController.navigate("story/$id")
                    },
                    onAddClick = {
                        navController.navigate("add")
                    }
                )
            }
            composable(Screen.Explore.route) {
                InscriptionListScreen(
                    viewModel = viewModel,
                    onInscriptionClick = { id ->
                        navController.navigate("story/$id")
                    }
                )
            }
            composable(Screen.Passport.route) {
                PassportScreen(
                    viewModel = viewModel,
                    onInscriptionClick = { id ->
                        navController.navigate("story/$id")
                    }
                )
            }
            composable(
                route = "story/{id}",
                arguments = listOf(navArgument("id") { type = NavType.LongType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getLong("id") ?: return@composable
                StoryScreen(
                    inscriptionId = id,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable("add") {
                AddInscriptionScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
