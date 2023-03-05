package com.example.vkcase

import android.R
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide


class GifAdapter(val context:Context,val list: ArrayList<Gif>): BaseAdapter() {
    val lInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private class ViewHolder(view: View){
        var button: Button = view.findViewById(com.example.vkcase.R.id.button)
        var imageView: ImageView= view.findViewById(com.example.vkcase.R.id.imgView)
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            view = lInflater.inflate(
                com.example.vkcase.R.layout.item_list,
                parent,
                false
            )
        }
        val gif:Gif=  getItem(position) as Gif
        val intent = Intent(context,SecondActivity::class.java).apply {
            putExtra("title", gif.title)
            putExtra("rating",gif.rating)
            putExtra("username",gif.userName)
        }
        val imageView = (view?.findViewById<View>(com.example.vkcase.R.id.imgView) as ImageView)
        val button = (view.findViewById<View>(com.example.vkcase.R.id.button) as Button)
        Glide.with(context).load(gif.sourceUrl).into(imageView)
        button.setOnClickListener {
            context.startActivity(intent)
        }
        return view
    }
}