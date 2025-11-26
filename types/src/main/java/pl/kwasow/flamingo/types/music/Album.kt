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

@Entity
@Serializable
@Table(name = "Albums")
data class Album(
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
    val tracks: MutableList<AudioTrack>,
) {
    // ====== Constructors
    constructor() : this(-1, "", "", "", "", -1, mutableListOf())
}
