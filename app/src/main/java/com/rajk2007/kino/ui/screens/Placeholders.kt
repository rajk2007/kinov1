package com.rajk2007.kino.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Home Screen") }
}

@Composable
fun SearchScreen(navController: NavController) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Search Screen") }
}

@Composable
fun LibraryScreen(navController: NavController) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Library Screen") }
}

@Composable
fun ProfileScreen(navController: NavController) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Profile Screen") }
}

@Composable
fun DetailsScreen(navController: NavController, type: String, id: Int) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Details Screen: $type $id") }
}

@Composable
fun PlayerScreen(navController: NavController, type: String, id: Int) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Player Screen: $type $id") }
}
