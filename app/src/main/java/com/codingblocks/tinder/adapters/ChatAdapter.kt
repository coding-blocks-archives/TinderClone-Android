package com.codingblocks.tinder.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codingblocks.tinder.R
import com.codingblocks.tinder.models.ChatModelObject
import com.codingblocks.tinder.models.DateObject
import com.codingblocks.tinder.models.ListObject
import com.google.firebase.auth.FirebaseAuth


class ChatAdapter(private val list: ArrayList<ListObject>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            ListObject.TYPE_DATE ->
                DateViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.date_row, parent, false)
                )
            ListObject.TYPE_GENERAL_RIGHT ->
                ChatRightViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.right_chat_row, parent, false)
                )
            ListObject.TYPE_GENERAL_LEFT ->
                ChatLeftViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.left_chat_row, parent, false)
                )
            else -> ChatRightViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.right_chat_row, parent, false)
            )


        }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ListObject.TYPE_DATE -> {
                val dateObject = list[position] as DateObject
                val dateViewHolder = holder as DateViewHolder
                dateViewHolder.bind(dateObject.date)
            }

            ListObject.TYPE_GENERAL_RIGHT
            -> {
                val chatModel = list[position] as ChatModelObject
                val chatViewHolder = holder as ChatRightViewHolder
                chatViewHolder.bind(chatModel.messages)
            }

            ListObject.TYPE_GENERAL_LEFT
            -> {
                val chatModel = list[position] as ChatModelObject
                val chatViewHolder = holder as ChatLeftViewHolder
                chatViewHolder.bind(chatModel.messages)
            }


        }
    }

    override fun getItemViewType(position: Int): Int =
        list[position].getType(FirebaseAuth.getInstance().uid ?: "")


}