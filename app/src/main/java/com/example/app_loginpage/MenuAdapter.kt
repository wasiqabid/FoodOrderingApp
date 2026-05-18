package com.example.app_loginpage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

import com.bumptech.glide.Glide

class MenuAdapter(private val onAddClick: (MenuItem) -> Unit) : ListAdapter<MenuItem, MenuAdapter.MenuViewHolder>(MenuDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view, onAddClick)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MenuViewHolder(itemView: View, private val onAddClick: (MenuItem) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.itemTitle)
        private val desc: TextView = itemView.findViewById(R.id.itemDescription)
        private val price: TextView = itemView.findViewById(R.id.itemPrice)
        private val image: ImageView = itemView.findViewById(R.id.itemImage)
        private val addBtn: ImageView = itemView.findViewById(R.id.addToCartBtn)

        fun bind(item: MenuItem) {
            title.text = item.name
            desc.text = item.description
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

    class MenuDiffCallback : DiffUtil.ItemCallback<MenuItem>() {
        override fun areItemsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean {
            return oldItem == newItem
        }
    }
}