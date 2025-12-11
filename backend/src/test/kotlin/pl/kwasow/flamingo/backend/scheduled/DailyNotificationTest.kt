package pl.kwasow.flamingo.backend.scheduled

import com.google.firebase.messaging.MulticastMessage
import org.mockito.Captor
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.util.ReflectionTestUtils
import pl.kwasow.flamingo.backend.setup.BaseTest
import pl.kwasow.flamingo.types.messaging.MessageType
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
class DailyNotificationTest : BaseTest() {
    @Captor
    val argumentCaptor = argumentCaptor<MulticastMessage>()

    @Autowired
    private lateinit var dailyMemories: DailyMemories

    @Test
    fun `notification is sent to all tokens`() {
        dailyMemories.sendNotifications()

        verify(firebaseMessaging, times(1))
            .sendEachForMulticast(argumentCaptor.capture())

        val message = argumentCaptor.lastValue
        val data = ReflectionTestUtils.getField(message, "data") as Map<*, *>
        val tokens = ReflectionTestUtils.getField(message, "tokens") as List<*>

        assertEquals(3, tokens.size)
        assert(TestData.ALICE_FCM_TOKEN in tokens)
        assert(TestData.BOB_FCM_TOKEN in tokens)
        assert(TestData.MALLORY_FCM_TOKEN in tokens)
        assertEquals(MessageType.DAILY_MEMORY.id, data["type"])
    }
}
