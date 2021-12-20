package com.datechnologies.androidtest.view.chat

import android.content.Context
import android.os.Bundle
import com.datechnologies.androidtest.R
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.datechnologies.androidtest.databinding.ActivityChatBinding
import com.datechnologies.androidtest.model.api.chat.ChatRetriever
import com.datechnologies.androidtest.view.chat.recyclerview.viewHolder.ChatAdapter
import com.datechnologies.androidtest.view.main.MainActivity
import com.datechnologies.androidtest.viewModel.ChatViewModel


/**
 * Screen that displays a list of chats from a chat log.
 */
class ChatActivity : AppCompatActivity() {
    //==============================================================================================
    // Class Properties
    //==============================================================================================

    private lateinit var binding: ActivityChatBinding
    private val chatRetriever: ChatRetriever = ChatRetriever()
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var adapter: ChatAdapter

    //==============================================================================================
    // Lifecycle Methods
    //==============================================================================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)

        val actionBar = supportActionBar!!
        actionBar.title = "Chat"
        actionBar.elevation = 30F
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        chatViewModel = ChatViewModel(chatRetriever)

        initRecyclerview()

        // TODO: Make the UI look like it does in the mock-up. Allow for horizontal screen rotation. DONE

        // TODO: Retrieve the chat data from http://dev.rapptrlabs.com/Tests/scripts/chat_log.php  DONE
        // TODO: Parse this chat data from JSON into ChatLogMessageModel and display it. DONE
    }


    private fun initRecyclerview() {
        binding.recyclerView.layoutManager = LinearLayoutManager( this, LinearLayoutManager.VERTICAL,false )
        adapter = ChatAdapter()
        binding.recyclerView.adapter = adapter
        displayChats()
    }

    private fun displayChats() {
        val responseLiveData = chatViewModel.getChats()
        responseLiveData.observe(this, Observer {
            if(it != null) {
                adapter.setList(it)
                adapter.notifyDataSetChanged()
            } else Toast.makeText(applicationContext,"No data available",Toast.LENGTH_LONG).show()
        })
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