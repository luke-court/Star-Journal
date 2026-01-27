package com.lukecourt.scjournal.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lukecourt.scjournal.data.JsonStorageManagerGson
import com.lukecourt.scjournal.data.Mission
import com.lukecourt.scjournal.data.MissionStatus
import com.lukecourt.scjournal.data.setLastEdited
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MissionsViewModel(application: Application) : AndroidViewModel(application) {

    // mission list
    private var missionsLocation = "missionStorage.json"
    private val storageManager = JsonStorageManagerGson(application)
    private var _missions = MutableStateFlow(listOf<Mission>())
    var missions: StateFlow<List<Mission>> = _missions

    // access information
    private val _lastModified = MutableStateFlow(System.currentTimeMillis())
    private val _lastAccessed = MutableStateFlow(System.currentTimeMillis())

    init {
        val loadObjectFromJson = storageManager.loadObjectFromJson<MutableList<Mission>>(missionsLocation)
        if (loadObjectFromJson == null) {
            _missions = MutableStateFlow(listOf<Mission>())
        } else {
            var loadedMissions = loadObjectFromJson
            _missions = MutableStateFlow(loadedMissions)
            _lastModified.value = System.currentTimeMillis()
            _lastAccessed.value = System.currentTimeMillis()
            missions = _missions
        }
    }

    fun createMission(newMission: Mission) {
        viewModelScope.launch {
            _missions.value = _missions.value + newMission
            missions = _missions
            _lastModified.value = System.currentTimeMillis()
            storageManager.saveObjectAsJson(missionsLocation, _missions.value)
        }

    }

    fun updateMission(updatedMission: Mission) {
        viewModelScope.launch {
            _missions.value = _missions.value.map { mission ->
                if (mission.id == updatedMission.id) {
                    updatedMission
                } else {
                    mission
                }
            }
            missions = _missions
            _lastModified.value = System.currentTimeMillis()
            storageManager.saveObjectAsJson(missionsLocation, _missions.value)
        }
    }

    fun updateMissionStatus(missionId: String, newStatus: MissionStatus) {
        viewModelScope.launch {
            _missions.update { currentList ->
                currentList.map {
                    if (it.id == missionId) {
                        it.setLastEdited()
                        it.copy(status = newStatus)
                    } else {
                        it
                    }
                }
            }
            // Here you would also persist this change to your backend or local database
            println("Mission $missionId status updated to $newStatus")
            storageManager.saveObjectAsJson(missionsLocation, _missions.value)
        }
    }

    fun getMissionsSize(): Int {
        return _missions.value.size
    }

    fun getMissions(): List<Mission> {
        return _missions.value
    }

    fun getCompletedMissionsTotal(): Int {
        return _missions.value.filter { it.status == MissionStatus.COMPLETED }.size
    }

    fun getPendingMissionsTotal(): Int {
        return _missions.value.filter { it.status == MissionStatus.PENDING }.size
    }

    fun gettotalEarned(): Int {
        return _missions.value.filter { it.status == MissionStatus.COMPLETED }.sumOf { it.reward }
    }
}