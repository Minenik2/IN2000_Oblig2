package com.example.nikitafe_oblig2

data class DataSource(
    val parties: MutableList<AlpacaParty>
) {

    // lag grensesnitt som returnerer en list kravet
    fun returnList(): MutableList<AlpacaParty> {
        return parties
    }
}


