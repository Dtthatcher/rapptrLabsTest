package com.datechnologies.androidtest.view.chat.recyclerview.viewHolder


import android.view.ViewGroup
import android.view.LayoutInflater
import com.datechnologies.androidtest.R
import android.view.View

import androidx.recyclerview.widget.RecyclerView
import com.datechnologies.androidtest.model.chatDTO.ChatMessage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_chat.view.*

/**
 * A recycler view adapter used to display chat log messages in [ChatActivity].
 *
 */
class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    private val chatList = mutableListOf<ChatMessage>()
    fun setList(chats: List<ChatMessage>){
        chatList.addAll(chats)
    }
    //==============================================================================================
    // ChatViewHolder Class
    //==============================================================================================
    inner class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindView(chatMessage: ChatMessage){

            itemView.bubble.text = chatMessage.message
            itemView.name.text = chatMessage.name
            Picasso.get()
                .load(chatMessage.avatar_url)
                .placeholder(R.drawable.ic_avatar_placeholder)
                .into(itemView.avatarImageView)
        }
    }

    //Adapter Methods

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: ChatViewHolder, position: Int) {
        viewHolder.bindView(chatList[position])
    }

    override fun getItemCount(): Int = chatList.size

}