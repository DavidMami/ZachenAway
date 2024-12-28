package com.example.zachenaway.data

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

object RetrofitClient {
    private const val BASE_URL = "https://countriesnow.space/api/v0.1/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

data class Country(
    val country: String,
)

object CountriesClient {
    private val countriesApiService: CountriesApiService by lazy {
        RetrofitClient.retrofit.create(CountriesApiService::class.java)
    }

    fun getIsraelCities(callback: (List<String>) -> Unit, context: Context? = null) {
        val body = Country("israel")
        val call = this.countriesApiService.getCities(body)
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
}

interface CountriesApiService {
    @POST("countries/cities")
    fun getCities(@Body country: Country): Call<List<String>>
}