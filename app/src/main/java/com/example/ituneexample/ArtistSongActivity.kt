package com.example.ituneexample

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

class ArtistSongActivity : AppCompatActivity() {

    val EXTRA_MESSAGE = "message"
    var amgArtistId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_song)

        var message = intent.getIntExtra(EXTRA_MESSAGE, 0)

        // ignore needing to network on separate thread
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        // get url with JSON data
        var urlString: String = "https://itunes.apple.com/lookup?id=${message}"
        var url: URL? = try {
            URL(urlString)
        } catch (e: MalformedURLException) {
            Log.d("No URL Exception", e.toString())
            null
        }

        amgArtistId = amgArtistParse(url?.readText())
        Log.e("amgArtistId", "ID $amgArtistId")

        urlString = "https://itunes.apple.com/lookup?amgArtistId=${amgArtistId}&entity=song"
        url = try {
            URL(urlString)
        } catch (e: MalformedURLException) {
            Log.d("No URL Exception", e.toString())
            null
        }

        // read JsonData into a list of items then sort items
        var songList: MutableList<Song> = parseJson(url?.readText())

        Log.e("Size", "${songList.size}")

        songList = songList.toSet().toMutableList()
        var songAdapter = SongAdapter(songList)
        val artistRecyclerView: RecyclerView = findViewById(R.id.song_recycler_view)
        artistRecyclerView.adapter = songAdapter
        artistRecyclerView.layoutManager = LinearLayoutManager(applicationContext)

    }

    private fun amgArtistParse(data: String?): Int {

        try {
            val jsonObject = JSONObject(data)
            val itemArray = jsonObject.optJSONArray("results")

            for (i in 0 until itemArray.length()) {
                val jsonItem = itemArray.getJSONObject(i)
                amgArtistId = jsonItem.getInt("amgArtistId")
            }

        } catch (e: JSONException) {
            Log.d("Parse Json Exception", e.toString())
        }

        return amgArtistId
    }

    private fun parseJson(data: String?): MutableList<Song> {
        val songList = mutableListOf<Song>()
        try {

            val jsonObject = JSONObject(data)
            val artistArray = jsonObject.optJSONArray("results")
            Log.e("artistArray", "$artistArray")

            for (i in 1 until artistArray.length()) {
                val jsonArtist = artistArray.getJSONObject(i)
                val trackId = jsonArtist.getInt("trackId")
                val trackName: String? = jsonArtist.getString("trackName")
                val collectionName: String? = jsonArtist.getString("collectionName")

                songList.add(Song(trackId, trackName, collectionName))
            }
        } catch (e: JSONException) {
            Log.d("Parse Json Exception", e.toString())
        }
        return songList
    }

}