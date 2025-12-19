package pl.kwasow.managers

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import pl.kwasow.BuildConfig
import pl.kwasow.flamingo.types.auth.AuthResponse
import pl.kwasow.flamingo.types.auth.AuthenticationResult
import pl.kwasow.flamingo.types.auth.Authorization
import pl.kwasow.flamingo.types.location.LocationGetResponse
import pl.kwasow.flamingo.types.location.UserLocation
import pl.kwasow.flamingo.types.memories.MemoriesGetResponse
import pl.kwasow.flamingo.types.memories.Memory
import pl.kwasow.flamingo.types.messaging.FcmUpdateTokenRequest
import pl.kwasow.flamingo.types.messaging.MessageType
import pl.kwasow.flamingo.types.music.Album
import pl.kwasow.flamingo.types.music.AlbumsGetResponse
import pl.kwasow.flamingo.types.wishlist.Wish
import pl.kwasow.flamingo.types.wishlist.WishlistDeleteRequest
import pl.kwasow.flamingo.types.wishlist.WishlistGetResponse
import pl.kwasow.utils.FlamingoLogger

class RequestManagerImpl(
    private val tokenManager: TokenManager,
) : RequestManager {
    // ====== Fields
    companion object {
        private const val HOST = BuildConfig.BASE_URL

        private const val PING_URL = "/api/ping"
        private const val AUTH_URL = "/api/auth"

        private const val POST_MESSAGE_URL = "/api/messaging/send"
        private const val POST_UPDATE_FCM_TOKEN_URL = "/api/messaging/updateFcmToken"

        private const val GET_MEMORIES_URL = "/api/memories/get"

        private const val GET_WISHLIST_URL = "/api/wishlist/get"
        private const val ADD_WISHLIST_URL = "/api/wishlist/add"
        private const val UPDATE_WISHLIST_URL = "/api/wishlist/update"
        private const val REMOVE_WISHLIST_URL = "/api/wishlist/delete"

        private const val GET_ALBUMS_URL = "/api/albums/get"

          private const val GET_LOCATION_URL = "/api/location/get/partner"
        private const val UPDATE_LOCATION_URL = "/api/location/get/self"
    }

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

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
            makeFullAuthJsonRequest<AuthResponse>(
                type = HttpMethod.Get,
                url = AUTH_URL,
            )

        val httpResponse = response.first

        val authorization =
            if (httpResponse?.status == HttpStatusCode.OK) {
                // Got response: OK
                Authorization.AUTHORIZED
            } else if (httpResponse != null) {
                // Got response: non-OK
                Authorization.UNAUTHORIZED
            } else {
                // No response
                Authorization.UNKNOWN
            }

        return AuthenticationResult(authorization, response.second)
    }

    override suspend fun sendMissingYouMessage(): Boolean {
        val response =
            makeAuthRequest(
                type = HttpMethod.Post,
                url = POST_MESSAGE_URL,
                body = MessageType.MISSING_YOU,
            )

        return response?.status == HttpStatusCode.OK
    }

    override suspend fun getMemories(): Map<Int, List<Memory>>? {
        return makeAuthJsonRequest<MemoriesGetResponse>(
            type = HttpMethod.Get,
            url = GET_MEMORIES_URL,
        )
    }

    override suspend fun getWishlist(): List<Wish>? {
        return makeAuthJsonRequest<WishlistGetResponse>(
            type = HttpMethod.Get,
            url = GET_WISHLIST_URL,
        )
    }

    override suspend fun addWish(wish: Wish): Boolean {
        val response =
            makeAuthRequest(
                type = HttpMethod.Post,
                url = ADD_WISHLIST_URL,
                body = wish,
            )

        return response?.status == HttpStatusCode.OK
    }

    override suspend fun updateWish(wish: Wish): Boolean {
        val response =
            makeAuthRequest(
                type = HttpMethod.Post,
                url = UPDATE_WISHLIST_URL,
                body = wish,
            )

        return response?.status == HttpStatusCode.OK
    }

    override suspend fun removeWish(id: Int): Boolean {
        val body = WishlistDeleteRequest(id)

        val response =
            makeAuthRequest(
                type = HttpMethod.Delete,
                url = REMOVE_WISHLIST_URL,
                body = body,
            )

        return response?.status == HttpStatusCode.OK
    }

    override suspend fun getAlbums(): List<Album>? {
        return makeAuthJsonRequest<AlbumsGetResponse>(
            type = HttpMethod.Get,
            url = GET_ALBUMS_URL,
        )
    }

    override suspend fun getPartnerLocation(): UserLocation? {
        return makeAuthJsonRequest<LocationGetResponse>(
            type = HttpMethod.Get,
            url = GET_LOCATION_URL,
        )
    }

    override suspend fun updateLocation(location: UserLocation): Boolean {
        val response =
            makeAuthRequest(
                type = HttpMethod.Post,
                url = UPDATE_LOCATION_URL,
                body = location,
            )

        return response?.status == HttpStatusCode.OK
    }

    override suspend fun updateFcmToken(token: String): Boolean {
        val body = FcmUpdateTokenRequest(token, BuildConfig.DEBUG)

        val response =
            makeAuthRequest(
                type = HttpMethod.Post,
                url = POST_UPDATE_FCM_TOKEN_URL,
                body = body,
            )

        return response?.status == HttpStatusCode.OK
    }

    // ====== Private methods
    private suspend inline fun <reified T> makeAuthJsonRequest(
        type: HttpMethod,
        url: String,
        body: Any? = null,
        parameters: Map<String, String>? = null,
    ): T? = makeFullAuthJsonRequest<T>(type, url, body, parameters).second

    private suspend inline fun <reified T> makeFullAuthJsonRequest(
        type: HttpMethod,
        url: String,
        body: Any? = null,
        parameters: Map<String, String>? = null,
    ): Pair<HttpResponse?, T?> {
        val response = makeAuthRequest(type, url, body, parameters)

        if (response == null || response.status != HttpStatusCode.OK) {
            return Pair(response, null)
        }

        val jsonResponse =
            try {
                response.body<T>()
            } catch (e: Exception) {
                FlamingoLogger.d("Failed to decode json response", e)
                null
            }

        return Pair(response, jsonResponse)
    }

    private suspend fun makeAuthRequest(
        type: HttpMethod,
        url: String,
        body: Any? = null,
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
                        contentType(ContentType.Application.Json)
                    }

                    parameters?.forEach { (key, value) ->
                        parameter(key, value)
                    }
                }

            FlamingoLogger.d(
                "Request (auth) to ${request.request.url} [${request.status.value}]: ${request.bodyAsText()}",
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
        body: Any? = null,
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
                        contentType(ContentType.Application.Json)
                    }

                    parameters?.forEach { (key, value) ->
                        parameter(key, value)
                    }
                }

            FlamingoLogger.d(
                "Request (no auth) to ${request.request.url} [${request.status.value}]: ${request.bodyAsText()}",
            )
            return request
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}
