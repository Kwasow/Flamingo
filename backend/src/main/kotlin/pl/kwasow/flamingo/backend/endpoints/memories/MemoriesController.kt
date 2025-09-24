package pl.kwasow.flamingo.backend.endpoints.memories

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pl.kwasow.flamingo.backend.services.MemoryService
import pl.kwasow.flamingo.types.memories.Memory
import pl.kwasow.flamingo.types.user.User

@RestController
class MemoriesController(
    private val memoryService: MemoryService,
) {
    // ====== Endpoints
    @GetMapping("/memories/get")
    fun getMemories(
        @AuthenticationPrincipal user: User
    ): ResponseEntity<Map<Int, List<Memory>>> {
        return ResponseEntity.ok(memoryService.getMemoriesForUserByYear(user))
    }

    @PostMapping("/memories/add")
    fun addMemory(
        @AuthenticationPrincipal user: User,
        @RequestBody memory: Memory
    ): ResponseEntity<Memory> {
        val incomingMemory = memory.copy(id = null, coupleId = user.couple.id)
        val newMemory = memoryService.saveMemory(incomingMemory)

        return ResponseEntity
            .ok()
            .body(newMemory)
    }

    @PostMapping("/memories/edit")
    fun editMemory(
        @AuthenticationPrincipal user: User,
        @RequestBody memory: Memory
    ): ResponseEntity<Memory?> {
        val incomingMemory = memory.copy(coupleId = user.couple.id)
        val errorResponse = verifyMemory(user, incomingMemory.id)

        if (errorResponse != null) {
            return errorResponse
        }

        val editedMemory = memoryService.saveMemory(incomingMemory)

        return ResponseEntity
            .ok()
            .body(editedMemory)
    }

    @DeleteMapping("/memories/delete")
    fun deleteMemory(
        @AuthenticationPrincipal user: User,
        id: Int
    ): ResponseEntity<*> {
        val errorResponse = verifyMemory(user, id)

        if (errorResponse != null) {
            return errorResponse
        }

        memoryService.deleteMemory(id)

        return ResponseEntity
            .ok()
            .build<Any>()
    }

    // ====== Private methods
    private fun verifyMemory(user: User, id: Int?): ResponseEntity<Memory?>? {
        // Check if ID is set
        val incomingId = id ?: return ResponseEntity
            .badRequest()
            .build()

        // Check if the user is authorized to edit this memory
        val savedCoupleId = memoryService.findOwner(incomingId)
            ?: return ResponseEntity
                .badRequest()
                .build()

        if (savedCoupleId != user.couple.id) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .build()
        }

        return null
    }
}
