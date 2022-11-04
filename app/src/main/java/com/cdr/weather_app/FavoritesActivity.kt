package com.cdr.weather_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cdr.weather_app.databinding.ActivityFavoritesBinding

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.backButton.setOnClickListener { finish() }

    }

}