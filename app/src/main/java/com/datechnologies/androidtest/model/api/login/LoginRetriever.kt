package com.datechnologies.androidtest.model.api.login

import com.datechnologies.androidtest.model.api.chat.ChatInterface
import com.datechnologies.androidtest.model.loginDTO.LoginResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginRetriever {
    private val loginInterface: LoginInterface

    companion object {
        var BaseURL = "https://dev.rapptrlabs.com/"
    }

    init {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(BaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        loginInterface = retrofit.create(LoginInterface::class.java)

    }

    suspend fun postLoginReceiveResponseBody(loginCreds: HashMap<String, String>) : LoginResponse {
        val response = loginInterface.sendLogin(loginCreds)
        return LoginResponse(
            code = response.code(),
            message = response.message()
        )
    }

}