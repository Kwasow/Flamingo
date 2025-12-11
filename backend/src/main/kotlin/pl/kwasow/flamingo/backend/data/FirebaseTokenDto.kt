package pl.kwasow.flamingo.backend.data

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.sql.Timestamp

@Entity
@Table(name = "FirebaseTokens")
data class FirebaseTokenDto(
    // ====== Fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    @Column(name = "user_id")
    val userId: Int,
    @Column(name = "last_seen")
    val lastSeen: Timestamp,
    @Column(name = "token")
    val token: String,
    @Column(name = "debug")
    val debug: Boolean,
)
