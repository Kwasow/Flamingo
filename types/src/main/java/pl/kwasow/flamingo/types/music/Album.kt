package pl.kwasow.flamingo.types.music

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import kotlinx.serialization.Serializable

@Serializable
data class Album(
    val id: Int,
    val uuid: String,
    val title: String,
    val artist: String,
    val coverName: String,
    val tracks: List<AudioTrack>,
) {
    constructor(dto: AlbumDto) : this(
        id = dto.id,
        uuid = dto.uuid,
        title = dto.title,
        artist = dto.artist,
        coverName = dto.coverName,
        tracks = dto.tracks.map { AudioTrack(it) },
    )
}

@Entity
@Table(name = "Albums")
data class AlbumDto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = -1,
    @Column("uuid")
    val uuid: String,
    @Column("title")
    val title: String,
    @Column("artist")
    val artist: String,
    @Column("cover_name")
    val coverName: String,
    @Column("couple_id")
    val coupleId: Int,
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "album_uuid", referencedColumnName = "uuid")
    val tracks: MutableList<AudioTrackDto>,
) {
    // ====== Constructors
    constructor() : this(-1, "", "", "", "", -1, mutableListOf())
}
