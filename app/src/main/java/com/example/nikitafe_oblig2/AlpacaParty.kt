package com.example.nikitafe_oblig2

data class AlpacaParty(
    val parties: MutableList<AlpacaPartyItem>
) {
    // lag grensesnitt som returnerer en list kravet
    fun returnList(): MutableList<AlpacaPartyItem> {
        return parties
    }
}


