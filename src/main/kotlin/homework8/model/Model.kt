package homework8.model

import homework8.controller.TurnAuthor
import homework8.controller.TurnPlace
import homework8.controller.TurnStage
import javafx.application.Platform
import javafx.scene.control.Button

open class Model(private val gameFieldSize: Int = 3) : ModelInterface {
    private var fields: List<MutableList<Char>> = List(gameFieldSize) { MutableList(gameFieldSize) { ' ' } }
    private var turnNumber = 0
    private val winningXExpression: String
    private val winning0Expression: String

    init {
        val winningXExpressionBuilder = StringBuilder()
        repeat(gameFieldSize) { winningXExpressionBuilder.append('X') }
        val winning0ExpressionBuilder = StringBuilder()
        repeat(gameFieldSize) { winning0ExpressionBuilder.append('0') }
        this.winningXExpression = winningXExpressionBuilder.toString()
        this.winning0Expression = winning0ExpressionBuilder.toString()
    }

    override fun move(
        turnPlace: TurnPlace,
        gameField: List<List<Button>>,
        turnAuthor: TurnAuthor
    ): MoveResult = makeTurn(turnPlace, gameField)

    protected fun makeTurn(
        turnPlace: TurnPlace,
        gameField: List<List<Button>>,
        turnAuthor: TurnAuthor = TurnAuthor.CLIENT
    ): MoveResult {
        fields[turnPlace.row][turnPlace.column] = if (turnNumber % 2 == 0) {
            'X'
        } else '0'
        ++turnNumber
        val turnStage = checkWin(turnNumber == gameFieldSize * gameFieldSize)
        if (turnAuthor == TurnAuthor.CLIENT) gameField[turnPlace.row][turnPlace.column].text = turnStage.sign
        else Platform.runLater { gameField[turnPlace.row][turnPlace.column].text = turnStage.sign }
        val isGameOver = turnStage.isGameOver()
        if (isGameOver) cleanField()
        return MoveResult(turnStage, isGameOver)
    }

    private fun cleanField() {
        turnNumber = 0
        fields = List(gameFieldSize) { MutableList(gameFieldSize) { ' ' } }
    }

    fun getCurrentState(): List<List<Char>> = fields

    private fun TurnStage.isGameOver(): Boolean =
        this == TurnStage.DRAW || this == TurnStage.WIN_0 || this == TurnStage.WIN_X

    private fun getRow(row: Int): String {
        var rowResult = ""
        for (x in fields.indices) {
            rowResult += fields[row][x].toString()
        }
        return rowResult
    }

    private fun getColumn(column: Int): String {
        var columnResult = ""
        for (x in fields.indices) {
            columnResult += fields[x][column].toString()
        }
        return columnResult
    }

    private fun getLeftDiagonal(): String {
        var diagonalResult = ""
        for (i in fields.indices) {
            diagonalResult += fields[i][i].toString()
        }
        return diagonalResult
    }

    private fun getRightDiagonal(): String {
        var diagonalResult = ""
        for (i in fields.indices) {
            diagonalResult += fields[i][gameFieldSize - i - 1].toString()
        }
        return diagonalResult
    }

    @Suppress("ReturnCount")
    private fun checkWin(isLastTurn: Boolean): TurnStage {

        for (i in fields.indices) {
            if (getRow(i) == winningXExpression || getColumn(i) == winningXExpression) {
                return TurnStage.WIN_X
            }
            if (getRow(i) == winning0Expression || getColumn(i) == winning0Expression) {
                return TurnStage.WIN_0
            }
        }
        return when {
            getLeftDiagonal() == winningXExpression || getRightDiagonal() == winningXExpression -> {
                TurnStage.WIN_X
            }
            getLeftDiagonal() == winning0Expression || getRightDiagonal() == winning0Expression -> {
                TurnStage.WIN_0
            }
            isLastTurn -> {
                TurnStage.DRAW
            }
            else -> if ((turnNumber - 1) % 2 == 0) TurnStage.NO_WINNER_YET_X else TurnStage.NO_WINNER_YET_0
        }
    }
}
