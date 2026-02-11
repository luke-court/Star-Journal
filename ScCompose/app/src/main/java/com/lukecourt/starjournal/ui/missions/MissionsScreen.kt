package com.lukecourt.starjournal.ui.missions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lukecourt.starjournal.AddMission
import com.lukecourt.starjournal.MissionDetails
import com.lukecourt.starjournal.data.Mission
import com.lukecourt.starjournal.ui.theme.SCJournalComposeTheme
import com.lukecourt.starjournal.viewModels.MissionsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissionsScreen(
    navController: NavController,
    missionsViewModel: MissionsViewModel = viewModel()
) {
    val missionsState by missionsViewModel.missions.collectAsState(initial = emptyList<Mission>())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Missions") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Navigate to a screen to add a new mission
                navController.navigate(AddMission.route)
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Mission")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            items(missionsState) { mission ->
                MissionItem(mission = mission, onMissionClick = {
                    // Navigate to mission detail screen, passing mission ID
                    navController.navigate(MissionDetails.route + "/${mission.id}")
                })
            }
        }
    }
}

@Composable
fun MissionItem(mission: Mission, onMissionClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onMissionClick() }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = mission.title, style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
            Text(text = mission.description, style = androidx.compose.material3.MaterialTheme.typography.bodySmall)
            Text(text = "Status: ${mission.status}", style = androidx.compose.material3.MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
@PreviewLightDark
fun MissionsScreenPreview() {
    SCJournalComposeTheme {
        MissionsScreen(navController = NavController(LocalContext.current))
    }
}