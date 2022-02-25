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

class DataSource {
    private var dataSourceAlpacaParty: AlpacaParty = AlpacaParty(mutableListOf())

    fun getMyData() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getData()

        retrofitData.enqueue(object : Callback<AlpacaParty?> {
            override fun onResponse(call: Call<AlpacaParty?>, response: Response<AlpacaParty?>) {
                val responseBody = response.body()!!

                //val myStringBuilder = StringBuilder()

                //    myStringBuilder.append(responseBody.parties)
                //    myStringBuilder.append("\n")
                //    responseBody.returnList()
                //binding.textView1.text = myStringBuilder
                //println(myStringBuilder)
                println(responseBody.returnList())
                //pasteDataInRecycler(responseBody.returnList())
                //model.getAlpacaParty(responseBody)
                getMyDataDistrict(responseBody)
                dataSourceAlpacaParty = responseBody
            }

            override fun onFailure(call: Call<AlpacaParty?>, t: Throwable) {
                //Toast.makeText(applicationContext, "ops system not loaded", Toast.LENGTH_SHORT).show()
                println("Failed to call <DataSource>: $t")
            }
        })
    }

    fun getMyDataDistrict(myDataSource: AlpacaParty) {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getDataDistrict1("district1.json")

        retrofitData.enqueue(object : Callback<DistrictData?> {
            override fun onResponse(call: Call<DistrictData?>, response: Response<DistrictData?>) {
                val responseBody = response.body()!!

                //val myStringBuilder = StringBuilder()

                //    myStringBuilder.append(responseBody.parties)
                //    myStringBuilder.append("\n")
                //    responseBody.returnList()
                //binding.textView1.text = myStringBuilder
                //println(myStringBuilder)
                println(responseBody)
                districtCalculateData(myDataSource, responseBody)
            }

            override fun onFailure(call: Call<DistrictData?>, t: Throwable) {
                //Toast.makeText(applicationContext, "ops system not loaded", Toast.LENGTH_SHORT).show()
                println("Failed to call <AlpacaParty>: $t")
            }
        })
    }

    fun pasteDataInRecycler(dataList: MutableList<AlpacaPartyItem>) {
        //model.updateAlpacaParty(dataList)
        //binding.recyclerView.adapter = PartyAdapter(dataList)
        //binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    // beregner dataen slik at vi fordeller stemmer til alle partier og finner %
    fun districtCalculateData(dataPartyList: AlpacaParty, dataDistrictList: DistrictData) {
        val votes = setInnStemmer(dataDistrictList, dataPartyList)
        val totalVotes = votes.sum()

        println("dirstrikt 1 distrikdata: " + {dataDistrictList})
        println("dirstrikt 1 votes: " + {votes})
        println("dirstrikt 1 totalvotes:" + {totalVotes})

        settInn(dataPartyList.returnList(), votes, totalVotes)
    }

    fun setInnStemmer(districtList : DistrictData, parties: AlpacaParty): MutableList<Int> {
        var votes: Int
        for (party in parties.returnList()) {
            votes = 0
            for (vote in districtList) {
                if (vote.id == party.id) {
                    votes++
                    party.votes = votes
                }
            }
        }

        val voteList = mutableListOf<Int>()

        for (party in parties.returnList()) {
            voteList.add(party.votes)
        }
        return voteList
    }

    // fordeler stemmer til alle partier ut visuelt
    fun settInn(alpacapartyListe : MutableList<AlpacaPartyItem>, s : MutableList<Int>, t : Int){

        for((pos, party) in alpacapartyListe.withIndex()){
            var prosent = (s[pos].toDouble() / t) * 100
            prosent = String.format("%.2f", prosent).toDouble()
            party.voteText = s[pos].toString() + " stemmer, " + prosent.toString() + "% av totalen"
        }

        pasteDataInRecycler(alpacapartyListe)
    }

    fun getMyDataDistrict2(myDataSource: AlpacaParty) {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getDataDistrict1("district2.json")

        retrofitData.enqueue(object : Callback<DistrictData?> {
            override fun onResponse(call: Call<DistrictData?>, response: Response<DistrictData?>) {
                val responseBody = response.body()!!

                //val myStringBuilder = StringBuilder()

                //    myStringBuilder.append(responseBody.parties)
                //    myStringBuilder.append("\n")
                //    responseBody.returnList()
                //binding.textView1.text = myStringBuilder
                //println(myStringBuilder)
                println(responseBody)
                districtCalculateData(myDataSource, responseBody)
            }

            override fun onFailure(call: Call<DistrictData?>, t: Throwable) {
                //Toast.makeText(applicationContext, "ops system not loaded", Toast.LENGTH_SHORT).show()
                println("Failed to call <AlpacaParty>: $t")
            }
        })
    }

    // function to read district3.xml file
    fun getMyDataDistrict3(myDataSource: DataSource) {
        try{
                GlobalScope.launch {
                    withContext(Dispatchers.IO) {
                        val xml = Fuel.get("https://www.uio.no/studier/emner/matnat/ifi/IN2000/v22/obligatoriske-oppgaver/district3.xml").awaitString()
                        //Log.d("ListePrint xml", xml.toString()) //sjekket om det funket
                        val inputStream = xml.byteInputStream()
                        //Log.d("ListePrint inputstream", inputStream.toString())
                        val listOfParties = XmlParser().parse(inputStream) //parser informasjonen og faar tilbake liste med partier og hvor mange stemmer de fikk
                        //Log.d("party objekter liste", listOfParties.toString())
                        var stemmer = mutableListOf<Int>()
                        var stemmetall : Int
                        for(party in dataSourceAlpacaParty.returnList()){
                            for(k in listOfParties){ //fordeler stemmer
                                if(k.id == party.id){
                                    party.votes = k.votes!!
                                    k.votes?.let { stemmer.add(it) }
                                }
                            }
                        }
                        //Log.d("id1 total", id1.toString())
                        //Log.d("id2 total", id2.toString())
                        //Log.d("id3 total", id3.toString())
                        //Log.d("id4 total", id4.toString())
                        val totalmengdestemmer = stemmer.sum()
                        //Log.d("valgdistrikt 3 a", alpacapartyListe.toString())
                        //Log.d("valgdistrikt 3 s", stemmer.toString())
                        //Log.d("valgdistrikt 3 t", totalmengdestemmer.toString())

                        settInn(dataSourceAlpacaParty.returnList(), stemmer, totalmengdestemmer)
                    }
                }
        } catch(exception: Exception) {
            println("A network request exception was thrown: ${exception.message}")
        }
}}


