package com.lukecourt.scjournal.data

import android.content.Context
import androidx.compose.ui.input.key.type
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken // For loading lists
import java.io.File
import java.io.IOException

class JsonStorageManagerGson(val context: Context) {

    val gson: Gson = Gson()

    // Generic function to save any type of object
    fun <T> saveObjectAsJson(fileName: String, dataObject: T) {
        val jsonString = gson.toJson(dataObject)

        try {
            val file = File(context.filesDir, fileName) // Saves in app's internal storage
            file.writeText(jsonString)
            println("Successfully saved to ${file.absolutePath}")
        } catch (e: IOException) {
            e.printStackTrace()
            println("Error saving JSON: ${e.message}")
        }
    }

    // Generic function to load any type of object
    inline fun <reified T> loadObjectFromJson(fileName: String): T? {
        return try {
            val file = File(context.filesDir, fileName)
            if (!file.exists()) {
                println("File not found: $fileName")
                return null
            }
            val jsonString = file.readText()
            // For simple objects, T::class.java works.
            // For generic types like List<MyDataObject>, you need TypeToken.
            val type = object : TypeToken<T>() {}.type
            gson.fromJson(jsonString, type)
        } catch (e: IOException) {
            e.printStackTrace()
            println("Error reading JSON: ${e.message}")
            null
        } catch (e: Exception) { // Catch broader JSON parsing errors
            e.printStackTrace()
            println("Error parsing JSON: ${e.message}")
            null
        }
    }
}