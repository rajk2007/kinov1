package com.rajk2007.kino.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Search : Screen("search")
    object Library : Screen("library")
    object Profile : Screen("profile")
    object Details : Screen("details/{type}/{id}") {
        fun createRoute(type: String, id: Int) = "details/$type/$id"
    }
    object Player : Screen("player/{type}/{id}") {
        fun createRoute(type: String, id: Int) = "player/$type/$id"
    }
}
