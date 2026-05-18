package com.example.app_loginpage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class CartActivity : AppCompatActivity() {

    private lateinit var viewModel: CartViewModel
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart)

        viewModel = ViewModelProvider(this).get(CartViewModel::class.java)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cart_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Setup RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.cartRecyclerView)
        val emptyText = findViewById<TextView>(R.id.emptyCartText)
        val placeOrderBtn = findViewById<MaterialButton>(R.id.placeOrderButton)
        
        adapter = CartAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Observe Cart Items
        val badge = findViewById<TextView>(R.id.cartBadge)
        CartRepository.cartItems.observe(this) { items ->
            adapter.submitList(items.toMutableList())
            
            val count = items.sumOf { it.quantity }
            if (count > 0) {
                badge.visibility = View.VISIBLE
                badge.text = count.toString()
            } else {
                badge.visibility = View.GONE
            }

            if (items.isEmpty()) {
                recyclerView.visibility = View.GONE
                placeOrderBtn.visibility = View.GONE
                emptyText.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                placeOrderBtn.visibility = View.VISIBLE
                emptyText.visibility = View.GONE
            }
        }

        // Navigation
        findViewById<ImageView>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP })
        }
        findViewById<ImageView>(R.id.navProfile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.placeOrderButton).setOnClickListener {
            startActivity(Intent(this, OrderPlacementActivity::class.java))
        }
    }
}
