package com.lukecourt.scjournal.viewModels

import androidx.lifecycle.ViewModel
import com.lukecourt.scjournal.data.CargoData
import com.lukecourt.scjournal.data.GetDataJSON
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DataViewModel: ViewModel() {

    val cargoLocations = arrayListOf<String>()

    private val _dataController = GetDataJSON()
    private var _citiesList = mutableListOf<String>()
    private var _stationsList = mutableListOf<String>()
    private var _commoditiesListTemp = mutableListOf<CargoData>()
    private var _commoditiesList = MutableStateFlow<List<CargoData>>(_commoditiesListTemp)
    val commoditiesList: MutableStateFlow<List<CargoData>> = _commoditiesList
    val commoditiesListValue: List<CargoData> = commoditiesList.value

    init {
        fetchData()
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun fetchData() {
        GlobalScope.launch {
            _citiesList = _dataController.getLocations("https://api.uexcorp.space/2.0/cities")
            _stationsList = _dataController.getLocations("https://api.uexcorp.space/2.0/space_stations")
            cargoLocations.addAll(_citiesList)
            cargoLocations.addAll(_stationsList)
            _commoditiesListTemp = _dataController.getCommodities("https://api.uexcorp.space/2.0/commodities")
            _commoditiesList.value = _commoditiesListTemp
            commoditiesList.value = _commoditiesListTemp
            println(commoditiesList.value.get(0).cargoName)
        }
    }
}