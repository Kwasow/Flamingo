package pl.kwasow

import org.junit.Assert.assertEquals
import org.junit.Test
import pl.kwasow.extensions.roundUpToMultiple

class IntExtensionsTest {
    @Test
    fun testRoundUpToMultipleNeeded() {
        assertEquals(6, 4.roundUpToMultiple(3))
    }

    @Test
    fun testRoundUpToMultipleNotNeeded() {
        assertEquals(6, 6.roundUpToMultiple(3))
    }
}
