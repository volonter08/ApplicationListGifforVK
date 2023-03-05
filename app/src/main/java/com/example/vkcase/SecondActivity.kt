package com.example.vkcase

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        initViews()
    }
    @SuppressLint("SetTextI18n")
    fun initViews(){
        val title= findViewById<TextView>(R.id.title)
        val rating= findViewById<TextView>(R.id.rating)
        val userName = findViewById<TextView>(R.id.username)
        val arguments = intent.extras
        arguments?.let{
            title.text = "title: "+it.get("title").toString()
            rating.text = "rating: "+it.get("rating").toString()
            userName.text = "username: "+it.get("username").toString()
        }
    }
}