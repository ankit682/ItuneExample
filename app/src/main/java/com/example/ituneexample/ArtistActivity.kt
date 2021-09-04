package com.example.ituneexample

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONException
import org.json.JSONObject
import java.net.MalformedURLException
import java.net.URL

class ArtistActivity : AppCompatActivity() {

    val EXTRA_MESSAGE = "message"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist)

        // ignore needing to network on separate thread
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        // get url with JSON data
        val url: URL? = try {
            URL("https://itunes.apple.com/search?term=musicArtist")
        } catch (e: MalformedURLException) {
            Log.d("No URL Exception", e.toString())
            null
        }

        // read JsonData into a list of items then sort items
        var artistList: MutableList<Artist> = parseJson(url?.readText())

        Log.e("Size", "${artistList.size}")

        artistList = artistList.toSet().toMutableList()
        var artistAdapter = ArtistAdapter(artistList)
        val artistRecyclerView: RecyclerView = findViewById(R.id.artist_recycler_view)
        artistRecyclerView.adapter = artistAdapter

        artistAdapter.setOnArtistClickListener(object : ArtistAdapter.OnArtistClickListener {
            override fun onArtistClick(position: Int) {
                val intent = Intent(this@ArtistActivity, ArtistSongActivity::class.java).apply {
                    putExtra(EXTRA_MESSAGE, artistList.get(position).artistId)
                }
                startActivity(intent)
            }
        })
        artistRecyclerView.layoutManager = LinearLayoutManager(applicationContext)

    }

    private fun parseJson(data: String?): MutableList<Artist> {
        val artistList = mutableListOf<Artist>()
        try {

            val jsonObject = JSONObject(data)
            val artistArray = jsonObject.optJSONArray("results")
            Log.e("artistArray", "$artistArray")

            for (i in 0 until artistArray.length()) {
                val jsonArtist = artistArray.getJSONObject(i)
                var artistId = jsonArtist.getInt("artistId")
                val artistName: String? = jsonArtist.getString("artistName")

                val artist = Artist(artistId, artistName)
                artistList.add(artist)
            }
        } catch (e: JSONException) {
            Log.d("Parse Json Exception", e.toString())
        }
        return artistList
    }

}