package com.rajk2007.kino

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.rajk2007.kino.ui.navigation.Screen
import com.rajk2007.kino.ui.screens.*
import com.rajk2007.kino.ui.theme.KinoColors
import com.rajk2007.kino.ui.theme.KinoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KinoTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                val showBottomBar = currentDestination?.route in listOf(
                    Screen.Home.route,
                    Screen.Search.route,
                    Screen.Library.route,
                    Screen.Profile.route
                )

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            NavigationBar(
                                containerColor = Color(0xF7080808),
                                contentColor = KinoColors.Red
                            ) {
                                val items = listOf(
                                    Triple(Screen.Home, "Home", Icons.Default.Home),
                                    Triple(Screen.Search, "Search", Icons.Default.Search),
                                    Triple(Screen.Library, "Library", Icons.Default.VideoLibrary),
                                    Triple(Screen.Profile, "Profile", Icons.Default.Person)
                                )
                                items.forEach { (screen, label, icon) ->
                                    NavigationBarItem(
                                        icon = { Icon(icon, contentDescription = label) },
                                        label = { Text(label) },
                                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = KinoColors.Red,
                                            selectedTextColor = KinoColors.Red,
                                            unselectedIconColor = KinoColors.Muted,
                                            unselectedTextColor = KinoColors.Muted,
                                            indicatorColor = Color.Transparent
                                        )
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Splash.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Splash.route) {
                            val context = androidx.compose.ui.platform.LocalContext.current
                            SplashScreen(onTimeout = {
                                val isInstalled = com.rajk2007.kino.plugin.PluginManager.isInstalled(context)
                                if (isInstalled) {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Splash.route) { inclusive = true }
                                    }
                                } else {
                                    navController.navigate("installer") {
                                        popUpTo(Screen.Splash.route) { inclusive = true }
                                    }
                                }
                            })
                        }
                        composable("installer") { InstallerScreen(navController) }
                        composable(Screen.Home.route) { HomeScreen(navController) }
                        composable(Screen.Search.route) { SearchScreen(navController) }
                        composable(Screen.Library.route) { LibraryScreen(navController) }
                        composable(Screen.Profile.route) { ProfileScreen(navController) }
                        composable(Screen.Details.route) { backStackEntry ->
                            val type = backStackEntry.arguments?.getString("type") ?: "movie"
                            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
                            DetailsScreen(navController, type, id)
                        }
                        composable(Screen.Player.route) { backStackEntry ->
                            val type = backStackEntry.arguments?.getString("type") ?: "movie"
                            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
                            PlayerScreen(navController, type, id)
                        }
                    }
                }
            }
        }
    }
}
