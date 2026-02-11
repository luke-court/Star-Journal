package com.lukecourt.starjournal

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed interface SCDestination {
    val icon: ImageVector
    val route: String
}

data object Home : SCDestination {
    override val icon = Icons.Filled.Home
    override val route = "Home"
}

data object Missions : SCDestination {
    override val icon = Icons.AutoMirrored.Default.List
    override val route = "Missions"
}

data object AddMission : SCDestination {
    override val icon = Icons.AutoMirrored.Default.List
    override val route = "addMission"
}

data object MissionDetails : SCDestination {
    override val icon = Icons.AutoMirrored.Default.List
    override val route = "missionDetails"
    const val missionIdArg = "missionId"
    val routeWithArgs = "$route/{$missionIdArg}"
    val arguments = listOf(navArgument(missionIdArg) { type = NavType.StringType })
}

data object Settings : SCDestination {
    override val icon = Icons.Filled.Settings
    override val route = "Settings"
}

data object AddCargoObject : SCDestination {
    override val icon = Icons.AutoMirrored.Default.List
    override val route = "addCargoObject"
    const val cargoListArg = "cargoList"
    val routeWithArgs = "$route/{$cargoListArg}"
    val arguments = listOf(navArgument(cargoListArg) { type = NavType.StringType })

}
