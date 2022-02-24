package com.example.nikitafe_oblig2

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {

    // Create a LiveData med alpacaparty
    val currentLiveData: MutableLiveData<AlpacaParty> by lazy {
        MutableLiveData<AlpacaParty>()
    }

    // livedata for andre distrikter
    private val currentDistrictLiveData: MutableLiveData<DistrictData> by lazy {
        MutableLiveData<DistrictData>()
    }

    // livedata for datasource
    val currentDataSourceLiveData: MutableLiveData<DataSource> by lazy {
        MutableLiveData<DataSource>()
    }

    // metode fra mainacitivtyveiwmodel som henter listen med alpakka objekter fra datasource objektet vårt
    fun getAlpacaParty(currentDataSource: DataSource) {
        for (alpacaParty in currentDataSource.returnList()) {
            currentLiveData.value = alpacaParty
        }
    }

    // metode fra mainacitivtyveiwmodel som henter listen med alpakka objekter fra datasource objektet vårt
    fun getDistrictData(currentDistrictData: DistrictData) {
            currentDistrictLiveData.value = currentDistrictData
    }


}