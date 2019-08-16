package com.codingblocks.tinder.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codingblocks.tinder.R
import kotlinx.android.synthetic.main.item_selectable.view.*
import java.io.Serializable


data class Orientations(
    val name: String,
    val isChecked: Boolean = false
) : Serializable

class MultiSelectionAdapter(private val list: ArrayList<Orientations>) : RecyclerView.Adapter<MultiViewHolder>() {

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

    fun getSelected() : List<Orientations> = list.filter { it.isChecked }
}

class MultiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        var count: Int = 0
    }

    fun bind(orientations: Orientations) {
        itemView.textView.text = orientations.name
        itemView.imageView.visibility = if (orientations.isChecked) View.VISIBLE else View.GONE
        itemView.imageView.setOnClickListener {
            if (count != 3)
                if (orientations.isChecked) {
                    count--
                } else {
                    count++
                }
            itemView.imageView.visibility = if (orientations.isChecked) View.VISIBLE else View.GONE
        }
    }

}
