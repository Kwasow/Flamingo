package pl.kwasow.sunshine.managers

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import kotlinx.serialization.json.Json
import pl.kwasow.sunshine.BuildConfig
import pl.kwasow.sunshine.data.Memory
import pl.kwasow.sunshine.data.User
import java.io.File

class SystemManagerImpl(
    private val context: Context,
) : SystemManager {
    // ====== Fields
    companion object {
        private const val MEMORIES_CACHE_FILE = "memories_cache.json"
        private const val USER_CACHE_FILE = "user_cache.json"
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
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}"),
                ),
            )
        } catch (_: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        "https://play.google.com/store/apps/details?id=" +
                            BuildConfig.APPLICATION_ID,
                    ),
                ),
            )
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
            null
        }
    }
}
