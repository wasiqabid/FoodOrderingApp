package com.example.app_loginpage

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import java.util.Locale

class OrderPlacementActivity : AppCompatActivity() {

    private lateinit var adapter: OrderSummaryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_order_placement)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.order_placement_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<ImageView>(R.id.backBtn).setOnClickListener { finish() }

        val recyclerView = findViewById<RecyclerView>(R.id.summaryRecyclerView)
        adapter = OrderSummaryAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val totalText = findViewById<TextView>(R.id.totalAmountText)

        CartRepository.cartItems.observe(this) { items ->
            adapter.submitList(items.toMutableList())
            val total = items.sumOf { it.price * it.quantity }
            totalText.text = String.format(Locale.US, "Rs. %.2f", total)
        }

        findViewById<MaterialButton>(R.id.confirmOrderButton).setOnClickListener {
            saveOrderToFirestore()
        }
    }

    private fun saveOrderToFirestore() {
        val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
        val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
        val userId = auth.currentUser?.uid ?: return
        val items = CartRepository.cartItems.value ?: return
        val total = items.sumOf { it.price * it.quantity }

        val orderData = hashMapOf(
            "userId" to userId,
            "totalAmount" to total,
            "timestamp" to com.google.firebase.Timestamp.now(),
            "items" to items
        )

        db.collection("orders")
            .add(orderData)
            .addOnSuccessListener {
                Toast.makeText(this, "Order Confirmed! Thank you for ordering from feasto.", Toast.LENGTH_LONG).show()
                CartRepository.clearCart()
                val intent = Intent(this, MenuActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to place order. Please try again.", Toast.LENGTH_SHORT).show()
            }
    }
}