# Part2

# Weather App
This application fetches weather data from an external API and allows users to retrieve weather information for a specific date in Delhi, India with help of local database.

# Usage
- Click on the "Save Weather Data from API" button to fetch and save weather data from the API to local Database.
- Enter the desired date in the format "yyyy-MM-dd" in the provided text field.
- Click on the "Get Weather" button to retrieve weather information for the entered date.
- The maximum and minimum temperature for the specified date will be displayed in the read-only text field labeled "Weather Result of Delhi".
  
# Dependencies
- Compose Material3: Used for UI components and styling.
- Kotlinx Coroutines: Used for asynchronous programming.
- org.json: Used for JSON parsing.
- java.net.URL: Used for making HTTP requests.
  
# Functions
- WeatherApp: Composable function responsible for rendering the UI and handling user interactions.
- fetchAndSaveWeatherData: Suspended function to fetch weather data from the external API and save it to a local database.
- getAverageWeatherData: Suspended function to calculate the average maximum and minimum temperatures for a period of ten days preceding the current date.
