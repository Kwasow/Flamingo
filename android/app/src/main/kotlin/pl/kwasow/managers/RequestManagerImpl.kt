package pl.kwasow.managers

import android.location.Location
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.path
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import pl.kwasow.BuildConfig
import pl.kwasow.data.types.Album
import pl.kwasow.data.types.AuthenticationResult
import pl.kwasow.data.types.Memory
import pl.kwasow.data.types.User
import pl.kwasow.data.types.UserLocation
import pl.kwasow.data.types.Wish
import pl.kwasow.utils.FlamingoLogger

class RequestManagerImpl(
    private val tokenManager: TokenManager,
) : RequestManager {
    // ====== Fields
    companion object {
        private const val HOST = BuildConfig.BASE_URL

        private const val PING_URL = "/api/ping.php"
        private const val AUTH_URL = "/api/auth.php"

        private const val POST_MESSAGE_URL = "/api/messaging/send.php"
        private const val POST_UPDATE_FCM_TOKEN_URL = "/api/messaging/updateToken.php"

        private const val GET_MEMORIES_URL = "/api/memories/get.php"

        private const val GET_WISHLIST_URL = "/api/wishlist/get.php"
        private const val ADD_WISHLIST_URL = "/api/wishlist/add.php"
        private const val UPDATE_WISHLIST_URL = "/api/wishlist/update.php"
        private const val REMOVE_WISHLIST_URL = "/api/wishlist/remove.php"

        private const val GET_ALBUMS_URL = "/api/albums/get.php"

        private const val GET_LOCATION_URL = "/api/location/partnerGet.php"
        private const val UPDATE_LOCATION_URL = "/api/location/selfUpdate.php"
    }

    private val client = HttpClient(Android)
    private val json = Json { ignoreUnknownKeys = true }

    // ====== Interface methods
    override suspend fun ping(): Boolean {
        val response =
            makeRequest(
                type = HttpMethod.Get,
                url = PING_URL,
            )

        return response?.status == HttpStatusCode.OK
    }

    override suspend fun getAuthenticatedUser(): AuthenticationResult {
        val response =
            makeFullAuthJsonRequest<User>(
                type = HttpMethod.Get,
                url = AUTH_URL,
            )

        val httpResponse = response.first

        val authorization =
            if (httpResponse?.status == HttpStatusCode.OK) {
                // Got response: OK
                AuthenticationResult.Authorization.AUTHORIZED
            } else if (httpResponse != null) {
                // Got response: non-OK
                AuthenticationResult.Authorization.UNAUTHORIZED
            } else {
                // No response
                AuthenticationResult.Authorization.UNKNOWN
            }

        return AuthenticationResult(authorization, response.second)
    }

    override suspend fun sendMissingYouMessage(): Boolean {
        val body =
            buildJsonObject {
                put("type", MessagingManager.MessageType.MISSING_YOU.id)
            }

        val response =
            makeAuthRequest(
                type = HttpMethod.Post,
                url = POST_MESSAGE_URL,
                body = body.toString(),
            )

        return response?.status == HttpStatusCode.OK
    }

    override suspend fun getMemories(): Map<Int, List<Memory>>? {
        return makeAuthJsonRequest(
            type = HttpMethod.Get,
            url = GET_MEMORIES_URL,
        )
    }

    override suspend fun getWishlist(): List<Wish>? {
        return makeAuthJsonRequest(
            type = HttpMethod.Get,
            url = GET_WISHLIST_URL,
        )
    }

    override suspend fun addWish(
        author: String,
        content: String,
        timestamp: Long,
    ): Boolean {
        val body =
            buildJsonObject {
                put("author", author)
                put("content", content)
                put("timestamp", timestamp)
            }

        val response =
            makeAuthRequest(
                type = HttpMethod.Post,
                url = ADD_WISHLIST_URL,
                body = body.toString(),
            )

        return response?.status == HttpStatusCode.OK
    }

    override suspend fun updateWish(wish: Wish): Boolean {
        val response =
            makeAuthRequest(
                type = HttpMethod.Post,
                url = UPDATE_WISHLIST_URL,
                body = json.encodeToString(wish),
            )

        return response?.status == HttpStatusCode.OK
    }

    override suspend fun removeWish(wish: Wish): Boolean {
        val body =
            buildJsonObject {
                put("id", wish.id)
            }

        val response =
            makeAuthRequest(
                type = HttpMethod.Post,
                url = REMOVE_WISHLIST_URL,
                body = body.toString(),
            )

        return response?.status == HttpStatusCode.OK
    }

    override suspend fun getAlbums(): List<Album>? {
        return makeAuthJsonRequest(
            type = HttpMethod.Get,
            url = GET_ALBUMS_URL,
        )
    }

    override suspend fun getPartnerLocation(cached: Boolean): UserLocation? {
        return makeAuthJsonRequest(
            type = HttpMethod.Get,
            url = GET_LOCATION_URL,
            parameters = mapOf("cached" to cached.toString()),
        )
    }

    override suspend fun updateLocation(location: Location): Boolean {
        val body =
            buildJsonObject {
                put("latitude", location.latitude)
                put("longitude", location.longitude)
                put("accuracy", location.accuracy)
                put("timestamp", location.time)
            }

        val response =
            makeAuthRequest(
                type = HttpMethod.Post,
                url = UPDATE_LOCATION_URL,
                body = body.toString(),
            )

        return response?.status == HttpStatusCode.OK
    }

    override suspend fun updateFcmToken(token: String): Boolean {
        val body =
            buildJsonObject {
                put("token", token)
                put("debug", BuildConfig.DEBUG)
            }

        val response =
            makeAuthRequest(
                type = HttpMethod.Post,
                url = POST_UPDATE_FCM_TOKEN_URL,
                body = body.toString(),
            )

        return response?.status == HttpStatusCode.OK
    }

    // ====== Private methods
    private suspend inline fun <reified T> makeAuthJsonRequest(
        type: HttpMethod,
        url: String,
        body: String? = null,
        parameters: Map<String, String>? = null,
    ): T? = makeFullAuthJsonRequest<T>(type, url, body, parameters).second

    private suspend inline fun <reified T> makeFullAuthJsonRequest(
        type: HttpMethod,
        url: String,
        body: String? = null,
        parameters: Map<String, String>? = null,
    ): Pair<HttpResponse?, T?> {
        val response = makeAuthRequest(type, url, body, parameters)

        if (response == null || response.status != HttpStatusCode.OK) {
            return Pair(response, null)
        }

        val jsonResponse =
            try {
                json.decodeFromString<T>(response.bodyAsText())
            } catch (e: Exception) {
                FlamingoLogger.d("Failed to decode json response", e)
                null
            }

        return Pair(response, jsonResponse)
    }

    private suspend fun makeAuthRequest(
        type: HttpMethod,
        url: String,
        body: String? = null,
        parameters: Map<String, String>? = null,
    ): HttpResponse? {
        val token = tokenManager.getIdToken() ?: return null

        try {
            val request =
                client.request {
                    method = type

                    url {
                        protocol = URLProtocol.HTTPS
                        host = HOST.removePrefix("https://")
                        path(url)
                    }

                    headers {
                        bearerAuth(token)
                    }

                    if (body != null) {
                        setBody(body)
                    }

                    parameters?.forEach { (key, value) ->
                        parameter(key, value)
                    }
                }

            FlamingoLogger.d(
                "Request (auth) to $url [${request.status.value}]: ${request.bodyAsText()}",
            )
            return request
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private suspend fun makeRequest(
        type: HttpMethod,
        url: String,
        body: String? = null,
        parameters: Map<String, String>? = null,
    ): HttpResponse? {
        try {
            val request =
                client.request {
                    method = type

                    url {
                        protocol = URLProtocol.HTTPS
                        host = HOST.removePrefix("https://")
                        path(url)
                    }

                    if (body != null) {
                        setBody(body)
                    }

                    parameters?.forEach { (key, value) ->
                        parameter(key, value)
                    }
                }

            FlamingoLogger.d(
                "Request (no auth) to $url [${request.status.value}]: ${request.bodyAsText()}",
            )
            return request
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}
