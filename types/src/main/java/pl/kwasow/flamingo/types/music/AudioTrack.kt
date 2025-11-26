package pl.kwasow.flamingo.types.music

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import kotlinx.serialization.Serializable

@Entity
@Serializable
@Table(name = "Tracks")
class AudioTrack(
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
