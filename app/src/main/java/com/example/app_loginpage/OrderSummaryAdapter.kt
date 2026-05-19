package com.example.app_loginpage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class OrderSummaryAdapter : ListAdapter<CartItem, OrderSummaryAdapter.SummaryViewHolder>(CartAdapter.CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_summary, parent, false)
        return SummaryViewHolder(view)
    }

    override fun onBindViewHolder(holder: SummaryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SummaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val qtyName: TextView = itemView.findViewById(R.id.itemQtyName)
        private val price: TextView = itemView.findViewById(R.id.itemPrice)

        fun bind(item: CartItem) {
            qtyName.text = "${item.quantity} x ${item.name}"
            val totalPrice = item.quantity * item.price
            price.text = String.format(Locale.US, "Rs. %.2f", totalPrice)
        }
    }
}