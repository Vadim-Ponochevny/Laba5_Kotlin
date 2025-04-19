package com.example.laba5_kotlin

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import timber.log.Timber

class Adapter(
    private val context: Context,
    private val list: List<String>
) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    // ViewHolder — держит ссылки на элементы одной строки списка
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val viewItem: View = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rview_item, parent, false)
        return ViewHolder(view)
    }

    // onBindViewHolder используется для инициализации и манипуляций с ячейками.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val UrlOfCurrentItem = list[position]

        Glide.with(holder.itemView.context)
            .load(UrlOfCurrentItem)
            .into(holder.viewItem as ImageView)


        holder.viewItem.setOnClickListener {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Photo URL", UrlOfCurrentItem)
            clipboard.setPrimaryClip(clip)

            Timber.i("Copied URL: $UrlOfCurrentItem")
            Toast.makeText(context, "Ссылка скопирована", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = list.size
}