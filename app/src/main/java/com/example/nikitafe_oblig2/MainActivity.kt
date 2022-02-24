package com.example.nikitafe_oblig2

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nikitafe_oblig2.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import layout.PartyAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


private lateinit var binding: ActivityMainBinding
const val BASE_URL = "https://www.uio.no/studier/emner/matnat/ifi/IN2000/v22/obligatoriske-oppgaver/"
const val DISTRICT2_SUB_URL = "district2.json"
const val DISTRICT3_SUB_URL = "district3.xml"

class MainActivity : AppCompatActivity() {

    // creating a viewmodel for observer
    private val model: MainActivityViewModel = MainActivityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // made spinner
        val mySpinner: Spinner = binding.sp
        ArrayAdapter.createFromResource(this,
            R.array.valgdistrikt,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mySpinner.adapter = adapter
        }

        GlobalScope.launch {
            taskMakeAPI()
        }

        val activityObserver = Observer<AlpacaParty> {

        }

        model.currentLiveData.observe(this, activityObserver)

        // listener når brukeren klikker på spinneren
        mySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{ //hva som skjer når items i spinneren er valgt
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(mySpinner.selectedItem){
                    "valgdistrikt 1" ->{ //dersom man velger foerste posisjon
                        GlobalScope.launch {
                            withContext(Dispatchers.IO) {

                            }
                        }
                        /*
                        CoroutineScope(newSingleThreadContext("valgdistrikt1")).launch(Dispatchers.IO){
                            try{
                                //println(Fuel.get(valg1).awaitString()) // "{"origin":"127.0.0.1"}"
                                val mlid : List<Id> = gson.fromJson(Fuel.get(valg1).awaitString(), Array<Id>::class.java).toList()//henter json og gjoer det om til objekter som er satt i liste

                                val stemmer = settInnStemmer(mlid, alpacapartyListe)
                                val totalmengdestemmer = stemmer.sum() //finner total mengden av stemmer for a regne ut prosent
                                Log.d("valgdistrikt 1 a", alpacapartyListe.toString())
                                Log.d("valgdistrikt 1 s", stemmer.toString())
                                Log.d("valgdistrikt 1 t", totalmengdestemmer.toString())

                                settInn(alpacapartyListe, stemmer, totalmengdestemmer)
                                CoroutineScope(newSingleThreadContext("valgdistrikt1")).launch(Dispatchers.IO){

                                }
                            } catch(exception: Exception) {
                                println("A network request exception was thrown: ${exception.message}")
                            }
                        }*/
                    }
                    "valgdistrikt 2" ->{
                        /*
                        CoroutineScope(newSingleThreadContext("valgdistrikt2")).launch(Dispatchers.IO){ //gjoer det samme som "valgdistrikt 1"
                            try{
                                //println(Fuel.get(valg2).awaitString()) // "{"origin":"127.0.0.1"}"
                                val mlid : List<Id> = gson.fromJson(Fuel.get(valg2).awaitString(), Array<Id>::class.java).toList()

                                val stemmer = settInnStemmer(mlid, alpacapartyListe)
                                val totalmengdestemmer = stemmer.sum()
                                Log.d("valgdistrikt 2 a", alpacapartyListe.toString())
                                Log.d("valgdistrikt 2 s", stemmer.toString())
                                Log.d("valgdistrikt 2 t", totalmengdestemmer.toString())
                                settInn(alpacapartyListe, stemmer, totalmengdestemmer)
                            } catch(exception: Exception) {
                                println("A network request exception was thrown: ${exception.message}")
                            }
                        }
                        //valgdistriktListe = hentvalgdistrikt.await()*/
                    }
                    "valgdistrikt 3" ->{
                        /*
                        CoroutineScope(newSingleThreadContext("valgdistrikt2")).launch(Dispatchers.IO){ //henter inn xml og lager objekter via xml parser
                            async{
                                try{
                                    val xml = Fuel.get(valg3).awaitString()
                                    //Log.d("ListePrint xml", xml.toString()) //sjekket om det funket
                                    val inputStream = xml.byteInputStream()
                                    //Log.d("ListePrint inputstream", inputStream.toString())
                                    val listOfParties = XmlParser().parse(inputStream) //parser informasjonen og faar tilbake liste med partier og hvor mange stemmer de fikk
                                    //Log.d("party objekter liste", listOfParties.toString())
                                    var stemmer = mutableListOf<Int>()
                                    var stemmetall : Int
                                    for(i in alpacapartyListe){
                                        for(j in i.parties!!){
                                            for(k in listOfParties){ //fordeler stemmer
                                                if(k.id == j.id){
                                                    j.stemme = k.votes
                                                    k.votes?.let { stemmer.add(it) }
                                                }
                                            }
                                        }
                                    }
                                    //Log.d("id1 total", id1.toString())
                                    //Log.d("id2 total", id2.toString())
                                    //Log.d("id3 total", id3.toString())
                                    //Log.d("id4 total", id4.toString())
                                    val totalmengdestemmer = stemmer.sum()
                                    Log.d("valgdistrikt 3 a", alpacapartyListe.toString())
                                    Log.d("valgdistrikt 3 s", stemmer.toString())
                                    Log.d("valgdistrikt 3 t", totalmengdestemmer.toString())
                                    settInn(alpacapartyListe, stemmer, totalmengdestemmer)
                                } catch(exception: Exception) {
                                    println("A network request exception was thrown: ${exception.message}")
                                }
                            }
                        }*/
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(applicationContext,"Velg noe i drop down menyen",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun taskMakeAPI() {
        withContext(Dispatchers.IO) {
            getMyData()


            /*
            val gson = Gson()
            val partyDataClass = AlpacaParty(gson.fromJson("color", String::class.java).toString(),
                gson.fromJson("id", String::class.java).toString(),
                gson.fromJson("img", String::class.java).toString(),
                gson.fromJson("leader", String::class.java).toString(),
                    gson.fromJson("name", String::class.java).toString())
            println(partyDataClass)*/
        }
    }

    private fun getMyData() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getData()

        retrofitData.enqueue(object : Callback<DataSource?> {
            override fun onResponse(call: Call<DataSource?>, response: Response<DataSource?>) {
                val responseBody = response.body()!!

                //val myStringBuilder = StringBuilder()

                //    myStringBuilder.append(responseBody.parties)
                //    myStringBuilder.append("\n")
                //    responseBody.returnList()
                //binding.textView1.text = myStringBuilder
                //println(myStringBuilder)
                println(responseBody.returnList())
                //pasteDataInRecycler(responseBody.returnList())
                model.getAlpacaParty(responseBody)
                getMyDataDistrict(responseBody)
            }

            override fun onFailure(call: Call<DataSource?>, t: Throwable) {
                Toast.makeText(applicationContext, "ops system not loaded", Toast.LENGTH_SHORT).show()
                println("Failed to call <DataSource>: $t")
            }
        })
    }

    private fun getMyDataDistrict(myDataSource: DataSource) {
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
                Toast.makeText(applicationContext, "ops system not loaded", Toast.LENGTH_SHORT).show()
                println("Failed to call <DataSource>: $t")
            }
        })
    }

    private fun pasteDataInRecycler(dataList: MutableList<AlpacaParty>) {
        binding.recyclerView.adapter = PartyAdapter(dataList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    // beregner dataen slik at vi fordeller stemmer til alle partier og finner %
    private fun districtCalculateData(dataPartyList: DataSource, dataDistrictList: DistrictData) {
        val votes = setInnStemmer(dataDistrictList, dataPartyList)
        val totalVotes = votes.sum()

        println("dirstrikt 1 distrikdata: " + {dataDistrictList})
        println("dirstrikt 1 votes: " + {votes})
        println("dirstrikt 1 totalvotes:" + {totalVotes})

        settInn(dataPartyList.returnList(), votes, totalVotes, 0)
    }

    private fun setInnStemmer(districtList : DistrictData, parties: DataSource): MutableList<Int> {
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
    private fun settInn(alpacapartyListe : MutableList<AlpacaParty>, s : MutableList<Int>, t : Int, districtIndex: Int){

        for((pos, party) in alpacapartyListe.withIndex()){
            var prosent = (s[pos].toDouble() / t) * 100
            prosent = String.format("%.2f", prosent).toDouble()
            party.voteTextList[districtIndex] = s[pos].toString() + " stemmer, " + prosent.toString() + "% av totalen"
        }

        pasteDataInRecycler(alpacapartyListe)
    }
}