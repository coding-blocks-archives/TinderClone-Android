package com.codingblocks.tinder.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codingblocks.tinder.R
import com.codingblocks.tinder.fragments.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_match.view.*

class MatchesAdapter(private val users: ArrayList<Matches>) :
    RecyclerView.Adapter<MatchesAdapter.MatchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder =
        MatchViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_match,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        holder.bind(users[position])
    }


    class MatchViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun bind(user: Matches) {

            itemView.nameTv.text = user.name
            Picasso.get().load(user.photo).into(itemView.profileImgView)
            itemView.setOnClickListener {
                user.uid
            }

        }
    }

}