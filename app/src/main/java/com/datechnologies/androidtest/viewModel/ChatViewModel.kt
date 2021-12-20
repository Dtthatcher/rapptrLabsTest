package com.datechnologies.androidtest.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.datechnologies.androidtest.model.api.chat.ChatRetriever

class ChatViewModel(
    private val chatRetriever : ChatRetriever
): ViewModel() {
    fun getChats() = liveData {
        val chatList = chatRetriever.getChats()
        emit(chatList)
    }
}