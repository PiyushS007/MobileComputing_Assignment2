# Part1 
# Weather App

This Android application allows users to fetch weather data for a specific date in Delhi, India. Users can enter a date in the format "yyyy-MM-dd" and retrieve the maximum and minimum temperatures for that day.

## Features

- Input field to enter the date
- Button to fetch weather data
- Display of maximum and minimum temperatures for the entered date

# functions used:-

# MainActivity
onCreate(savedInstanceState: Bundle?): This is a lifecycle method of the main activity in Android. It's called when the activity is starting. In this method, the content of the activity is set using setContent, which inflates the layout defined in the WeatherApp composable function.

# WeatherApp
@Composable fun WeatherApp(): This is a composable function that defines the UI of the weather application using Jetpack Compose. It consists of an input field to enter the date, a button to fetch weather data, and a display field to show the weather result or error message.

# fetchWeatherData1
suspend fun fetchWeatherData1(date: String): Pair<Float, Float>?: This is a suspending function used to fetch weather data from an API. It takes a date parameter in the format "yyyy-MM-dd", constructs a URL with the date parameter, and makes a network call using URL().readText(). It then parses the JSON response to extract the maximum and minimum temperatures for the given date.

# fetchWeatherData2
suspend fun fetchWeatherData2(date: String): Pair<Float, Float>?: Similar to fetchWeatherData1, this is another suspending function used to fetch weather data. However, it fetches data from a different API endpoint. It also considers whether the date provided is today's date or not and processes the response accordingly.


# Usage
- Run the application on an Android device or emulator.
- Enter a date in the input field.
- Tap the "Get Weather" button to fetch weather data.
- View the maximum and minimum temperatures for the entered date.
