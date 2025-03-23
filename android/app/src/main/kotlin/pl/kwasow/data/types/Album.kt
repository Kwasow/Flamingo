package pl.kwasow.data.types

import kotlinx.serialization.Serializable

@Serializable
data class Album(
    val id: Int,
    val uuid: String,
    val title: String,
    val artist: String,
    val coverName: String,
    val tracks: List<AudioTrack>,
)
