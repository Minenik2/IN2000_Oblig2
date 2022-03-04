package com.example.nikitafe_oblig2

import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class DataSource(
    var dataSourceAlpacaParty: MutableList<AlpacaPartyItem>
) {
    var updatedParty: Boolean = false
    // lag grensesnitt som returnerer en list kravet
    fun returnList(): MutableList<AlpacaPartyItem> {
        while (true) {
            if (updatedParty) {
                return dataSourceAlpacaParty
            }
        }
    }

    private suspend fun taskMakeAPI() {
        withContext(Dispatchers.IO) {
            getMyData()
        }
    }

    fun getMyData() {
        updatedParty = false
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getData()

        retrofitData.enqueue(object : Callback<AlpacaParty?> {
            override fun onResponse(call: Call<AlpacaParty?>, response: Response<AlpacaParty?>) {
                val responseBody = response.body()!!
                println(responseBody.returnList())
                getMyDataDistrict(responseBody.returnList())
                dataSourceAlpacaParty = responseBody.returnList()
            }

            override fun onFailure(call: Call<AlpacaParty?>, t: Throwable) {
                //Toast.makeText(applicationContext, "ops system not loaded", Toast.LENGTH_SHORT).show()
                println("Failed to call <DataSource>: $t")
            }
        })
    }

    fun getMyDataDistrict(myAlpacaParty: MutableList<AlpacaPartyItem>) {
        updatedParty = false
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getDataDistrict1("district1.json")

        retrofitData.enqueue(object : Callback<DistrictData?> {
            override fun onResponse(call: Call<DistrictData?>, response: Response<DistrictData?>) {
                val responseBody = response.body()!!
                println(responseBody)
                districtCalculateData(myAlpacaParty, responseBody)
            }

            override fun onFailure(call: Call<DistrictData?>, t: Throwable) {
                //Toast.makeText(applicationContext, "ops system not loaded", Toast.LENGTH_SHORT).show()
                println("Failed to call <DataSource>: $t")
            }
        })
    }

    private fun pasteDataInRecycler(dataList: MutableList<AlpacaPartyItem>) {
        println("JEG KJØRTE I DATALISTUPDATE")
        dataSourceAlpacaParty = dataList
        updatedParty = true
    }

    // beregner dataen slik at vi fordeller stemmer til alle partier og finner %
    private fun districtCalculateData(dataPartyList: MutableList<AlpacaPartyItem>, dataDistrictList: DistrictData) {
        val votes = setInnStemmer(dataDistrictList, dataPartyList)
        val totalVotes = votes.sum()

        println("dirstrikt 1 distrikdata: " + {dataDistrictList})
        println("dirstrikt 1 votes: " + {votes})
        println("dirstrikt 1 totalvotes:" + {totalVotes})

        settInn(dataPartyList, votes, totalVotes)
    }

    private fun setInnStemmer(districtList : DistrictData, parties: MutableList<AlpacaPartyItem>): MutableList<Int> {
        var votes: Int
        for (party in parties) {
            votes = 0
            for (vote in districtList) {
                if (vote.id == party.id) {
                    votes++
                    party.votes = votes
                }
            }
        }

        val voteList = mutableListOf<Int>()

        for (party in parties) {
            voteList.add(party.votes)
        }
        return voteList
    }

    // fordeler stemmer til alle partier ut visuelt
    private fun settInn(alpacapartyListe : MutableList<AlpacaPartyItem>, s : MutableList<Int>, t : Int){

        for((pos, party) in alpacapartyListe.withIndex()){
            var prosent = (s[pos].toDouble() / t) * 100
            prosent = String.format("%.2f", prosent).toDouble()
            party.voteText = s[pos].toString() + " stemmer, " + prosent.toString() + "% av totalen"
        }

        pasteDataInRecycler(alpacapartyListe)
    }

    fun getMyDataDistrict2(myAlpacaParty: MutableList<AlpacaPartyItem>) {
        updatedParty = false
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getDataDistrict1("district2.json")

        retrofitData.enqueue(object : Callback<DistrictData?> {
            override fun onResponse(call: Call<DistrictData?>, response: Response<DistrictData?>) {
                val responseBody = response.body()!!
                println(responseBody)
                districtCalculateData(myAlpacaParty, responseBody)
            }

            override fun onFailure(call: Call<DistrictData?>, t: Throwable) {
                //Toast.makeText(applicationContext, "ops system not loaded", Toast.LENGTH_SHORT).show()
                println("Failed to call <DataSource>: $t")
            }
        })
    }

    // function to read district3.xml file
    fun getMyDataDistrict3(myDataSource: MutableList<AlpacaPartyItem>) {
        updatedParty = false
        try{
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    val xml = Fuel.get("https://www.uio.no/studier/emner/matnat/ifi/IN2000/v22/obligatoriske-oppgaver/district3.xml").awaitString()
                    val inputStream = xml.byteInputStream()
                    val listOfParties = XmlParser().parse(inputStream) //parser informasjonen og faar tilbake liste med partier og hvor mange stemmer de fikk
                    var stemmer = mutableListOf<Int>()
                    var stemmetall : Int
                    for(party in dataSourceAlpacaParty){
                        for(k in listOfParties){ //fordeler stemmer
                            if(k.id == party.id){
                                party.votes = k.votes!!
                                k.votes?.let { stemmer.add(it) }
                            }
                        }
                    }
                    val totalmengdestemmer = stemmer.sum()

                    settInn(dataSourceAlpacaParty, stemmer, totalmengdestemmer)
                }
            }
        } catch(exception: Exception) {
            println("A network request exception was thrown: ${exception.message}")
        }
    }
}
