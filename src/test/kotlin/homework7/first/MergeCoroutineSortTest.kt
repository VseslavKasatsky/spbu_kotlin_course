package homework7.first

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class MergeCoroutineSortTest {

    companion object {
        @JvmStatic
        fun inputData(): List<Arguments> = listOf(
            Arguments.of(
                intArrayOf(1, 4, 5, 7, 10), intArrayOf(5, 10, 1, 4, 7), 10
            ),
            Arguments.of(
                intArrayOf(1, 4, 5, 7, 10), intArrayOf(5, 10, 1, 4, 7), 1
            ),
            Arguments.of(
                intArrayOf(0, 1, 3, 15, 25, 29, 252, 1551), intArrayOf(3, 15, 25, 1551, 252, 0, 1, 29), 2
            ),
            Arguments.of(
                intArrayOf(4, 17, 22, 100, 1598, 9151, 15000), intArrayOf(100, 9151, 15000, 4, 17, 1598, 22), 15
            )
        )
    }

    @MethodSource("inputData")
    @ParameterizedTest(name = "test{index}, {1}")
    fun mergeSortingCoroutineTest(expectedArray: IntArray, arrayToSort: IntArray, threadsNumber: Int) {
        MergeSorterCoroutine().sort(arrayToSort, threadsNumber)
        assertEquals(expectedArray.toList(), arrayToSort.toList())
    }
}
