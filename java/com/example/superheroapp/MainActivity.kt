package com.example.superheroapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: HeroAdapter
    private var originalList: List<Hero> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val searchView = findViewById<SearchView>(R.id.searchView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = HeroAdapter(emptyList()) { selectedHero ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("HERO_ID", selectedHero.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        fetchHeroes()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                val query = newText.orEmpty().lowercase()
                val filteredList = originalList.filter { it.name.lowercase().contains(query) }
                adapter.updateList(filteredList)
                return true
            }
        })
    }

    private fun fetchHeroes() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.instance.getAllHeroes()
                if (response.isSuccessful && response.body() != null) {
                    originalList = response.body()!!
                    withContext(Dispatchers.Main) {
                        adapter.updateList(originalList)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error de red al cargar la lista", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}