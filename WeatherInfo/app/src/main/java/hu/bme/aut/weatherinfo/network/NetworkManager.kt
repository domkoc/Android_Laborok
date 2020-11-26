package hu.bme.aut.weatherinfo.network

import android.os.Handler
import hu.bme.aut.weatherinfo.model.WeatherData
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    private const val SERVICE_URL = "https://api.openweathermap.org"
    private const val APP_ID = "92d9cbc504f9e100074418b03f469f64"

    private val weatherApi: WeatherApi

    init {

        val retrofit = Retrofit.Builder()
            .baseUrl(SERVICE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        weatherApi = retrofit.create(WeatherApi::class.java)
    }

    private fun <T> runCallOnBackgroundThread(
        call: Call<T>,
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val handler = Handler()
        Thread {
            try {
                val response = call.execute().body()!!
                handler.post { onSuccess(response) }

            } catch (e: Exception) {
                e.printStackTrace()
                handler.post { onError(e) }
            }
        }.start()
    }

    fun getWeather(
        city: String?,
        onSuccess: (WeatherData) -> Unit,
        onError: (Throwable) -> Unit
    ){
        val getWeatherRequest = weatherApi.getWeather(city, "metric", APP_ID)
        runCallOnBackgroundThread(getWeatherRequest, onSuccess, onError)
    }
}