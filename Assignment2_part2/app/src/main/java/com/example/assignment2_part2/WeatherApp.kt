package com.example.assignment2_part2

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeParseException

// WeatherApp.kt
@Composable
fun WeatherApp(weatherDao: WeatherDao) {
    var date by remember { mutableStateOf("") }
    var weatherResult by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            coroutineScope.launch {
                fetchAndSaveWeatherData(weatherDao)
            }
        }) {
            Text("Save Weather Data from API")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Date (yyyy-MM-dd)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (date.isNotEmpty()) {
                val isValidDate = try {
                    LocalDate.parse(date)
                    true
                } catch (e: DateTimeParseException) {
                    false
                }
                if (isValidDate) {
                    coroutineScope.launch {
                        val today = LocalDate.now()
                        val inputDate = LocalDate.parse(date)

                        if (inputDate.isAfter(today) || inputDate.isEqual(today)) {
                            val avt = getAverageWeatherData(weatherDao)
                            if(avt!=null)
                                weatherResult = "Average Max Temp: ${avt.first}°C, Average Min Temp:${avt.second}°C"
                        } else {
                            val weatherData = weatherDao.getWeatherData(date)
                            if (weatherData != null) {
                                weatherResult = "Max Temp: ${weatherData.maxTemp}°C, Min Temp: ${weatherData.minTemp}°C"
                            } else {
                                weatherResult = "Weather data not found for this date."
                            }
                        }
                    }
                } else {
                    weatherResult = "Please enter a valid date."
                }
            } else {
                weatherResult = "Please enter a date."
            }
        }) {
            Text("Get Weather")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = weatherResult,
            onValueChange = { /* Do nothing, as this is read-only */ },
            label = { Text("Weather Result of Delhi") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


suspend fun fetchAndSaveWeatherData(weatherDao: WeatherDao) {
    val apiUrl = "https://archive-api.open-meteo.com/v1/archive?latitude=28.6519&longitude=77.2315&start_date=2024-03-15&end_date=2024-03-31&daily=temperature_2m_max,temperature_2m_min&timezone=Asia%2FTokyo"

    withContext(Dispatchers.IO) {
        try {
            val response = URL(apiUrl).readText()
            val jsonObject = JSONObject(response)
            val daily = jsonObject.getJSONObject("daily")
            val timeArray = daily.getJSONArray("time")
            val maxTempArray = daily.getJSONArray("temperature_2m_max")
            val minTempArray = daily.getJSONArray("temperature_2m_min")

            // Iterate through time array and get the corresponding temperature data
            for (i in 0 until timeArray.length()) {
                val currentDate = timeArray.getString(i)
                val maxTemp = maxTempArray.getDouble(i).toFloat()
                val minTemp = minTempArray.getDouble(i).toFloat()

                // Insert data into database
                val weatherData = WeatherData(currentDate, maxTemp, minTemp)
                weatherDao.insertWeatherData(weatherData)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}



suspend fun getAverageWeatherData(weatherDao: WeatherDao): Pair<Float, Float>? {
    val today = LocalDate.now()
    val startDate = today.minusDays(10)
    val endDate = today.minusDays(1)

    val weatherDataList = weatherDao.getWeatherDataBetweenDates(startDate.toString(), endDate.toString())
    var totalMaxTemp = 0f
    var totalMinTemp = 0f

    for (weatherData in weatherDataList) {
        totalMaxTemp += weatherData.maxTemp
        totalMinTemp += weatherData.minTemp
    }

    val averageMaxTemp = totalMaxTemp / weatherDataList.size
    val averageMinTemp = totalMinTemp / weatherDataList.size

    return Pair(averageMaxTemp, averageMinTemp)
}