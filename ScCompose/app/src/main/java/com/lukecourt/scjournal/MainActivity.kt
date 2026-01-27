package com.lukecourt.scjournal

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lukecourt.scjournal.ui.theme.SCJournalComposeTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.lukecourt.scjournal.viewModels.DataViewModel
import com.lukecourt.scjournal.viewModels.MissionsViewModel

class MainActivity : ComponentActivity() {


    val dataViewModel = DataViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScJournalApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScJournalApp() {
    SCJournalComposeTheme {
        val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        when (navBackStackEntry?.destination?.route) {
            Missions.route -> {
                bottomBarState.value = true
            }
            Home.route -> {
                bottomBarState.value = true
            }
            Settings.route -> {
                bottomBarState.value = true
            }
            else -> {
                bottomBarState.value = false
            }
        }
        Scaffold (bottomBar = { BottomNavBar(
            navController = navController, bottomBarState = bottomBarState
        ) }) { innerPadding ->
            SCJournalNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}


@Composable
private fun BottomNavBar(modifier: Modifier = Modifier,
                         navController: NavController,
                         bottomBarState: MutableState<Boolean>) {
    AnimatedVisibility(visible = bottomBarState.value) {
        NavigationBar (modifier = modifier) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = "Home"
                    )
                },
                selected = currentDestination?.route == Home.route,
                onClick = {
                    navController.navigate(Home.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.List,
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = "Missions"
                    )
                },
                selected = currentDestination?.route == Missions.route,
                onClick = {
                    navController.navigate(Missions.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
//            NavigationBarItem(
//                icon = {
//                    Icon(
//                        imageVector = Icons.Default.Settings,
//                        contentDescription = null
//                    )
//                },
//                label = {
//                    Text(
//                        text = "Settings"
//                    )
//                },
//                selected = currentDestination?.route == Settings.route,
//                onClick = {
//                    navController.navigate(Settings.route) {
//                        popUpTo(navController.graph.findStartDestination().id) {
//                            saveState = true
//                        }
//                        // Avoid multiple copies of the same destination when
//                        // reselecting the same item
//                        launchSingleTop = true
//                        // Restore state when reselecting a previously selected item
//                        restoreState = true
//                    }
//                }
//            )
        }
    }


}

// Previews

@PreviewLightDark
@Composable
fun BottomNavBarPreview() {
    SCJournalComposeTheme {
        BottomNavBar(navController = rememberNavController(), bottomBarState = rememberSaveable { (mutableStateOf(true)) })
    }
}

@PreviewLightDark
@Composable
fun ScJournalAppPreview() {
    SCJournalComposeTheme {
        ScJournalApp()
    }
}
