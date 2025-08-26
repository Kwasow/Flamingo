package pl.kwasow.flamingo.backend.endpoints.memories

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import pl.kwasow.flamingo.types.memories.Memory

@RestController
class MemoriesController {
    @GetMapping("/memories/get")
    fun getMemories(): ResponseEntity<Map<Int, List<Memory>>> {

    }

    @PostMapping("/memories/add")
    fun addMemory(): ResponseEntity<*> {
        return ResponseEntity.ok().build<Any>()
    }
}
