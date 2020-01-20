package engineer.echo.logic

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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

    @Test
    fun maxProfit1() {
        val arr = intArrayOf(2, 7, 12, 1, 10, 5, 3, 6, 4)
        val max1 = Simple.maxProfit1(arr)
        val max2 = Simple.maxProfit2(arr)
        println("max1 = $max1 max2 = $max2")
    }

    @Test
    fun maxProfit2() {
        val arr = intArrayOf(1, 2, 3, 4, 5)
        println("max = ${Middle.maxProfit(arr)}")
    }

    @Test
    fun testRotateArray() {
        val arr = intArrayOf(1, 2, 3, 4, 5, 6, 7)
        val arr1 = Simple.rotateArray(arr, 3)
        println("#1 = ${arr1?.contentToString()}")
    }

    @Test
    fun testDuplicate() {
        val arr = intArrayOf(1, 2, 3, 1)
        val th1 = Simple.duplicate(arr)
        val th2 = Simple.duplicateArr(arr)
        println("#1 = $th1")
        assertTrue("#2", th2)
    }
}
