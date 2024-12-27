package com.example.zachenaway.data

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

object CountriesClient {
    private const val BASE_URL = "https://countriesnow.space/api/v0.1"

    private val client = OkHttpClient.Builder().apply {}.build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create()) // Gson for JSON parsing
        .build()

    val countriesApiService: CountriesApiService = retrofit.create(CountriesApiService::class.java)
}

data class Country(
    val country: String,
)

fun getIsraelCities(callback: (List<String>) -> Unit, context: Context?= null) {
    val call = CountriesClient.countriesApiService.getCities(Country("israel"))
    call.enqueue(object : Callback<List<String>> {
        override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
            if (response.isSuccessful) {
                val cities = response.body()

                if (cities != null) {
                    callback(cities)
                } else {
                    callback(emptyList())
                }
            } else {
                callback(emptyList())
            }
        }

        override fun onFailure(call: Call<List<String>>, t: Throwable) {
            println("Error: ${t.message}")
        }
    })
}

interface CountriesApiService {
    @POST("/countries/cities")
    fun getCities(@Body country: Country): Call<List<String>>
}