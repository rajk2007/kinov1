package com.rajk2007.kino.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kinov1.plugins.KinoPluginManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PluginManagementScreen(
    pluginManager: KinoPluginManager,
    onBack: () -> Unit
) {
    var plugins by remember { mutableStateOf(pluginManager.getPluginNames()) }
    var searchText by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Plugin Management") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search bar
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Search plugins") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            
            // Plugin list
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(plugins.filter { it.contains(searchText, ignoreCase = true) }) { pluginName ->
                    ListItem(
                        headlineContent = { Text(pluginName) },
                        trailingContent = {
                            IconButton(onClick = {
                                pluginManager.removePlugin(pluginName)
                                plugins = pluginManager.getPluginNames()
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}
