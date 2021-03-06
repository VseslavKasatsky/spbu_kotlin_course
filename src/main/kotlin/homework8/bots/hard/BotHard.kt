package homework8.bots.hard

import homework8.bots.BotInterface
import homework8.controller.TurnPlace
import kotlin.random.Random

class BotHard : BotInterface {
    override val botName = "Hard Bot: Egor"

    override fun makeTurn(currentGameState: List<List<Char>>): TurnPlace {
        val gameStatistic = getGameFieldStatistic(currentGameState)
        val decision = getDecision(gameStatistic)
        return when (decision.stringType) {
            LineType.ROW -> {
                val placeInLine = currentGameState[decision.lineNumber].indexOf(' ')
                TurnPlace(decision.lineNumber, placeInLine)
            }
            LineType.COLUMN -> {
                var placeInLine = 0
                for (i in currentGameState.indices) {
                    if (currentGameState[i][decision.lineNumber] == ' ') {
                        placeInLine = i
                    }
                }
                TurnPlace(placeInLine, decision.lineNumber)
            }
            LineType.LEFT_DIAGONAL -> {
                var placeInLine = 0
                for (i in currentGameState.indices) {
                    if (currentGameState[i][i] == ' ') {
                        placeInLine = i
                    }
                }
                TurnPlace(placeInLine, placeInLine)
            }
            LineType.RIGHT_DIAGONAL -> {
                var placeInLine = 0
                for (i in currentGameState.indices) {
                    if (currentGameState[i][currentGameState.size - i - 1] == ' ') {
                        placeInLine = i
                    }
                }
                TurnPlace(placeInLine, currentGameState.size - placeInLine - 1)
            }
            LineType.RANDOM -> {
                var botTurn = getBotRandomTurn(currentGameState)
                while (!isBotRandomTurnCorrect(botTurn, currentGameState)) botTurn = getBotRandomTurn(currentGameState)
                botTurn
            }
        }
    }

    private fun getDecision(gameFieldStatistic: List<LineState>): LineState {
        val defenseList = gameFieldStatistic.filter {
            it.numberOfX > 0 && it.numberOf0 == 0
        }.sortedByDescending { it.numberOfX }

        val attackList = gameFieldStatistic.filter {
            it.numberOfX == 0
        }.sortedByDescending { it.numberOf0 }

        return when {
            defenseList.isEmpty() && attackList.isEmpty() -> LineState(LineType.RANDOM, 0, 0, 0)
            attackList.isNotEmpty() && attackList[0].numberOf0 == gameFieldStatistic.size - 1 -> attackList[0]
            defenseList.isEmpty() -> attackList[0]
            else -> defenseList[0]
        }
    }

    private data class LineState(val stringType: LineType, val lineNumber: Int, val numberOfX: Int, val numberOf0: Int)

    private fun getGameFieldStatistic(fields: List<List<Char>>): List<LineState> {
        val gameStatistic: MutableList<LineState> = mutableListOf()
        gameStatistic.add(
            countXAnd0(LineType.LEFT_DIAGONAL, 0, getLeftDiagonal(fields))
        )
        gameStatistic.add(
            countXAnd0(LineType.RIGHT_DIAGONAL, 0, getRightDiagonal(fields))
        )
        for (i in fields.indices) {
            val row = getRow(i, fields)
            val column = getColumn(i, fields)
            gameStatistic.add(countXAnd0(LineType.ROW, i, row))
            gameStatistic.add(countXAnd0(LineType.COLUMN, i, column))
        }
        return gameStatistic
    }

    private fun countXAnd0(stringType: LineType, stringNumber: Int, string: String): LineState {
        var numberOfX = 0
        var numberOf0 = 0
        string.forEach { char: Char ->
            if (char == 'X') ++numberOfX
            else if (char == '0') ++numberOf0
        }
        return LineState(stringType, stringNumber, numberOfX, numberOf0)
    }

    private fun getRow(row: Int, fields: List<List<Char>>): String {
        var rowResult = ""
        for (x in fields.indices) {
            rowResult += fields[row][x].toString()
        }
        return rowResult
    }

    private fun getColumn(column: Int, fields: List<List<Char>>): String {
        var columnResult = ""
        for (x in fields.indices) {
            columnResult += fields[x][column].toString()
        }
        return columnResult
    }

    private fun getLeftDiagonal(fields: List<List<Char>>): String {
        var diagonalResult = ""
        for (i in fields.indices) {
            diagonalResult += fields[i][i].toString()
        }
        return diagonalResult
    }

    private fun getRightDiagonal(fields: List<List<Char>>): String {
        var diagonalResult = ""
        for (i in fields.indices) {
            diagonalResult += fields[i][fields.size - i - 1].toString()
        }
        return diagonalResult
    }

    private fun getBotRandomTurn(currentGameState: List<List<Char>>) = TurnPlace(
        Random.nextInt(0, currentGameState.size),
        Random.nextInt(0, currentGameState.size)
    )

    private fun isBotRandomTurnCorrect(botTurn: TurnPlace, currentGameState: List<List<Char>>): Boolean =
        currentGameState[botTurn.row][botTurn.column] == ' '
}
