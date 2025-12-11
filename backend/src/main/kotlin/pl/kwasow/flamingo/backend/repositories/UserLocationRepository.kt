package pl.kwasow.flamingo.backend.repositories

import org.springframework.data.jpa.repository.JpaRepository
import pl.kwasow.flamingo.types.location.UserLocationDto

interface UserLocationRepository : JpaRepository<UserLocationDto, Int>
