package com.codingblocks.tinder.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.codingblocks.tinder.R
import com.codingblocks.tinder.fragments.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_swipe_cards.view.*
import android.widget.LinearLayout



class CardStackAdapter(
    private var users: List<User> = emptyList()
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_swipe_cards, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])

    }

    override fun getItemCount(): Int = users.size

    fun setUsers(users: List<User>) {
        this.users = users
    }

    fun getUsers(): List<User> = users

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val indicators = arrayOfNulls<ImageView?>(6)
        fun bind(user: User) {
            with(user) {
                itemView.item_name.text = name
                photos.forEachIndexed { index, s ->
                    indicators[index] = ImageView(itemView.context)
                    indicators[index]?.setImageDrawable(itemView.resources.getDrawable(R.drawable.nonselecteditem))

                    val params = LinearLayout.LayoutParams(
                        100,
                        25, 1f
                    )
                    params.setMargins(4, 10, 4, 0)

                    itemView.indicator_ll.addView(indicators[index],params)
                    Picasso.get().load(s).centerCrop().fetch()
                }
            }
        }

    }

}