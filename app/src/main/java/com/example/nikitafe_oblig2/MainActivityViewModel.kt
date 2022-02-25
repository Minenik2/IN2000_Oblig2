package com.example.nikitafe_oblig2

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {

    // Create a LiveData med alpacaparty
    private val partyListLiveData: MutableLiveData<MutableList<AlpacaPartyItem>> by lazy {
        MutableLiveData<MutableList<AlpacaPartyItem>>()
    }

    // livedata for andre distrikter
    private val currentDistrictLiveData: MutableLiveData<MutableList<DistrictData>> by lazy {
        MutableLiveData<MutableList<DistrictData>>()
    }

    // livedata for datasource
    val currentDataSourceLiveData: MutableLiveData<DataSource> by lazy {
        MutableLiveData<DataSource>()
    }

    // metode fra mainacitivtyveiwmodel som henter listen med alpakka objekter fra datasource objektet vårt
    fun getAlpacaParty(): MutableLiveData<MutableList<AlpacaPartyItem>> {
        return partyListLiveData
    }

    fun updateAlpacaParty(alpList: MutableList<AlpacaPartyItem>) {
        partyListLiveData.postValue(alpList)
    }

    // metode fra mainacitivtyveiwmodel som henter listen med alpakka objekter fra datasource objektet vårt
    fun getDistrictData(): MutableLiveData<MutableList<DistrictData>> {
        return currentDistrictLiveData
    }

    fun updateDistrictData(currentDistrictData: MutableList<DistrictData>) {
        currentDistrictLiveData.postValue(currentDistrictData)
    }
}