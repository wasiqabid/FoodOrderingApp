package com.example.app_loginpage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide

class CartAdapter : ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.itemName)
        private val desc: TextView = itemView.findViewById(R.id.itemDesc)
        private val qty: TextView = itemView.findViewById(R.id.itemQty)
        private val image: ImageView = itemView.findViewById(R.id.itemImage)
        private val minus: ImageView = itemView.findViewById(R.id.minusBtn)
        private val plus: ImageView = itemView.findViewById(R.id.plusBtn)

        fun bind(item: CartItem) {
            name.text = item.name
            desc.text = item.description
            qty.text = item.quantity.toString()
            
            if (item.imageUrl.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(item.imageUrl)
                    .centerCrop()
                    .into(image)
            } else {
                image.setImageResource(item.imageResId)
            }

            minus.setOnClickListener {
                CartRepository.removeItem(item.id)
            }
            plus.setOnClickListener {
                CartRepository.addItem(item)
            }
        }
    }

    class CartDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }
}