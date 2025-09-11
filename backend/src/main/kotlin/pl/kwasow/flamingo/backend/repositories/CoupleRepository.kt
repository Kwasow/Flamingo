package pl.kwasow.flamingo.backend.repositories

import org.springframework.data.jpa.repository.JpaRepository
import pl.kwasow.flamingo.types.user.Couple

interface CoupleRepository : JpaRepository<Couple, Int>
