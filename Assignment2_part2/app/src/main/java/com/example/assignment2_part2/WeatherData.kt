package com.example.assignment2_part2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherData(
    @PrimaryKey val date: String,
    val maxTemp: Float,
    val minTemp: Float
)