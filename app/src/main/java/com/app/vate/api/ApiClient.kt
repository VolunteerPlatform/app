package com.app.vate.api

import com.app.vate.activity.LoginContext
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.CookieManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object ApiClient {

    private const val SERVER_URL = "http://server.vate.kr:8080"

    fun getClient(): Retrofit {
        val gson = GsonBuilder()
            .registerTypeAdapter(
                LocalDate::class.java,
                JsonDeserializer { json, _, _ ->
                    LocalDate.parse(
                        json.asString,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    )
                })
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .client(provideOkHttpClient(AppInterceptor()))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient =
        OkHttpClient.Builder().run {
            addInterceptor(interceptor)
                .cookieJar(JavaNetCookieJar(CookieManager()))
                .build()
        }

    class AppInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
            val newRequest: Request;

            if (LoginContext.accessToken.isNotEmpty()) {
                newRequest = request().newBuilder()
                    .addHeader("accessToken", LoginContext.accessToken)
                    .build()
            } else {
                newRequest = request()
                    .newBuilder()
                    .build()
            }

            proceed(newRequest)
        }
    }

}