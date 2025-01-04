package com.example.zachenaway.data.network

import android.content.Context
import android.util.Log
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

data class CitiesResponse(
    val error: Boolean,
    val msg: String,
    val data: List<String>
)

object CountriesClient {
    private val countriesApiService: CountriesApiService by lazy {
        RetrofitClient.retrofit.create(CountriesApiService::class.java)
    }

    fun getIsraelCities(callback: (List<String>) -> Unit, context: Context? = null) {
        val body = Country("Israel")

        val call = countriesApiService.getCities(body)

        call.enqueue(object : Callback<CitiesResponse> {
            override fun onResponse(
                call: Call<CitiesResponse>,
                response: Response<CitiesResponse>
            ) {
                if (response.isSuccessful) {
                    val citiesResponse = response.body()

                    if (citiesResponse != null && !citiesResponse.error) {
                        // Get the cities list from the 'data' field
                        val cities = citiesResponse.data

                        Log.d("Cities", cities.toString())

                        if (context != null) {
                            callback(cities)
                        } else {
                            Log.d("Cities", "Fragment is not attached to a context")
                        }
                    } else {
                        Log.d("Cities", "Error in response: ${citiesResponse?.msg}")
                        callback(emptyList())
                    }
                } else {
                    Log.d("Cities", "Failed to retrieve cities: ${response.message()}")
                    callback(emptyList())
                }
            }

            override fun onFailure(call: Call<CitiesResponse>, t: Throwable) {
                Log.d("Cities", "Error: ${t.message}")
                callback(emptyList())
            }
        })
    }
}

interface CountriesApiService {
    @POST("countries/cities")
    fun getCities(@Body country: Country): Call<CitiesResponse>
}