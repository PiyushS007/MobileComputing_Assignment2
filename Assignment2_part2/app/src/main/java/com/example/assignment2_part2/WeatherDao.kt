package com.example.assignment2_part2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherData(weatherData: WeatherData)

    @Query("SELECT * FROM weather WHERE date = :date")
    suspend fun getWeatherData(date: String): WeatherData?

    @Query("SELECT * FROM Weather WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getWeatherDataBetweenDates(startDate: String, endDate: String): List<WeatherData>

}