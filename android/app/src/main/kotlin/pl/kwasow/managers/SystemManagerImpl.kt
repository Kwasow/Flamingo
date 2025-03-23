package pl.kwasow.managers

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import kotlinx.serialization.json.Json
import pl.kwasow.BuildConfig
import pl.kwasow.data.Memory
import pl.kwasow.data.User
import pl.kwasow.utils.FlamingoLogger
import java.io.File

class SystemManagerImpl(
    private val context: Context,
) : SystemManager {
    // ====== Fields
    companion object {
        private const val MEMORIES_CACHE_FILE = "memories_cache.json"
        private const val USER_CACHE_FILE = "user_cache.json"
    }

    private val storeIntent by lazy {
        val intent =
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}"),
            )
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        return@lazy intent
    }

    private val webStoreIntent by lazy {
        val intent =
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}",
                ),
            )
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        return@lazy intent
    }

    private val json = Json { ignoreUnknownKeys = true }

    // ====== Interface methods
    override fun isInternetAvailable(): Boolean {
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    override fun cacheMemories(memories: Map<Int, List<Memory>>?) =
        cacheObject(
            memories,
            MEMORIES_CACHE_FILE,
        )

    override fun getCachedMemories(): Map<Int, List<Memory>>? = readCache(MEMORIES_CACHE_FILE)

    override fun cacheUser(user: User?) = cacheObject(user, USER_CACHE_FILE)

    override fun getCachedUser(): User? = readCache(USER_CACHE_FILE)

    override fun clearCache() {
        deleteCache(MEMORIES_CACHE_FILE)
        deleteCache(USER_CACHE_FILE)

        clearCoilCache()
    }

    @OptIn(ExperimentalCoilApi::class)
    override fun clearCoilCache() {
        val imageLoader = context.imageLoader

        imageLoader.diskCache?.clear()
        imageLoader.memoryCache?.clear()
    }

    override fun launchStore() {
        try {
            context.startActivity(storeIntent)
        } catch (_: ActivityNotFoundException) {
            context.startActivity(webStoreIntent)
        }
    }

    // ====== Private methods
    private fun deleteCache(filename: String) {
        val file = File(context.cacheDir, filename)
        file.delete()
    }

    private inline fun <reified T> cacheObject(
        obj: T?,
        filename: String,
    ) {
        obj?.let {
            val file = File(context.cacheDir, filename)
            file.writeText(json.encodeToString(obj))
        }
    }

    private inline fun <reified T> readCache(filename: String): T? {
        val file = File(context.cacheDir, filename)
        if (!file.exists()) {
            return null
        }

        val rawJson = file.readText()
        return try {
            json.decodeFromString(rawJson)
        } catch (e: Exception) {
            // Delete the broken cache file
            file.delete()
            FlamingoLogger.e("Error while reading cache: $e", e)
            null
        }
    }
}
