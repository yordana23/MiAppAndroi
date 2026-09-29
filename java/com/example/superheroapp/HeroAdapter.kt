package com.example.superheroapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class HeroAdapter(
    private var heroList: List<Hero>,
    private val onClick: (Hero) -> Unit
) : RecyclerView.Adapter<HeroAdapter.HeroViewHolder>() {

    class HeroViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgHero: ImageView = view.findViewById(R.id.imgHero)
        val tvHeroName: TextView = view.findViewById(R.id.tvHeroName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hero, parent, false)
        return HeroViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        val hero = heroList[position]
        holder.tvHeroName.text = hero.name

        Glide.with(holder.itemView.context)
            .load(hero.images.sm)
            .into(holder.imgHero)

        holder.itemView.setOnClickListener { onClick(hero) }
    }

    override fun getItemCount(): Int = heroList.size

    fun updateList(newList: List<Hero>) {
        heroList = newList
        notifyDataSetChanged()
    }
}