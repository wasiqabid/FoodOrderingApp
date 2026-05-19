package com.example.app_loginpage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class OrderHistoryAdapter : ListAdapter<Order, OrderHistoryAdapter.OrderViewHolder>(OrderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order_history, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateText: TextView = itemView.findViewById(R.id.orderDateText)
        private val totalText: TextView = itemView.findViewById(R.id.orderTotalText)
        private val itemsText: TextView = itemView.findViewById(R.id.orderItemsText)

        fun bind(order: Order) {
            val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            dateText.text = order.timestamp?.toDate()?.let { sdf.format(it) } ?: "N/A"
            totalText.text = String.format(Locale.US, "Rs. %.2f", order.totalAmount)
            
            val itemsString = order.items.joinToString("\n") { "${it.quantity} x ${it.name}" }
            itemsText.text = itemsString
        }
    }

    class OrderDiffCallback : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Order, newItem: Order) = oldItem == newItem
    }
}