package com.example.app_loginpage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import java.util.Locale

class MenuActivity : AppCompatActivity() {

    private lateinit var viewModel: MenuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Ensure Cart Persistence is initialized
        CartRepository.init(this)

        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)

        viewModel = ViewModelProvider(this).get(MenuViewModel::class.java)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.menu_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Popular Section Dynamic Setup
        val popularLayout = findViewById<View>(R.id.popularItemsLayout)
        viewModel.popularItems.observe(this) { items ->
            val beefBurger = items.find { it.name.lowercase().contains("beef burger") }
            val pepperoniPizza = items.find { it.name.lowercase().contains("pepperoni passion") }

            if (beefBurger != null || pepperoniPizza != null) {
                popularLayout.visibility = View.VISIBLE
            }

            beefBurger?.let { item ->
                findViewById<TextView>(R.id.popularBurgerName).text = item.name
                findViewById<TextView>(R.id.popularBurgerPrice).text = String.format(Locale.US, "Rs. %.0f", item.price)
                Glide.with(this).load(item.imageUrl).into(findViewById(R.id.popularBurgerImage))
                findViewById<ImageView>(R.id.addBurger).setOnClickListener {
                    CartRepository.addItem(CartItem(item.id, item.name, item.description, item.price, 1, 0, item.imageUrl))
                    Toast.makeText(this, "Added ${item.name} to cart", Toast.LENGTH_SHORT).show()
                }
            }

            pepperoniPizza?.let { item ->
                findViewById<TextView>(R.id.popularPizzaName).text = item.name
                findViewById<TextView>(R.id.popularPizzaPrice).text = String.format(Locale.US, "Rs. %.0f", item.price)
                Glide.with(this).load(item.imageUrl).into(findViewById(R.id.popularPizzaImage))
                findViewById<ImageView>(R.id.addPizza).setOnClickListener {
                    CartRepository.addItem(CartItem(item.id, item.name, item.description, item.price, 1, 0, item.imageUrl))
                    Toast.makeText(this, "Added ${item.name} to cart", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Search functionality
        val searchBar = findViewById<EditText>(R.id.searchBar)
        searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                val query = searchBar.text.toString().lowercase().trim()
                performSearch(query, searchBar)
                true
            } else {
                false
            }
        }

        // Category Navigation
        findViewById<View>(R.id.categoryBurgers).setOnClickListener {
            startActivity(Intent(this, BurgersActivity::class.java))
        }
        findViewById<View>(R.id.categoryPizza).setOnClickListener {
            startActivity(Intent(this, PizzaActivity::class.java))
        }
        findViewById<View>(R.id.categoryDesserts).setOnClickListener {
            startActivity(Intent(this, DessertsActivity::class.java))
        }

        // Bottom Nav Navigation
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

    private fun performSearch(query: String, searchBar: EditText) {
        if (query.isEmpty()) return

        com.google.firebase.firestore.FirebaseFirestore.getInstance().collection("items")
            .get()
            .addOnSuccessListener { documents ->
                val allItems = documents.toObjects(MenuItem::class.java)
                val matchedItem = allItems.find { it.name.lowercase().contains(query) }

                if (matchedItem != null) {
                    val cat = matchedItem.category.lowercase().trim()
                    val intent = when {
                        cat.contains("burger") -> Intent(this, BurgersActivity::class.java)
                        cat.contains("pizza") -> Intent(this, PizzaActivity::class.java)
                        cat.contains("dessert") -> Intent(this, DessertsActivity::class.java)
                        else -> null
                    }
                    
                    if (intent != null) {
                        intent.putExtra("SEARCH_QUERY", matchedItem.name)
                        startActivity(intent)
                    } else {
                        searchBar.setText("")
                        searchBar.hint = "No results found"
                        searchBar.postDelayed({ searchBar.hint = "Search" }, 2000)
                    }
                } else {
                    searchBar.setText("")
                    searchBar.hint = "No results found"
                    searchBar.postDelayed({
                        searchBar.hint = "Search"
                    }, 2000)
                }
            }
            .addOnFailureListener {
                searchBar.setText("")
                searchBar.hint = "Connection Error"
                searchBar.postDelayed({ searchBar.hint = "Search" }, 2000)
            }
    }
}