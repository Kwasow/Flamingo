package pl.kwasow.managers

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class TokenManagerImpl : TokenManager {
    // ====== Fields
    private val firebaseAuth = Firebase.auth

    // ====== Interface methods
    override suspend fun getIdToken(): String? =
        firebaseAuth.currentUser?.getIdToken(
            false,
        )?.await()?.token
}
