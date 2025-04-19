package com.example.laba5_kotlin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber
import com.google.gson.Gson
const val API_KEY = "ff49fcd4d4a08aa6aafb6ea3de826464"


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.plant(Timber.DebugTree())

        photoParsing()



    }

    private fun photoParsing() {
        lifecycleScope.launch {
            try {
                val photos = fetchPhotosFromApi()

                val photoUrls = photos.map { photo ->
                    "https://farm${photo.farm}.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}_z.jpg"
                }

                setupRecyclerView(photoUrls)

                for (indexOfPhoto in photos.indices step 5) {
                    val photo = photos[indexOfPhoto]
                    Timber.d(photo.toString())
                }
            } catch (e: Exception) {
                Timber.e(e, "Error fetching photos")
            }
        }
    }

    private fun setupRecyclerView(photoUrls: List<String>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = Adapter(this, photoUrls)

        Timber.d("Adapter attached with ${photoUrls.size} items")
    }

    private suspend fun fetchPhotosFromApi(): List<Photo> = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=" +
                    API_KEY +
                    "&tags=" +
                    "cat" +
                    "&format=" +
                    "json" +
                    "&nojsoncallback=" +
                    "1")
            .build()

        val response = client.newCall(request).execute()
        val body = response.body?.string()

        if (body != null) {
            val wrapper = Gson().fromJson(body, Wrapper::class.java)
            wrapper.photos.photo
        } else {
            emptyList()
        }
    }




}