package com.example.clientaccesscontrol.data.retrofit

import android.annotation.SuppressLint
import android.content.Context
import android.util.Base64
import android.util.Log
import com.example.clientaccesscontrol.BuildConfig
import com.example.clientaccesscontrol.data.preference.UserPreference
import com.example.clientaccesscontrol.data.preference.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object ConfigApi {

    fun getApiServiceCAC(): ServiceApiCAC {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.203.164:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ServiceApiCAC::class.java)
    }

    private fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
        try {
            val trustAllCerts = arrayOf<TrustManager>(@SuppressLint("CustomX509TrustManager")
            object : X509TrustManager {
                @SuppressLint("TrustAllX509TrustManager")
                override fun checkClientTrusted(
                    chain: Array<X509Certificate>,
                    authType: String,
                ) {
                }

                @SuppressLint("TrustAllX509TrustManager")
                override fun checkServerTrusted(
                    chain: Array<X509Certificate>,
                    authType: String,
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { _, _ -> true }

            return builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun getApiServiceMikrotik(
        context: Context
    ): ServiceApiMikrotik {
        try {
            val loggingInterceptor = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }
            //Getting Base URL from DataStore
            val pref = UserPreference.getInstance(context.dataStore)
            val baseUrl = runBlocking { pref.getSession().first().ipAdress }

            val basicAuth = Interceptor { chain ->
                //Getting Base URL and Logging Interceptor Log
                Log.d("getApiServiceMikrotik", "Base URL: $baseUrl")
                Log.d("getApiServiceMikrotik", "Logging Interceptor: $loggingInterceptor")

                //Getting Username, and Password from DataStore
                val username = runBlocking { pref.getSession().first().username }
                val password = runBlocking { pref.getSession().first().password }
                Log.d("getApiServiceMikrotik", "Username: $username, Password: $password")

                //Creating Credential
                val credentials = "$username:$password"
                Log.d("getApiServiceMikrotik", "Credentials: $credentials")

                //Creating Authorization Header
                val auth = "Basic " + Base64.encodeToString(credentials.toByteArray(Charsets.UTF_8), Base64.NO_WRAP).replace("\n", "")
                Log.d("getApiServiceMikrotik", "Authorization Header: $auth")

                //Adding Authorization Header to Request
                val request = chain.request().newBuilder().addHeader("Authorization", auth).build()
                Log.d("BasicAuthInterceptor", "Request URL: ${request.url}")
                Log.d("BasicAuthInterceptor", "Request Headers: ${request.headers}")

                //Sending Request and Getting Response
                val response: Response
                try {
                    response = chain.proceed(request)
                    Log.d("BasicAuthInterceptor", "Response Code: ${response.code}")
                    if (response.code != 200) {
                        Log.e("BasicAuthInterceptor", "Error: ${response.message}")
                    }
                } catch (e: Exception) {
                    Log.e("BasicAuthInterceptor", "Request failed: ${e.message}")
                    throw e
                }
                response
            }
            Log.d("getApiServiceMikrotik", "Basic Auth Interceptor: $basicAuth")

            val client = getUnsafeOkHttpClient()
                .addInterceptor(basicAuth)
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ServiceApiMikrotik::class.java)
        } catch (e: Exception) {
            Log.e("ConfigApi", "Error in getApiServiceMikrotik: ${e.message}")
            throw e
        }
    }
}