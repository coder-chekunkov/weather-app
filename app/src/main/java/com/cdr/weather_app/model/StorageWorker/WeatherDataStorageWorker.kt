package com.cdr.weather_app.model.StorageWorker

import android.content.Context
import com.cdr.weather_app.model.WeatherData
import com.google.gson.Gson
import org.json.JSONArray
import java.io.*

class WeatherDataStorageWorker {

    fun saveWeatherData(context: Context, weatherData: List<WeatherData>) {
        val data = Gson().toJson(weatherData)

        val writer = OutputStreamWriter(
            context.openFileOutput(WEATHER_DATA_FILE_NAME, Context.MODE_PRIVATE)
        )
        writer.write(data)
        writer.close()
    }

    private fun readWeatherDataFile(context: Context): String {
        val file = StringBuilder()
        val inputStream: InputStream = context.openFileInput(WEATHER_DATA_FILE_NAME)

        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        var s: String?
        while (bufferedReader.readLine().also { s = it } != null) {
            file.append(s)
            file.append("\n")
        }

        return file.toString()
    }

    fun readWeatherData(context: Context): List<WeatherData> {
        val dataFromStorage = ArrayList<WeatherData>()
        val data = readWeatherDataFile(context)
        val jsonRoot = JSONArray(data)

        for (i in 0 until jsonRoot.length()) {
            val weatherObject = jsonRoot.getJSONObject(i)

            dataFromStorage.add(
                WeatherData(
                    id = weatherObject.getInt("id"),
                    name = weatherObject.getString("name"),
                    description = weatherObject.getString("description"),
                    temperature = weatherObject.getDouble("temperature").toFloat(),
                    windSpeed = weatherObject.getDouble("windSpeed").toFloat()
                )
            )
        }
        return createOrderedList(dataFromStorage)
    }

    private fun createOrderedList(dataFromStorage: ArrayList<WeatherData>): List<WeatherData> {
        val weatherData = mutableListOf<WeatherData>()
        ID_OF_CITIES.forEach {
            for (data in dataFromStorage) {
                if (it == data.id) weatherData.add(data)
            }
        }

        return weatherData
    }

    companion object {
        @JvmStatic
        val WEATHER_DATA_FILE_NAME = "weatherData.json"

        val ID_OF_CITIES = listOf(
            524901, 498817, 472757, 542415, 501183, 491422, 554234,
            499068, 473249, 553899, 580497, 473247, 479561, 1508291, 2123628, 1497337, 511565,
            491684, 500096, 563514
        )
    }
}