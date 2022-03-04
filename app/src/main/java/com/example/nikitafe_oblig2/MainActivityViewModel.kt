package com.example.nikitafe_oblig2

import android.provider.ContactsContract
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel: ViewModel() {

    private var currentDataSource: DataSource = DataSource(mutableListOf())

    // Create a LiveData med alpacaparty
    private val partyListLiveData: MutableLiveData<MutableList<AlpacaPartyItem>> by lazy {
        MutableLiveData<MutableList<AlpacaPartyItem>>()
    }

    // livedata for andre distrikter
    private val currentDistrictLiveData: MutableLiveData<MutableList<DistrictData>> by lazy {
        MutableLiveData<MutableList<DistrictData>>()
    }

    // metode fra mainacitivtyveiwmodel som henter listen med alpakka objekter fra datasource objektet vårt
    fun getAlpacaParty(): MutableLiveData<MutableList<AlpacaPartyItem>> {
        return partyListLiveData
    }

    fun getConnectAlpacaParty() {
        viewModelScope.launch(Dispatchers.IO) {
            currentDataSource.getMyData().also {
                updateAlpacaParty(currentDataSource.returnList())
            }
        }
    }

    fun updateAlpacaParty(alpList: MutableList<AlpacaPartyItem>) {
        partyListLiveData.postValue(alpList)
    }

    // metode fra mainacitivtyveiwmodel som henter listen med alpakka objekter fra datasource objektet vårt
    fun getDistrictData(): MutableLiveData<MutableList<DistrictData>> {
        return currentDistrictLiveData
    }

    fun updateDistrictData(currentDistrictData: MutableList<DistrictData>) {
        println("JEG KJØRTE I Å FÅ TILBAKE DATALISTUPDATE")
        currentDistrictLiveData.postValue(currentDistrictData)
    }

    fun getMyDataDistrict1() {
        viewModelScope.launch(Dispatchers.IO) {
            currentDataSource.getMyDataDistrict(currentDataSource.returnList()).also {
                println("JEG KJØRTE I Å FÅ TILBAKE DATALISTUPDATE")
                updateAlpacaParty(currentDataSource.returnList())
            }
        }
    }

    fun getMyDataDistrict2() {
        viewModelScope.launch(Dispatchers.IO) {
            currentDataSource.getMyDataDistrict2(currentDataSource.returnList()).also {
                println("JEG KJØRTE I Å FÅ TILBAKE DATALISTUPDATE")
                updateAlpacaParty(currentDataSource.returnList())
            }
        }
    }

    fun getMyDataDistrict3() {
        viewModelScope.launch(Dispatchers.IO) {
            currentDataSource.getMyDataDistrict3(currentDataSource.returnList()).also {
                println("JEG KJØRTE I Å FÅ TILBAKE DATALISTUPDATE")
                updateAlpacaParty(currentDataSource.returnList())
            }
        }
    }

}