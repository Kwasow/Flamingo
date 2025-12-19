package pl.kwasow.flamingo.backend.endpoints.memories

import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import pl.kwasow.flamingo.backend.services.MemoryService
import pl.kwasow.flamingo.types.memories.MemoriesAddResponse
import pl.kwasow.flamingo.types.memories.MemoriesDeleteRequest
import pl.kwasow.flamingo.types.memories.MemoriesGetResponse
import pl.kwasow.flamingo.types.memories.MemoriesUpdateResponse
import pl.kwasow.flamingo.types.memories.Memory
import pl.kwasow.flamingo.types.memories.MemoryDto
import pl.kwasow.flamingo.types.user.User

@RestController
class MemoriesController(
    private val memoryService: MemoryService,
) {
    // ====== Endpoints
    @GetMapping("/memories/get")
    fun getMemories(
        @AuthenticationPrincipal user: User,
    ): MemoriesGetResponse {
        val memoryDtoMap = memoryService.getMemoriesForUserByYear(user)
        val memories =
            memoryDtoMap.mapValues { (_, value) ->
                value.map { Memory(it) }
            }

        return memories
    }

    @PostMapping("/memories/add")
    fun addMemory(
        @AuthenticationPrincipal user: User,
        @RequestBody memory: Memory,
    ): MemoriesAddResponse {
        if (memoryService.verifyMemoryForAdding(memory.id)) {
            val memoryDto = memoryService.saveMemory(MemoryDto(memory, user))

            return Memory(memoryDto)
        }

        throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }

    @PostMapping("/memories/update")
    fun updateMemory(
        @AuthenticationPrincipal user: User,
        @RequestBody memory: Memory,
    ): MemoriesUpdateResponse {
        if (memoryService.verifyMemoryForEditing(user, memory.id)) {
            val memoryDto = memoryService.saveMemory(MemoryDto(memory, user))

            return Memory(memoryDto)
        }

        throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }

    @DeleteMapping("/memories/delete")
    fun deleteMemory(
        @AuthenticationPrincipal user: User,
        @RequestBody deleteRequest: MemoriesDeleteRequest,
    ) {
        if (memoryService.verifyMemoryForEditing(user, deleteRequest.id)) {
            memoryService.deleteMemory(deleteRequest.id)

            return
        }

        throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }
}
