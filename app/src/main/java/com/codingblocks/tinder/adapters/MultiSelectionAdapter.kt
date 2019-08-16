package com.codingblocks.tinder.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.codingblocks.tinder.R
import kotlinx.android.synthetic.main.item_selectable.view.*
import java.io.Serializable


data class Orientations(
    val name: String,
    var isChecked: Boolean = false
) : Serializable

class MultiSelectionAdapter(private val list: ArrayList<Orientations>) :
    RecyclerView.Adapter<MultiSelectionAdapter.MultiViewHolder>() {
    var count: Int = 0
    public var selectedList = MutableLiveData<List<Orientations>>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_selectable,
            parent, false
        )
        return MultiViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MultiViewHolder, position: Int) {
        holder.bind(list[position])
    }


    inner class MultiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(orientations: Orientations) {
        itemView.textView.text = orientations.name
        itemView.imageView.visibility = if (orientations.isChecked) View.VISIBLE else View.GONE
        itemView.setOnClickListener {
                if (orientations.isChecked) {
                    count--
                    orientations.isChecked = false
                } else {
                    if (count < 3) {
                        orientations.isChecked = true
                        count++
                    }
                }
            itemView.imageView.visibility = if (orientations.isChecked) View.VISIBLE else View.GONE
            notifyItemChanged(adapterPosition)
            selectedList.postValue(list.filter { it.isChecked })
        }
    }

    }
}
