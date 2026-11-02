package com.example.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Rotina : Screen(
        route = "rotina",
        title = "Rotina",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    object Receitas : Screen(
        route = "receitas",
        title = "Receitas",
        selectedIcon = Icons.Filled.Spa,
        unselectedIcon = Icons.Outlined.Spa
    )

    object Scan : Screen(
        route = "scan",
        title = "Scan",
        selectedIcon = Icons.Filled.Face,
        unselectedIcon = Icons.Outlined.Face
    )

    object Diario : Screen(
        route = "diario",
        title = "Diário",
        selectedIcon = Icons.Filled.PhotoLibrary,
        unselectedIcon = Icons.Outlined.PhotoLibrary
    )

    object Perfil : Screen(
        route = "perfil",
        title = "Perfil",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )

    companion object {
        val bottomNavItems = listOf(Rotina, Receitas, Scan, Diario, Perfil)
    }
}
