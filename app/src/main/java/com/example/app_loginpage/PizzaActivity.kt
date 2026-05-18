package com.example.app_loginpage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PizzaActivity : AppCompatActivity() {

    private lateinit var viewModel: PizzaViewModel
    private lateinit var adapter: MenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pizza)

        viewModel = ViewModelProvider(this).get(PizzaViewModel::class.java)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.pizza_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<ImageView>(R.id.backBtn).setOnClickListener { finish() }

        // RecyclerView Setup
        val recyclerView = findViewById<RecyclerView>(R.id.pizzaRecyclerView)
        adapter = MenuAdapter { item ->
            CartRepository.addItem(CartItem(
                id = item.id,
                name = item.name,
                description = item.description,
                price = item.price,
                quantity = 1,
                imageUrl = item.imageUrl
            ))
            Toast.makeText(this, "Added ${item.name} to cart", Toast.LENGTH_SHORT).show()
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Observe Pizza
        viewModel.pizzas.observe(this) { items ->
            if (items.isEmpty()) {
                Toast.makeText(this, "No pizzas found in database. Check category spelling!", Toast.LENGTH_LONG).show()
            }
            val searchQuery = intent.getStringExtra("SEARCH_QUERY")
            if (searchQuery != null) {
                adapter.submitList(items.filter { it.name.lowercase().contains(searchQuery.lowercase()) })
            } else {
                adapter.submitList(items)
            }
        }

        // Navigation
        findViewById<ImageView>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP })
        }
        findViewById<ImageView>(R.id.navCart).setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
        findViewById<ImageView>(R.id.navProfile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // Cart Badge Observation
        val badge = findViewById<TextView>(R.id.cartBadge)
        CartRepository.cartItems.observe(this) { items ->
            val count = items.sumOf { it.quantity }
            if (count > 0) {
                badge.visibility = View.VISIBLE
                badge.text = count.toString()
            } else {
                badge.visibility = View.GONE
            }
        }
    }
}