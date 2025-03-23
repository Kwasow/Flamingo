package pl.kwasow.data.store

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import pl.kwasow.AppState
import pl.kwasow.appState
import java.io.InputStream
import java.io.OutputStream

object AppStateSerializer : Serializer<AppState> {
    override val defaultValue: AppState =
        appState {
            lastFcmTokenSync = 0
        }

    override suspend fun readFrom(input: InputStream): AppState {
        try {
            return AppState.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: AppState,
        output: OutputStream,
    ) = t.writeTo(output)
}

val Context.appStateDataStore: DataStore<AppState> by dataStore(
    fileName = "app_state.pb",
    serializer = AppStateSerializer,
)
