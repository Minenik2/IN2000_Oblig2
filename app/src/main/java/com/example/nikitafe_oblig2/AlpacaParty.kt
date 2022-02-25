package com.example.nikitafe_oblig2

data class AlpacaParty(
    val color: String,
    val id: String,
    val img: String,
    val leader: String,
    val name: String
) {
    // legger til stemmer for task 4
    var votes: Int = 0
    var voteTextList: MutableList<String> = arrayListOf()
    var voteText: String = ""

}