package com.example.assignment2

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.assignment2.ui.theme.Assignment2Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Assignment2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Log.d("WeatherApp", " HI starting")
                    WeatherApp()
                }
            }
        }
    }
}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherApp() {
    var date by remember { mutableStateOf("") }
    val scaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    var weatherResult by remember { mutableStateOf("") } // Mutable state for weather result

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Date (yyyy-MM-dd)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            keyboardActions = KeyboardActions(onNext = {}),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (date.isNotEmpty()) {
                val currentDate = LocalDate.now().toString()
                if (date == currentDate) {
                    coroutineScope.launch {
                        val result = fetchWeatherData2(date)
                        if (result != null) {
                            // Update the weather result text
                            weatherResult =
                                "Max Temp: ${result.first}°C, Min Temp: ${result.second}°C"
                        } else {
                            // Update the error message
                            weatherResult = "Failed to fetch weather data."
                        }
                    }
                }
                if (date != currentDate) {
                    coroutineScope.launch {
                        val result = fetchWeatherData1(date)
                        if (result != null) {
                            // Update the weather result text
                            weatherResult =
                                "Max Temp: ${result.first}°C, Min Temp: ${result.second}°C"
                        } else {
                            // Update the error message
                            weatherResult = "Failed to fetch weather data."
                        }
                    }
                }
            }   else {
                // Update the message to enter a date
                weatherResult = "Please enter a date."
            }
        }) {
            Text("Get Weather")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display the weather result or error message
        TextField(
            value = weatherResult,
            onValueChange = { /* Do nothing, as this is read-only */ },
            label = { Text("Weather Result of Delhi") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

suspend fun fetchWeatherData1(date: String): Pair<Float, Float>? {
    val apiUrl = "https://archive-api.open-meteo.com/v1/archive?latitude=28.6519&longitude=77.2315&start_date=$date&end_date=$date&daily=temperature_2m_max,temperature_2m_min&timezone=Asia%2FTokyo"

    return withContext(Dispatchers.IO) {
        try {
            val response = URL(apiUrl).readText()
            Log.d("WeatherApp", "API Response: $response") // Logging API response
            val jsonObject = JSONObject(response)
            val daily = jsonObject.getJSONObject("daily")
            val maxTempArray = daily.getJSONArray("temperature_2m_max")
            val minTempArray = daily.getJSONArray("temperature_2m_min")

            if (maxTempArray.length() > 0 && minTempArray.length() > 0) {
                val maxTemp = maxTempArray.getDouble(0).toFloat()
                val minTemp = minTempArray.getDouble(0).toFloat()
                Log.d("WeatherApp", "Max Temp: $maxTemp, Min Temp: $minTemp") // Logging max and min temperature
                Pair(maxTemp, minTemp)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("WeatherApp", "Error fetching weather data", e) // Logging error
            null
        }
    }
}


suspend fun fetchWeatherData2(date: String): Pair<Float, Float>? {
    val apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=28.6519&longitude=77.2315&daily=temperature_2m_max,temperature_2m_min"
    val currentDate = LocalDate.now()
    val inputDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE)

    // Check if the user-entered date is today
    val isToday = currentDate == inputDate

    return withContext(Dispatchers.IO) {
        try {
            val response = URL(apiUrl).readText()
            Log.d("WeatherApp", "API Response: $response") // Logging API response
            val jsonObject = JSONObject(response)
            val daily = jsonObject.getJSONObject("daily")

            // Get temperature arrays for the given date

            val maxTempArray = daily.getJSONArray("temperature_2m_max")
            val minTempArray = daily.getJSONArray("temperature_2m_min")
            Log.d("WeatherApp", "Max Temp: $maxTempArray, Min Temp: $minTempArray")

            // Find the index of the input date


            // Check if the input date is found in the response

                val maxTemp = maxTempArray.getDouble(0).toFloat()
                val minTemp = minTempArray.getDouble(0).toFloat()
            Log.d("WeatherApp", "Max Temp: $maxTemp, Min Temp: $minTemp")
            Pair(maxTemp, minTemp)

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("WeatherApp", "Error fetching weather data", e) // Logging error
            null
        }
    }
}


