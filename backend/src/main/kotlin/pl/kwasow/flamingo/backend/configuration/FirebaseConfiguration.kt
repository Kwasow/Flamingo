package pl.kwasow.flamingo.backend.configuration

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FirebaseConfiguration {

    @Bean
    fun firebaseApp(): FirebaseApp {
        return FirebaseApp.initializeApp()
    }

    @Bean
    fun firebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}
