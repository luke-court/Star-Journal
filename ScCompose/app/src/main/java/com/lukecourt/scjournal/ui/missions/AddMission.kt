package com.lukecourt.scjournal.ui.missions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lukecourt.scjournal.Missions
import com.lukecourt.scjournal.data.CargoItem
import com.lukecourt.scjournal.data.CargoMission
import com.lukecourt.scjournal.data.Mission
import com.lukecourt.scjournal.data.MissionStatus
import com.lukecourt.scjournal.data.MissionType
import com.lukecourt.scjournal.data.toValue
import com.lukecourt.scjournal.verifyInt
import com.lukecourt.scjournal.ui.theme.SCJournalComposeTheme
import com.lukecourt.scjournal.viewModels.DataViewModel
import com.lukecourt.scjournal.viewModels.MissionsViewModel
import com.lukecourt.scjournal.ui.CargoItemDisplay
import kotlinx.coroutines.flow.MutableStateFlow
import org.intellij.lang.annotations.JdkConstants


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMissionScreen(
    navController: NavController,
    missionsViewModel: MissionsViewModel = viewModel()
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedMissionType by remember { mutableStateOf(MissionType.MISSION) }
    var missionName by remember { mutableStateOf("") }
    var missionDescription by remember { mutableStateOf("") }
    var missionReward by remember { mutableStateOf("") }
    var cargoItems = remember { mutableStateOf(listOf<CargoItem>()) }

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Missions") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = { BottomAppBar {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = {
                            missionSubmit(
                                selectedMissionType,
                                missionName,
                                missionDescription,
                                missionReward,
                                missionsViewModel,
                                navController,
                                cargoItems.value
                            )
                          },
            ) {
                Text("Submit")
            }
        } }
    ) { paddingValues ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
            ) {

                OutlinedTextField(
                    value = missionName,
                    onValueChange = { missionName = it },
                    label = { Text("Mission Name") }
                )
                OutlinedTextField(
                    value = missionDescription,
                    onValueChange = { missionDescription = it },
                    label = { Text("Mission Description") }
                )
                OutlinedTextField(
                    value = missionReward,
                    onValueChange = { if (missionRewardVerify(it)) missionReward = it },
                    label = { Text("Mission Reward") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                Box (modifier = Modifier
                    .padding(0.dp, 16.dp, 32.dp, 0.dp)
                    .border(BorderStroke(1.dp, Color.Gray))
                    .fillMaxWidth()) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                expanded = true
                            }
                            .padding(8.dp)
                    ) {
                        Text(selectedMissionType.toValue())
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Drop-down Icon")
                    }


                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(
                            text = { Text("Regular Mission") },
                            onClick = {
                                selectedMissionType = MissionType.MISSION
                                expanded = false
                            }
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("Cargo Mission") },
                            onClick = {
                                selectedMissionType = MissionType.CARGO_MISSION
                                expanded = false
                            }
                        )
                    }
                }

                if (selectedMissionType == MissionType.CARGO_MISSION) {
                    CargoMissionDetails(cargoItemsList = cargoItems)
                }
            }
    }
}

fun missionRewardVerify(input: String): Boolean {
    return input.isDigitsOnly()
}

fun missionSubmit(selectedMissionType: MissionType, missionName: String, missionDescription: String, missionReward: String, missionsViewModel: MissionsViewModel, navController: NavController,
                  cargoItems: List<CargoItem> = listOf()) {
    if (selectedMissionType == MissionType.MISSION) {
        missionsViewModel.createMission(Mission(
            id = (missionsViewModel.missions.value.size + 1).toString(),
            title = missionName,
            description = missionDescription,
            status = MissionStatus.PENDING,
            reward = missionReward.toInt()
        ))
        navController.navigate(Missions.route)
    }
    else if (selectedMissionType == MissionType.CARGO_MISSION) {
        val newMission = Mission(
            id = (missionsViewModel.missions.value.size + 1).toString(),
            title = missionName,
            description = missionDescription,
            status = MissionStatus.PENDING,
            reward = missionReward.toInt(),
        )
        newMission.cargoMission = CargoMission(
            cargoList = cargoItems
        )
        missionsViewModel.createMission(
            newMission
        )
        navController.navigate(Missions.route)
    }
    else {
        println("Something went wrong")
    }
}

@Composable
fun CargoMissionDetails(cargoItemsList: MutableState<List<CargoItem>> ){

    val openDialog = remember { mutableStateOf(false) }

    Column(modifier = Modifier) {
        Text("Cargo Mission Details")
        Button({ openDialog.value = true }) {
            Text("New Cargo Item")
        }

        HorizontalDivider()

        when {
            !openDialog.value -> {
                println(cargoItemsList.value.toString())
                cargoItemsList.value.forEach {
                    CargoItemDisplay(it)
                }
            }

        }

    }


    @Composable
    fun AddCargo(
        dataViewModel: DataViewModel = viewModel(),
        onDismissRequest: () -> Unit,
        closeDialog: () -> Unit
    )  {

        val commoditiesList = dataViewModel.commoditiesList.collectAsState()
        val cargoLocations = dataViewModel.cargoLocations

        val offset = remember { mutableStateOf(0f) }
        var selectedCargoType by remember { mutableStateOf("Cargo Type") }
        var cargoOrigin by remember { mutableStateOf("Cargo Collection Location") }
        var cargoDestination by remember { mutableStateOf("Cargo Delivery Location") }
        var expandedType by remember { mutableStateOf(false) }
        var expandedOrigin by remember { mutableStateOf(false) }
        var expandedDestination by remember { mutableStateOf(false) }
        var cargoAmount by remember { mutableStateOf("") }

        Dialog(
            onDismissRequest = { onDismissRequest() },
        ) {
            Card (
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(400.dp, 460.dp)
                    .scrollable(
                        orientation = Orientation.Vertical,
                        state = rememberScrollableState { delta -> offset.value += delta; delta }
                    )
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column (
                    modifier = Modifier.fillMaxSize()
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box (modifier = Modifier
                        .padding(0.dp, 16.dp, 32.dp, 0.dp)
                        .border(BorderStroke(1.dp, Color.Gray))
                        .fillMaxWidth()) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    expandedType = true
                                }
                                .padding(8.dp)
                        ) {
                            Text(selectedCargoType)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Drop-down Icon")
                        }

                        DropdownMenu(
                            expanded = expandedType,
                            onDismissRequest = { expandedType = false },
                            Modifier
                                .fillMaxWidth()
                                .height(600.dp)
                                .scrollable(
                                    orientation = Orientation.Vertical,
                                    state = rememberScrollableState { delta -> offset.value += delta; delta }
                                )
                        ) {
                            commoditiesList.value.forEach {
                                DropdownMenuItem(
                                    text = { Text(it.cargoName) },
                                    onClick = {
                                        selectedCargoType = it.cargoName
                                        expandedType = false
                                    }
                                )
                            }
                        }
                    }

                    Box (modifier = Modifier.padding(0.dp, 16.dp, 32.dp, 0.dp)) {
                        OutlinedTextField(
                            value = cargoAmount,
                            onValueChange = { if (verifyInt(it)) cargoAmount = it },
                            label = { Text("Cargo Quantity (SCU)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        )
                    }

                    Box (modifier = Modifier
                        .padding(0.dp, 16.dp, 32.dp, 0.dp)
                        .border(BorderStroke(1.dp, Color.Gray))
                        .fillMaxWidth()) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    expandedOrigin = true
                                }
                                .padding(8.dp)
                        ) {
                            Text(cargoOrigin)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Drop-down Icon")
                        }

                        DropdownMenu(
                            expanded = expandedOrigin,
                            onDismissRequest = { expandedOrigin = false },
                            Modifier
                                .fillMaxWidth()
                                .height(600.dp)
                                .scrollable(
                                    orientation = Orientation.Vertical,
                                    state = rememberScrollableState { delta -> offset.value += delta; delta }
                                )
                        ) {
                            cargoLocations.forEach {
                                DropdownMenuItem(
                                    text = { Text(it) },
                                    onClick = {
                                        cargoOrigin = it
                                        expandedOrigin = false
                                    }
                                )
                            }
                        }
                    }

                    Box (modifier = Modifier
                        .padding(0.dp, 16.dp, 32.dp, 0.dp)
                        .border(BorderStroke(1.dp, Color.Gray))
                        .fillMaxWidth()) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    expandedDestination = true
                                }
                                .padding(8.dp)
                        ) {
                            Text(cargoDestination)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Drop-down Icon")
                        }

                        DropdownMenu(
                            expanded = expandedDestination,
                            onDismissRequest = { expandedDestination = false },
                            Modifier
                                .fillMaxWidth()
                                .height(600.dp)
                                .scrollable(
                                    orientation = Orientation.Vertical,
                                    state = rememberScrollableState { delta -> offset.value += delta; delta }
                                )
                        ) {
                            cargoLocations.forEach {
                                DropdownMenuItem(
                                    text = { Text(it) },
                                    onClick = {
                                        cargoDestination = it
                                        expandedDestination = false
                                    }
                                )
                            }
                        }
                    }

                    Row {
                        TextButton(
                            onClick = { onDismissRequest() },
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text("Dismiss")
                        }
                        TextButton(
                            onClick = {
                                cargoItemsList.value = cargoItemsList.value.plus(
                                    CargoItem(
                                        id = cargoItemsList.value.size + 1,
                                        type = selectedCargoType,
                                        quantity = cargoAmount.toInt(),
                                        origin = cargoOrigin,
                                        destination = cargoDestination
                                    )
                                )
                                println(cargoItemsList.value.toString())
                                closeDialog()
                            },
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text("Confirm")
                        }
                    }

                }
            }
        }

    }
    when {
        openDialog.value -> {
            AddCargo(
                onDismissRequest = { openDialog.value = false },
                closeDialog = {
                    openDialog.value = false
                }
            )
        }
    }
}

//@Composable
//fun CargoItemDisplay(cargoItem: CargoItem) {
//    Card (
//        modifier = Modifier
//            .padding(16.dp)
//            .fillMaxWidth(),
//    ) {
//        Row (
//            modifier = Modifier.padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Column (
//                modifier = Modifier.weight(1f)
//            ) {
//                Text(cargoItem.type)
//            }
//            Column {
//                Text("Quantity: " + cargoItem.quantity.toString() + " SCU")
//            }
//
//        }
//        Row (
//            modifier = Modifier.padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Column (
//                modifier = Modifier.weight(1f)
//            ) {
//                Text(cargoItem.origin)
//            }
//            Column (
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier.weight(1f)
//            ) {
//                Icon(
//                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
//                    contentDescription = "Arrow Forward"
//                )
//
//            }
//            Column (
//                modifier = Modifier.weight(1f)
//            ) {
//                Text(cargoItem.destination)
//            }
//
//        }
//    }
//}


@PreviewLightDark()
@Composable
fun CargoItemDisplayPreview() {
    SCJournalComposeTheme {
      CargoItemDisplay(
          CargoItem(
              id = 1,
              type = "Cargo Type",
              quantity = 1,
              origin = "Origin",
              destination = "Destination"
          )
      )
    }
}


@PreviewLightDark
@Composable
fun AddMissionPreview() {
    SCJournalComposeTheme {
        AddMissionScreen(NavController(LocalContext.current))
    }

}

@Preview(showBackground = true)
@Composable
fun CargoMissionDetailsPreview() {
    SCJournalComposeTheme {
        CargoMissionDetails(
            cargoItemsList = remember { mutableStateOf(listOf<CargoItem>()) }
        )
    }
}
