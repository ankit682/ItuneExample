package com.example.ituneexample.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ituneexample.R
import com.example.ituneexample.model.Artist

class ArtistAdapter(private val artistList: MutableList<Artist>) :
    RecyclerView.Adapter<ArtistAdapter.ItemViewHolder>() {

    lateinit var clickListener: OnArtistClickListener

    interface OnArtistClickListener {
        fun onArtistClick(position: Int)
    }

    fun setOnArtistClickListener(listener: OnArtistClickListener) {
        clickListener = listener
    }

    class ItemViewHolder(view: View, listener: OnArtistClickListener) :
        RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.name_text_view)
        val idTextView: TextView = view.findViewById(R.id.id_text_view)


        init {
            view.setOnClickListener {
                listener.onArtistClick(absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // Inflate view for item
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.artist_layout, parent, false),
            clickListener
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        //Binding data
        val currentItem = artistList[position]
        holder.nameTextView.text = currentItem.name
        holder.idTextView.text = currentItem.artistId.toString()
    }

    override fun getItemCount(): Int {
        return artistList.size
    }
}