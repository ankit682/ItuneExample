package com.example.ituneexample.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ituneexample.R
import com.example.ituneexample.model.Song

class SongAdapter(var songList: MutableList<Song>) :
    RecyclerView.Adapter<SongAdapter.SongViewHolder>() {


    class SongViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        val trackNameTextView: TextView = view.findViewById(R.id.track_name_view)
        val trackIdTextView: TextView = view.findViewById(R.id.track_id_text_view)
        val collectionNameTextView: TextView = view.findViewById(R.id.collection_name_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        // Inflate view for song
        return SongViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.song_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        //Binding data
        val currentItem = songList[position]
        holder.trackNameTextView.text = currentItem.trackName
        holder.trackIdTextView.text = currentItem.trackId.toString()
        holder.collectionNameTextView.text = currentItem.collectionName
    }

    override fun getItemCount(): Int {
        return songList.size
    }
}