package com.codingblocks.tinder.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.codingblocks.tinder.R
import kotlinx.android.synthetic.main.fragment_sign_up_photos.view.*

data class Photos(
    val url: String? = null,
    var bitmap: Bitmap?
)

class PhotosAdapter(private val list: ArrayList<Photos>) : RecyclerView.Adapter<PhotosAdapter.MultiViewHolder>() {
    var selectedList = MutableLiveData<List<String?>>()
    var onClick: PhotoClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_image,
            parent, false
        )
        return MultiViewHolder(view)
    }

    override fun getItemCount(): Int = 9

    override fun onBindViewHolder(holder: MultiViewHolder, position: Int) {
        holder.bind(list[position])
    }


    inner class MultiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(photos: Photos) {
            itemView.imageView.setImageBitmap(photos.bitmap)
            itemView.setOnClickListener {
                onClick?.onClick(adapterPosition)
            }
            selectedList.postValue(list.filter { it.url != null }.map { it.url })
        }

    }
}

interface PhotoClickListener {
    fun onClick(position: Int)
}