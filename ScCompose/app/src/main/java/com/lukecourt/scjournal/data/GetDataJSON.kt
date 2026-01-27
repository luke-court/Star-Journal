package com.lukecourt.scjournal.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class GetDataJSON {

    fun Int.toBooleanFlag(): Boolean = this == 1

    suspend fun getLocations(address: String): MutableList<String> {
        val citiesList = mutableListOf<String>()
        val responseJSON = jsonRequest(address)

        responseJSON?.let {
            println(it)
            val citiesArray = it.getJSONArray("data")

            println(citiesArray)
            for (i in 0 until citiesArray.length()) {
                val cityObject = citiesArray.getJSONObject(i)
                val cityName = cityObject.getString("name")
                if (cityObject.getInt("is_available_live") == 1) {
                    citiesList.add(cityName)
                }
            }
        }

        return citiesList
    }

    suspend fun getCommodities(address: String): MutableList<CargoData> {
        val commodityList = mutableListOf<CargoData>()
        val responseJSON = jsonRequest(address)
        responseJSON.let {
            val commoditiesArray = it?.getJSONArray("data")
            println(commoditiesArray)

            for (i in 0 until commoditiesArray!!.length()) {
                val commodityObject = commoditiesArray.getJSONObject(i)
                val commodityName = commodityObject.getString("name")
                val commodityCode = commodityObject.getString("code")
                val commodityType = commodityObject.getString("kind")
                val commodityAvailable = commodityObject.getInt("is_available")
                val commodityAvailableLive = commodityObject.getInt("is_available_live")
                val commodityVisible = commodityObject.getInt("is_visible")
                val commodityExtractable = commodityObject.getInt("is_extractable")
                val commodityMineral = commodityObject.getInt("is_mineral")
                val commodityRaw = commodityObject.getInt("is_raw")
                val commodityPure = commodityObject.getInt("is_pure")
                val commodityRefined = commodityObject.getInt("is_refined")
                val commodityRefinable = commodityObject.getInt("is_refinable")
                val commodityHarvestable = commodityObject.getInt("is_harvestable")
                val commodityBuyable = commodityObject.getInt("is_buyable")
                val commoditySellable = commodityObject.getInt("is_sellable")
                val commodityTemp = commodityObject.getInt("is_temporary")
                val commodityVolatileQt = commodityObject.getInt("is_volatile_qt")
                val commodityVolatileTime = commodityObject.getInt("is_volatile_time")
                val commodityInert = commodityObject.getInt("is_inert")
                val commodityExplosive = commodityObject.getInt("is_explosive")
                val commodityFuel = commodityObject.getInt("is_fuel")
                val commodityBuggy = commodityObject.getInt("is_buggy")
                val cargoData = CargoData(
                    commodityName,
                    commodityCode,
                    commodityType,
                    commodityAvailable.toBooleanFlag(),
                    commodityAvailableLive.toBooleanFlag(),
                    commodityVisible.toBooleanFlag(),
                    commodityExtractable.toBooleanFlag(),
                    commodityMineral.toBooleanFlag(),
                    commodityRaw.toBooleanFlag(),
                    commodityPure.toBooleanFlag(),
                    commodityRefined.toBooleanFlag(),
                    commodityRefinable.toBooleanFlag(),
                    commodityHarvestable.toBooleanFlag(),
                    commodityBuyable.toBooleanFlag(),
                    commoditySellable.toBooleanFlag(),
                    commodityTemp.toBooleanFlag(),
                    commodityVolatileQt.toBooleanFlag(),
                    commodityVolatileTime.toBooleanFlag(),
                    commodityInert.toBooleanFlag(),
                    commodityExplosive.toBooleanFlag(),
                    commodityFuel.toBooleanFlag(),
                    commodityBuggy.toBooleanFlag()
                )
                commodityList.add(cargoData)
            }
        }

        return commodityList
    }

    private suspend fun jsonRequest(requestURL: String): JSONObject? {
        val responseJSON = withContext(Dispatchers.IO) {
            try {
                val response = URL(requestURL).readText()
                JSONObject(response)
            } catch (e: Exception) {
                // Handle exceptions (e.g., network error, malformed JSON)
                println("Error fetching request: ${e.message}")
                null // Or throw a custom exception
            }
        }
        return responseJSON
    }

}