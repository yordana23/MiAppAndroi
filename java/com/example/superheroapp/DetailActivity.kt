package com.example.superheroapp

import  android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val heroId = intent.getIntExtra("HERO_ID", -1)
        if (heroId != -1) {
            fetchHeroDetail(heroId)
        }
    }

    private fun fetchHeroDetail(id: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.instance.getHeroDetail(id)
                if (response.isSuccessful && response.body() != null) {
                    val hero = response.body()!!
                    withContext(Dispatchers.Main) {
                        showDetails(hero)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DetailActivity, "Error al obtener detalle", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showDetails(hero: Hero) {
        val imgLarge = findViewById<ImageView>(R.id.imgHeroLarge)
        val tvName = findViewById<TextView>(R.id.tvHeroNameDetail)
        val tvRealName = findViewById<TextView>(R.id.tvRealName)
        val tvPublisher = findViewById<TextView>(R.id.tvPublisher)
        val tvAlignment = findViewById<TextView>(R.id.tvAlignment)
        val tvTotalPower = findViewById<TextView>(R.id.tvTotalPower)
        val tvRank = findViewById<TextView>(R.id.tvRank)
        val tvPlaceOfBirth = findViewById<TextView>(R.id.tvPlaceOfBirth)
        val tvWorkBase = findViewById<TextView>(R.id.tvWorkBase)

        val pbIntelligence = findViewById<ProgressBar>(R.id.pbIntelligence)
        val pbStrength = findViewById<ProgressBar>(R.id.pbStrength)
        val pbSpeed = findViewById<ProgressBar>(R.id.pbSpeed)

        tvName.text = hero.name
        tvRealName.text = "Nombre real: ${hero.biography?.fullName ?: "Desconocido"}"
        tvPublisher.text = "Editorial: ${hero.biography?.publisher ?: "Desconocida"}"

        Glide.with(this)
            .load(hero.images.lg)
            .into(imgLarge)

        val alignment = hero.biography?.alignment
        if (alignment == "good") {
            tvAlignment.text = "HÉROE"
            tvAlignment.setBackgroundColor(Color.parseColor("#4CAF50"))
        } else if (alignment == "bad") {
            tvAlignment.text = "VILLANO"
            tvAlignment.setBackgroundColor(Color.parseColor("#F44336"))
        } else {
            tvAlignment.text = "NEUTRO / OTRO"
            tvAlignment.setBackgroundColor(Color.GRAY)
        }

        val power = hero.powerstats?.power ?: 0
        val combat = hero.powerstats?.combat ?: 0
        val totalPoder = power + combat
        tvTotalPower.text = "Poder Total: $totalPoder pts"

        if (totalPoder > 150) {
            tvRank.text = "Rango: Súper Poderoso"
            tvRank.setTextColor(Color.parseColor("#FF9800")) // Naranja destacado
        } else {
            tvRank.text = "Rango: Estándar"
            tvRank.setTextColor(Color.GRAY)
        }

        val rawBirth = hero.biography?.placeOfBirth
        if (rawBirth.isNullOrEmpty() || rawBirth == "-") {
            tvPlaceOfBirth.text = "Lugar de nacimiento: Lugar desconocido"
        } else {
            tvPlaceOfBirth.text = "Lugar de nacimiento: $rawBirth"
        }

        val rawBase = hero.work?.base
        if (rawBase.isNullOrEmpty() || rawBase == "-") {
            tvWorkBase.text = "Base de operaciones: Sin base fija"
        } else {
            tvWorkBase.text = "Base de operaciones: $rawBase"
        }

        hero.powerstats?.let { stats ->
            pbIntelligence.progress = stats.intelligence
            pbStrength.progress = stats.strength
            pbSpeed.progress = stats.speed
        }
    }
}