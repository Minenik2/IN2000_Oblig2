package com.example.nikitafe_oblig2

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nikitafe_oblig2.databinding.ActivityMainBinding
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import kotlinx.coroutines.*
import layout.PartyAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private lateinit var binding: ActivityMainBinding
const val BASE_URL = "https://www.uio.no/studier/emner/matnat/ifi/IN2000/v22/obligatoriske-oppgaver/"
const val DISTRICT2_SUB_URL = "district2.json"
const val DISTRICT3_SUB_URL = "district3.xml"

class MainActivity : AppCompatActivity() {

    // creating a viewmodel for observer
    private val model: MainActivityViewModel = MainActivityViewModel()
    private var dataSourceAlpacaParty: DataSource = DataSource(mutableListOf())

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



        model.getConnectAlpacaParty()

        model.getAlpacaParty().observe(this) {
            if (it != null) {
                binding.recyclerView.adapter = PartyAdapter(it)
                binding.recyclerView.layoutManager = LinearLayoutManager(this)
            }
        }



        // listener når brukeren klikker på spinneren
        mySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{ //hva som skjer når items i spinneren er valgt
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(mySpinner.selectedItem){
                    "valgdistrikt 1" ->{ //dersom man velger foerste posisjon
                        model.getMyDataDistrict1()
                    }
                    "valgdistrikt 2" ->{
                        model.getMyDataDistrict2()
                    }
                    "valgdistrikt 3" ->{
                        model.getMyDataDistrict3()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(applicationContext,"Velg noe i drop down menyen",Toast.LENGTH_SHORT).show()
            }
        }
    }
}