package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlin.random.Random
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.animateLottieCompositionAsState


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                TicTacToeApp()
            }
        }
    }
}
@Composable
fun TicTacToeApp() {
    var gameState by remember { mutableStateOf(TicTacToeState(List(9) { ' ' }, 'X', "Ongoing")) }
    var player1Input by remember { mutableStateOf(TextFieldValue()) }
    var player2Input by remember { mutableStateOf(TextFieldValue()) }
    var player1Name by remember { mutableStateOf("Player 1") }
    var player2Name by remember { mutableStateOf("Player 2") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Tic Tac Toe", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Current Player: ${if (gameState.currentPlayer == 'X') player1Name else player2Name}",
                fontSize = 16.sp
            )
        }

        TicTacToeGame(gameState) { index ->
            if (gameState.board[index] == ' ' && gameState.gameResult == "Ongoing") {
                gameState = gameState.copy(
                    board = gameState.board.toMutableList().apply { set(index, gameState.currentPlayer) }
                )
                if (checkWin(gameState.board, gameState.currentPlayer)) {
                    gameState = gameState.copy(gameResult = "${gameState.currentPlayer} Wins")
                } else if (gameState.board.all { it != ' ' }) {
                    gameState = gameState.copy(gameResult = "Draw")
                } else {
                    gameState = gameState.copy(
                        currentPlayer = if (gameState.currentPlayer == 'X') 'O' else 'X'
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Game Result: ${gameState.gameResult}", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = player1Input,
                onValueChange = {
                    player1Input = it
                },
                textStyle = TextStyle(fontSize = 16.sp),
                placeholder = { Text("Enter Player 1 Name") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = player2Input,
                onValueChange = {
                    player2Input = it
                },
                textStyle = TextStyle(fontSize = 16.sp),
                placeholder = { Text("Enter Player 2 Name") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    gameState = TicTacToeState(List(9) { ' ' }, 'X', "Ongoing")
                    player1Name = player1Input.text
                    player2Name = player2Input.text
                }
            ) {
                Text("Reset Game")
            }
        }

        if (gameState.gameResult != "Ongoing") {
            Text(
                text = "Player ${if (gameState.gameResult == "X Wins") player1Name else player2Name} Wins!",
                fontSize = 20.sp
            )
        }
    }
}

fun checkWin(board: List<Char>, player: Char): Boolean {
    val winningCombinations = listOf(
        listOf(0, 1, 2),
        listOf(3, 4, 5),
        listOf(6, 7, 8),
        listOf(0, 3, 6),
        listOf(1, 4, 7),
        listOf(2, 5, 8),
        listOf(0, 4, 8),
        listOf(2, 4, 6)
    )

    for (combination in winningCombinations) {
        if (combination.all { board[it] == player }) {
            return true
        }
    }
    return false
}
@Composable
fun TicTacToeGame(state: TicTacToeState, onCellClick: (Int) -> Unit) {
    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
        items(state.board.size) { index ->
            TicTacToeCell(state.board[index]) {
                onCellClick(index)
            }
        }
    }
}
@Composable
fun TicTacToeCell(value: Char, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .border(1.dp, Color.Black)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = value.toString(), fontSize = 24.sp)
    }
}
