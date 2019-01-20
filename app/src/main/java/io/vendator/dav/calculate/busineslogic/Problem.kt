package io.vendator.dav.calculate.busineslogic

data class Problem(
    val difficulty: Int,
    val numberList: Array<Int>,
    val operation: Int
) {

    val answer: Float
        get() {
            if (numberList.isEmpty()) return -1.0F
            var previous = numberList[0].toFloat()
            for (i in 1 until numberList.size) {
                println("i -> $i and Operation: ${getCharacter(operation)}")
                when (operation) {
                    OP_ADD -> previous += numberList[i]
                    OP_SUB -> previous -= numberList[i]
                    OP_DIV -> previous /= numberList[i]
                    OP_MUL -> previous *= numberList[i]
                }
            }
            return previous
        }

    val remainder: Int
        get() {
            if (numberList.isEmpty() || numberList.size > 2 || operation != OP_DIV)
                return -1
            return numberList[0] % numberList[1]
        }

    val quotient: Int
        get() {
            if (numberList.isEmpty() || numberList.size > 2 || operation != OP_DIV)
                return -1
            if (answer.toInt() == 0) return 0
            return ((numberList[0] - remainder) / (answer.toInt()))
        }


    companion object {
        const val OP_ADD = 1
        const val OP_SUB = 2
        const val OP_MUL = 3
        const val OP_DIV = 4
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Problem

        if (difficulty != other.difficulty) return false
        if (!numberList.contentEquals(other.numberList)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = difficulty
        result = 31 * result + numberList.contentHashCode()
        return result
    }

    override fun toString(): String {
        return "Problem : Difficulty Level: $difficulty" +
                " Numbers: ${numberList.asList()} and Operation: ${getCharacter(operation)}" +
                " Answer is: $answer" +
                if (operation == OP_DIV) {
                    " Remainder is: $remainder" +
                            " Quotient is: $quotient"
                } else {
                    ""
                }
    }


   fun getCharacter(operation: Int): String {
        return when (operation) {
            OP_DIV -> "/"
            OP_MUL -> "X"
            OP_ADD -> "+"
            else -> "-"
        }
    }
}