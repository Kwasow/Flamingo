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
import pl.kwasow.flamingo.types.memories.MemoriesAddResponse
import pl.kwasow.flamingo.types.memories.MemoriesDeleteRequest
import pl.kwasow.flamingo.types.memories.MemoriesGetResponse
import pl.kwasow.flamingo.types.memories.MemoriesUpdateResponse
import pl.kwasow.flamingo.types.memories.Memory
import pl.kwasow.flamingo.types.user.User

@RestController
class MemoriesController(
    private val memoryService: MemoryService,
) {
    // ====== Endpoints
    @GetMapping("/memories/get")
    fun getMemories(
        @AuthenticationPrincipal user: User,
    ): ResponseEntity<MemoriesGetResponse> =
        ResponseEntity.ok(memoryService.getMemoriesForUserByYear(user))

    @PostMapping("/memories/add")
    fun addMemory(
        @AuthenticationPrincipal user: User,
        @RequestBody memory: Memory,
    ): ResponseEntity<MemoriesAddResponse> {
        val incomingMemory = memory.copy(id = -1)
        if (!verifyAuthor(user, incomingMemory)) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .build()
        }

        val newMemory = memoryService.saveMemory(incomingMemory)

        return ResponseEntity
            .ok()
            .body(newMemory)
    }

    @PostMapping("/memories/update")
    fun updateMemory(
        @AuthenticationPrincipal user: User,
        @RequestBody memory: Memory,
    ): ResponseEntity<MemoriesUpdateResponse> {
        if (!verifyAuthor(user, memory)) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .build()
        }

        val errorResponse = verifyMemory(user, memory.id)

        if (errorResponse != null) {
            return errorResponse
        }

        val editedMemory = memoryService.saveMemory(memory)

        return ResponseEntity
            .ok()
            .body(editedMemory)
    }

    @DeleteMapping("/memories/delete")
    fun deleteMemory(
        @AuthenticationPrincipal user: User,
        @RequestBody deleteRequest: MemoriesDeleteRequest,
    ): ResponseEntity<*> {
        val errorResponse = verifyMemory(user, deleteRequest.id)

        if (errorResponse != null) {
            return errorResponse
        }

        memoryService.deleteMemory(deleteRequest.id)

        return ResponseEntity
            .ok()
            .build<Any>()
    }

    // ====== Private methods
    private fun verifyAuthor(
        user: User,
        coupleId: Int,
    ): Boolean = user.couple.id == coupleId

    private fun verifyAuthor(
        user: User,
        memory: Memory,
    ): Boolean = verifyAuthor(user, memory.coupleId)

    private fun verifyMemory(
        user: User,
        id: Int?,
    ): ResponseEntity<Memory>? {
        // Check if ID is set
        val incomingId =
            id ?: return ResponseEntity
                .badRequest()
                .build()

        // Check if the user is authorized to modify this memory
        val savedCoupleId =
            memoryService.findOwner(incomingId)
                ?: return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build()

        if (!verifyAuthor(user, savedCoupleId)) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .build()
        }

        return null
    }
}
