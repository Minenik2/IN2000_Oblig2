package com.example.nikitafe_oblig2

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("alpacaparties.json")
    fun getData(): Call<DataSource>

    @GET("{base}")
    fun getDataDistrict1(@Path("base") baseId: String): Call<DistrictData>

    /*
    @GET("district2.json")
    fun getDataDistrict2(): Call<DistrictData>

    @GET("district3.xml")
    fun getDataDistrict3(): Call<DistrictData>*/
}