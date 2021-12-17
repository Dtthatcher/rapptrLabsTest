package com.datechnologies.androidtest.model.chatDTO

/**
 * A data model that represents a chat log message fetched from the D & A Technologies Web Server.
 */
data class DataSet (
    val data: List<ChatMessage>
    )

data class ChatMessage (
    var user_Id:Int = 0,
    var avatar_url: String? = null,
    var name: String? = null,
    var message: String? = null
)