package io.vendator.dav.calculate.busineslogic

import java.lang.Math.pow
import kotlin.random.Random

class ProblemGenerator(private val difficultyLevel: Int) {

    fun generateRandomProblem(): Problem {
        return Problem(
            difficultyLevel,
            Array(Random.nextInt(2, 3)) {
                getNthDigitRandomNumber()
            },
            Random.nextInt(Problem.OP_ADD, Problem.OP_DIV+1)
        )
    }

    /*
       1 -> 1 digit (1, 1*10)
       2 -> 2 digit (10, 1 * 10 * 10) (10 - 100)
       3 -> 3 digit (100, 1 * 10 * 10 * 10) (100 - 1000)
       4 -> 4 digit (1000, 1 * 10 * 10 * 10 * 10) (1000, 10,000)
       5 -> 5 digit (10,000, 1 * 10 * 10 * 10 *  10 * 10) (10,000, 10,000,0)
     */
    private fun getNthDigitRandomNumber(): Int {
        return Random.nextInt(
            pow(10.0, (difficultyLevel - 1).toDouble()).toInt(),
            pow(10.0, difficultyLevel.toDouble()).toInt()
        )
    }



}