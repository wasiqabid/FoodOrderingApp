package com.example.app_loginpage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.Locale

class PopularAdapter(private val onAddClick: (MenuItem) -> Unit) : ListAdapter<MenuItem, PopularAdapter.PopularViewHolder>(MenuAdapter.MenuDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_popular, parent, false)
        return PopularViewHolder(view, onAddClick)
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PopularViewHolder(itemView: View, private val onAddClick: (MenuItem) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.popularImage)
        private val name: TextView = itemView.findViewById(R.id.popularName)
        private val price: TextView = itemView.findViewById(R.id.popularPrice)
        private val addBtn: ImageView = itemView.findViewById(R.id.addPopularBtn)

        fun bind(item: MenuItem) {
            name.text = item.name
            price.text = String.format(Locale.US, "%.0f", item.price)
            
            Glide.with(itemView.context)
                .load(item.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.burger_pic)
                .into(image)

            addBtn.setOnClickListener {
                onAddClick(item)
            }
        }
    }
}