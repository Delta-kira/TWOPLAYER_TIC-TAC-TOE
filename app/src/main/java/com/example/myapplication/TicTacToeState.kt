package com.example.myapplication

data class TicTacToeState(
    var board: List<Char>,
    var currentPlayer: Char,
    var gameResult: String

)