package engineer.echo.logic

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun removeDuplicates() {
        val arr = intArrayOf(1, 1, 1, 2, 2, 3, 3, 5, 6)
        val len = Simple.removeDuplicates(arr)
        // 执行过程
        // j = 1 [1, 1, 1, 2, 2, 3, 3, 5, 6]
        // j = 2 [1, 1, 1, 2, 2, 3, 3, 5, 6]
        // j = 3 [1, 2, 1, 2, 2, 3, 3, 5, 6]
        // j = 4 [1, 2, 1, 2, 2, 3, 3, 5, 6]
        // j = 5 [1, 2, 3, 2, 2, 3, 3, 5, 6]
        // j = 6 [1, 2, 3, 5, 2, 3, 3, 5, 6]
        // j = 7 [1, 2, 3, 5, 6, 3, 3, 5, 6]
        println("len = $len")
        println("arr = ${arr.contentToString()}")
        // 执行结果
        // len = 5
        // arr = [1, 2, 3, 5, 6, 3, 3, 5, 6]
    }
}