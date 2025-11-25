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
        if (memoryService.verifyMemoryForAdding(user, memory)) {
            return ResponseEntity
                .ok()
                .body(memoryService.saveMemory(memory))
        }

        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .build()
    }

    @PostMapping("/memories/update")
    fun updateMemory(
        @AuthenticationPrincipal user: User,
        @RequestBody memory: Memory,
    ): ResponseEntity<MemoriesUpdateResponse> {
        if (memoryService.verifyMemoryForEditing(user, memory)) {
            return ResponseEntity
                .ok()
                .body(memoryService.saveMemory(memory))
        }

        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .build()
    }

    @DeleteMapping("/memories/delete")
    fun deleteMemory(
        @AuthenticationPrincipal user: User,
        @RequestBody deleteRequest: MemoriesDeleteRequest,
    ): ResponseEntity<Any> {
        if (memoryService.verifyMemoryForDeletion(user, deleteRequest.id)) {
            memoryService.deleteMemory(deleteRequest.id)

            return ResponseEntity
                .ok()
                .build()
        }

        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .build()
    }
}
