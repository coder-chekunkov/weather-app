package com.cdr.weather_app.model.storage_worker

import android.content.Context
import com.cdr.core.model.Repository
import com.google.gson.Gson
import org.json.JSONArray
import java.io.*

typealias StorageCitiesListener = (favoriteCities: List<StorageFavoriteCity>) -> Unit

class StorageRepository(private val appContext: Context) : Repository {
    private var favoriteCities = mutableListOf<StorageFavoriteCity>()
    private val storageCitiesListeners = mutableListOf<StorageCitiesListener>()

    fun readFavoriteCitiesFromStorage(): List<StorageFavoriteCity> {
        val dataFromStorage = ArrayList<StorageFavoriteCity>()

        return if (File(appContext.filesDir.absolutePath + "/" + FAVORITE_DATA_FILE_NAME).exists()) {
            val data = getStringDataFromStorage()
            val jsonRoot = JSONArray(data)
            for (i in 0 until jsonRoot.length()) {
                val cityObject = jsonRoot.getJSONObject(i)
                dataFromStorage.add(
                    StorageFavoriteCity(
                        id = cityObject.getLong("id"),
                        linkName = cityObject.getString("linkName")
                    )
                )
            }
            dataFromStorage
        } else emptyList()
    }

    private fun getStringDataFromStorage(): String {
        val file = StringBuilder()
        val inputStream: InputStream = appContext.openFileInput(FAVORITE_DATA_FILE_NAME)
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        var s: String?
        while (bufferedReader.readLine().also { s = it } != null) {
            file.append(s)
            file.append("\n")
        }
        return file.toString()
    }

    private fun saveFavoriteCitiesInStorage() {
        val data = Gson().toJson(favoriteCities)
        val writer = OutputStreamWriter(
            appContext.openFileOutput(FAVORITE_DATA_FILE_NAME, Context.MODE_PRIVATE)
        )
        writer.write(data)
        writer.close()
    }

    fun likeCity(cityId: Long, cityLinkName: String) {
        val index = favoriteCities.indexOfFirst { it.id == cityId }

        favoriteCities = ArrayList(favoriteCities)
        if (index == -1) favoriteCities.add(
            StorageFavoriteCity(
                id = cityId,
                linkName = cityLinkName
            )
        )
        else favoriteCities.removeAt(index)

        notifyChanges()
        saveFavoriteCitiesInStorage()
    }

    fun addListener(listener: StorageCitiesListener) {
        storageCitiesListeners.add(listener)
        listener.invoke(favoriteCities)
    }

    fun removeListener(listener: StorageCitiesListener) {
        storageCitiesListeners.remove(listener)
        listener.invoke(favoriteCities)
    }

    private fun notifyChanges() = storageCitiesListeners.forEach { it.invoke(favoriteCities) }

    companion object {
        private const val FAVORITE_DATA_FILE_NAME = "FAVORITE_CITIES"
    }
}