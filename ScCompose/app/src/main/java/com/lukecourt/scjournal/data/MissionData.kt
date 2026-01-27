package com.lukecourt.scjournal.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.Serializable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Define an enum for MissionStatus for better type safety
enum class MissionStatus {
    ACTIVE, IN_PROGRESS, COMPLETED, FAILED, ON_HOLD, PENDING;

    // Optional: for display purposes
    override fun toString(): String {
        return name.replace("_", " ").lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}

enum class MissionType {
    MISSION,
    CARGO_MISSION
}

fun MissionType.toValue(): String {
    return when (this) {
        MissionType.MISSION -> "Regular Mission"
        MissionType.CARGO_MISSION -> "Cargo Mission"
    }
}

@Serializable
data class Mission(
    val id: String,
    val title: String,
    val description: String,
    var status: MissionStatus,
    val reward: Int,
    var cargoMission: CargoMission? = null,
    var lastEdited: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        .format(System.currentTimeMillis())
)

fun Mission.setCargoMission(cargoMission: CargoMission) {
    this.cargoMission = cargoMission

}

fun Mission.getLastEdited(): String {
    return lastEdited
}

fun Mission.setLastEdited() {
    lastEdited = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        .format(System.currentTimeMillis())
}

@Serializable
data class CargoItem(
    val id: Int,
    val type: String,
    val quantity: Int,
    val origin: String,
    val destination: String
)

@Serializable
data class CargoMission(
    val cargoList: List<CargoItem>
)