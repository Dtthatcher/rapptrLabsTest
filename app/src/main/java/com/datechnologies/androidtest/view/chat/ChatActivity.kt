package com.datechnologies.androidtest.view.chat

import android.content.Context
import android.os.Bundle
import com.datechnologies.androidtest.R
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.datechnologies.androidtest.model.api.chat.ChatRetriever
import com.datechnologies.androidtest.model.chatDTO.ChatMessage
import com.datechnologies.androidtest.view.chat.recyclerview.viewHolder.ChatAdapter
import com.datechnologies.androidtest.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.coroutines.*

/**
 * Screen that displays a list of chats from a chat log.
 */
class ChatActivity : AppCompatActivity() {
    //==============================================================================================
    // Class Properties
    //==============================================================================================

    private val chatRetriever: ChatRetriever = ChatRetriever()

    //==============================================================================================
    // Lifecycle Methods
    //==============================================================================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        val actionBar = supportActionBar!!
        actionBar.title = "Chat"
        actionBar.elevation = 30F
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)


        initRecyclerview()
        fetchChats()

        // TODO: Make the UI look like it does in the mock-up. Allow for horizontal screen rotation. DONE

        // TODO: Retrieve the chat data from http://dev.rapptrlabs.com/Tests/scripts/chat_log.php  DONE
        // TODO: Parse this chat data from JSON into ChatLogMessageModel and display it. DONE
    }

    private fun fetchChats() {
        val chatsFetchJob = Job()

        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }

        val scope = CoroutineScope(chatsFetchJob + Dispatchers.Main)
        scope.launch(errorHandler) {
            // fetched data
            val chatsResponse = chatRetriever.getChats()
            // render data in recyclerview
            renderData(chatsResponse)
        }
    }

    private fun renderData(chatResponse: List<ChatMessage>) {
        recyclerView.adapter = ChatAdapter(chatResponse)
    }

    private fun initRecyclerview() {
        recyclerView.layoutManager = LinearLayoutManager( this, LinearLayoutManager.VERTICAL,false )
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    companion object {
        //==============================================================================================
        // Static Class Methods
        //==============================================================================================
        fun start(context: Context) {
            val starter = Intent(context, ChatActivity::class.java)
            context.startActivity(starter)
        }
    }
}