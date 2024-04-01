package com.example.assignment2_part2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.example.assignment2_part2.ui.theme.Assignment2_part2Theme

class MainActivity : ComponentActivity() {
    private lateinit var weatherDao: WeatherDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(
            applicationContext,
            WeatherDatabase::class.java, "weather-database"
        ).allowMainThreadQueries() // For debugging purposes only
            .build()

        weatherDao = database.weatherDao()

        setContent {
            Assignment2_part2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherApp(weatherDao)
                }
            }
        }
    }
}

