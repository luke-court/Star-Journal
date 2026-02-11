package com.lukecourt.starjournal

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lukecourt.starjournal.ui.home.HomeScreen
import com.lukecourt.starjournal.ui.missions.AddMissionScreen
import com.lukecourt.starjournal.ui.missions.MissionsScreen
import com.lukecourt.starjournal.ui.settings.SettingsScreen
import com.lukecourt.starjournal.ui.missions.MissionDetailsScreen
import com.lukecourt.starjournal.viewModels.MissionsViewModel

@Composable
fun SCJournalNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    missionsVM: MissionsViewModel = viewModel(),
) {
    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier
    ) {
        composable(route = Home.route) {
            HomeScreen(navController, missionsVM = missionsVM)
        }
        composable(route = Settings.route) {
            SettingsScreen(navController)
        }
        composable (route = Missions.route) {
            MissionsScreen(navController, missionsViewModel = missionsVM)
        }
        composable(route = MissionDetails.routeWithArgs, arguments = MissionDetails.arguments) {
            navBackStackEntry -> val missionId = navBackStackEntry.arguments?.getString(MissionDetails.missionIdArg)
            MissionDetailsScreen(navController, missionId.toString(), missionsViewModel = missionsVM)
        }
        composable(route = AddMission.route) {
            AddMissionScreen(navController, missionsViewModel = missionsVM)
        }
    }
}
