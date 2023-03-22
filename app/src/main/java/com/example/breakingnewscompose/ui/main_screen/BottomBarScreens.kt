package com.example.breakingnewscompose.ui.main_screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreens(
    val route: String,
    val title: String,
    val icon: ImageVector
){
    object Home : BottomBarScreens(route = "Home", title = "Home", icon = Icons.Filled.Home)
    object Search : BottomBarScreens(route = "Search", "Search", Icons.Filled.Search)

    object Favorites : BottomBarScreens(route = "Favorites", title = "Favorites", icon = Icons.Default.Favorite)
}
