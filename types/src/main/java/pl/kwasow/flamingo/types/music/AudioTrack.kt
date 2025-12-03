package pl.kwasow.flamingo.types.music

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import kotlinx.serialization.Serializable

@Serializable
data class AudioTrack(
    val id: Int,
    val title: String,
    val comment: String? = null,
    val resourceName: String,
    val albumUuid: String,
) {
    // ====== Constructors
    constructor(dto: AudioTrackDto) : this(
        id = dto.id,
        title = dto.title,
        comment = dto.comment,
        resourceName = dto.resourceName,
        albumUuid = dto.albumUuid,
    )
}

@Entity
@Table(name = "Tracks")
data class AudioTrackDto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = -1,
    @Column("title")
    val title: String,
    @Column("comment")
    val comment: String? = null,
    @Column("resource_name")
    val resourceName: String,
    @Column("album_uuid")
    val albumUuid: String,
) {
    // ====== Constructors
    constructor() : this(-1, "", null, "", "")
}
