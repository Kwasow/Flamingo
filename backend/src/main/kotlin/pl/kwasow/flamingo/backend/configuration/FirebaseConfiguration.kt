package pl.kwasow.flamingo.backend.configuration

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FirebaseConfiguration {
    // ====== Public methods
    @Bean
    fun firebaseApp(): FirebaseApp {
        val serviceAccount =
            System
                .getenv("FIREBASE_CONFIG")
                .substring(1)
                .dropLast(1)
                .byteInputStream()

        println(serviceAccount)

        val options =
            FirebaseOptions
                .builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build()

        return FirebaseApp.initializeApp(options)
    }

    @Bean
    fun firebaseAuth(firebaseApp: FirebaseApp): FirebaseAuth = FirebaseAuth.getInstance(firebaseApp)
}
