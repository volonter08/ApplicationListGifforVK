package com.example.vkcase

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AbsListView
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    lateinit var gifAdapter: GifAdapter
    private val GIF_SDK_KEY = "Eby7n8zKxPa0yfZhzfZv7j8c6SuCRLMW"
    val listGif = ArrayList<Gif>()
    var countScroll= 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    fun initViews() {
        val listViewGif = findViewById<ListView>(R.id.listView)
        val inputEditText= findViewById<TextInputEditText>(R.id.input_edit_text)
        inputEditText.setOnEditorActionListener { v, actionid, event ->
            if (actionid == EditorInfo.IME_ACTION_DONE) {
                countScroll=0
                listGif.clear()
                gifAdapter.notifyDataSetChanged()
                gifRequest(inputEditText.text.toString(),countScroll++)

            }
            return@setOnEditorActionListener false
        }
        gifAdapter = GifAdapter(this, listGif)
        listViewGif.adapter = gifAdapter
        listViewGif.setOnScrollListener(object: AbsListView.OnScrollListener{
            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
            }
            override fun onScroll(
                view: AbsListView?,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {
                if(firstVisibleItem== countScroll*25-4) {
                    synchronized(listGif) {
                        gifRequest(inputEditText.text.toString(), countScroll++)
                    }
                }
            }
        })
    }
    fun gifRequest(searchName: String,value:Int) {
        val client = OkHttpClient()
        val url =
            "https://api.giphy.com/v1/gifs/search?api_key=${GIF_SDK_KEY}&q=${searchName}&offset=${value*25}&limit=25"
        val request = Request.Builder().url(url).build()
        CoroutineScope(Dispatchers.IO).launch {
            val responses = client.newCall(request).execute()
            val jsonData = responses.body()?.string()
            val jObject = jsonData?.let { JSONObject(it) }
            val jArray = jObject?.getJSONArray("data")
            withContext(Dispatchers.Main) {
                if (jArray != null) {
                    for (i in (0 until jArray.length())) {
                        val obj = jArray.getJSONObject(i)
                        val title = obj.getString("title")
                        val userName = obj.getString("username")
                        val rating = obj.getString("rating")
                        val obj1 = obj.getJSONObject("images")
                        val obj2 = obj1.getJSONObject("downsized_medium")
                        val sourceUrl = obj2.getString("url")
                        listGif.add(Gif(sourceUrl, title, rating, userName))
                        gifAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}