package com.lukecourt.scjournal.ui.missions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.core.graphics.forEach
import androidx.core.graphics.values
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lukecourt.scjournal.data.Mission
import com.lukecourt.scjournal.data.GetDataJSON
import com.lukecourt.scjournal.data.MissionStatus
import com.lukecourt.scjournal.ui.CargoItemDisplay
import com.lukecourt.scjournal.ui.theme.SCJournalComposeTheme
import com.lukecourt.scjournal.viewModels.MissionsViewModel


/**
 * Composable function that displays the details of a specific mission.
 *
 * This screen shows the mission's title in the top app bar and its description and status in the content area.
 * It uses a [Scaffold] for the basic layout structure.
 *
 * @param navController The [NavController] used for navigation, specifically to go back to the previous screen.
 * @param missionIDArg The ID of the mission to display, passed as a navigation argument.
 * @param missionsViewModel The [com.lukecourt.scjournal.viewModels.MissionsViewModel] instance, defaulting to a ViewModel provided by `viewModel()`. This ViewModel is used to fetch the mission details.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissionDetailsScreen(navController: NavController, missionIDArg: String, missionsViewModel: MissionsViewModel = viewModel()) {
    val missionsState by missionsViewModel.missions.collectAsState()
    val mission = missionsState.find { it.id == missionIDArg } as Mission
    val dataControl = GetDataJSON()
    // dataControl.getCities()
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text(text = "Details for ${mission.title}") },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.primaryContainer,
//                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
//                ),
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
//                    }
//                }
//            )
//        },
//    ) { paddingValues ->
//            Box (modifier = Modifier.padding(paddingValues)) {
//                    Row {
//                        MissionDescription(mission.description.toString())
//                        Text(text = mission.status.toString())
//
//                    }
//        }
//
//    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = mission?.title ?: "Mission Details") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        mission?.let { currentMission ->
            MissionDetailsContent(
                mission = currentMission,
                onStatusChange = { newStatus ->
                    missionsViewModel.updateMissionStatus(currentMission.id, newStatus)
                },
                modifier = Modifier.padding(paddingValues)
            )
        } ?: run {
            // Show a loading indicator or an error message if the mission is null
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator() // Or Text("Mission not found")
            }
        }
    }

}

@Composable
fun MissionDetailsContent(
    mission: Mission,
    onStatusChange: (MissionStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row {
            Column (
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = mission.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Column {
                Text(
                    text = mission.reward.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
            }

        }

        HorizontalDivider()

        Text(
            text = "Description",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = mission.description,
            style = MaterialTheme.typography.bodyLarge
        )

        HorizontalDivider()

        StatusSelector(
            currentStatus = mission.status,
            onStatusSelected = onStatusChange
        )

        if (mission.cargoMission != null) {

            HorizontalDivider()

            Text(
                text = "Cargo",
                style = MaterialTheme.typography.titleMedium
            )
            mission.cargoMission!!.cargoList.forEach { cargo -> CargoItemDisplay(cargoItem = cargo) }
        }

        HorizontalDivider()

        // Add more details as needed
        // e.g., Text("Reward: ${mission.reward}")
        // e.g., Text("Location: ${mission.location}")
    }
}

@Composable
fun StatusSelector(
    currentStatus: MissionStatus,
    onStatusSelected: (MissionStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val availableStatuses = MissionStatus.entries.toTypedArray()

    Column(modifier = modifier) {
        Text(
            text = "Status",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedCard( // Or use an OutlinedTextField for a different look
            modifier = Modifier.fillMaxWidth(),
            onClick = { expanded = true }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = currentStatus.toString(), // Display current status
                    style = MaterialTheme.typography.bodyLarge
                )
                Icon(Icons.Filled.ArrowDropDown, contentDescription = "Change Status")
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            availableStatuses.forEach { status ->
                DropdownMenuItem(
                    text = { Text(status.toString()) },
                    onClick = {
                        onStatusSelected(status)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
/**
 * Composable function that displays the description of a mission within a Card.
 *
 * This function takes a mission description string and presents it inside a [Card]
 * with padding for visual separation.
 *
 * @param missionDescription The string containing the mission's description.
 */
fun MissionDescription(missionDescription: String) {
    Card (
        modifier = Modifier
            .padding(vertical = 8.dp)
    ) {
        Column (
            modifier = Modifier
                .padding(19.dp)
        ) {
            Text(text = missionDescription)
        }
    }
}


@PreviewLightDark
@Composable
fun StatusSelectorPreview() {
    SCJournalComposeTheme {
        StatusSelector(
            currentStatus = MissionStatus.IN_PROGRESS,
            onStatusSelected = {}
        )
    }
}

@PreviewLightDark
@Composable
fun DescriptionPreview() {
    SCJournalComposeTheme {
        MissionDescription(missionDescription = "This is a mission description")
    }
}

@PreviewLightDark
@Composable
fun DetailsPreview() {
    SCJournalComposeTheme {
        MissionDetailsScreen(
            navController = NavController(LocalContext.current),
            missionIDArg = "1"
        )
    }
}


