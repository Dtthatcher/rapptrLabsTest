package com.datechnologies.androidtest.model.api.chat

import com.datechnologies.androidtest.model.chatDTO.ChatMessage
import com.datechnologies.androidtest.model.loginDTO.LoginResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChatRetriever {
    private val chatInterface: ChatInterface

    companion object {
        var BaseURL = "https://dev.rapptrlabs.com/"
    }

    init {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(BaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        chatInterface = retrofit.create(ChatInterface::class.java)
    }

    suspend fun getChats(): List<ChatMessage> {
        val data = chatInterface.getChats().body()?.data!!

        return data
    }

}