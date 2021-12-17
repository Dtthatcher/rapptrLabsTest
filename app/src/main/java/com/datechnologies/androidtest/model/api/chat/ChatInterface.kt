package com.datechnologies.androidtest.model.api.chat

import com.datechnologies.androidtest.model.chatDTO.DataSet
import retrofit2.Response
import retrofit2.http.GET

interface ChatInterface {

    @GET("Tests/scripts/chat_log.php")

    suspend fun getChats() : Response<DataSet>
}