package personal_projects;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Weatherr implements WeatherDataDisplay {

	// API key and base URL for OpenWeatherMap API
    private static final String API_KEY = "0a224190e053b98f4b218a6ac0a8d6f9";
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Weatherr weatherApp = new Weatherr();
        
        //User input for the two city names they want
        System.out.println("Welcome to the weather infomation script. Please enter the");
        System.out.println("names of the two cities you wish to know the weather of.");
        System.out.println("Example; Tokyo and New York");
        String city1 = weatherApp.getUserInput("Please enter the first city name: ");
        String city2 = weatherApp.getUserInput("Please enter the second city name: ");

        // displays the weather data for the two city user selected
        displayWeatherDataForCities(weatherApp, city1, city2); // display weather data for the two city the user selected
    }
    // displays weather data for two cities in the table
    private static void displayWeatherDataForCities(Weatherr weatherApp, String city1, String city2) { // Create a user friendly table that displays the data  
        try {
        	
        	// gets weather data for both cities the user selected
            String response1 = weatherApp.fetchDataFromApi(weatherApp.buildApiUrl(city1));
            String response2 = weatherApp.fetchDataFromApi(weatherApp.buildApiUrl(city2));

            // table header
            System.out.println("+----------------+-------------------------+----------------+");
            System.out.println("|      City      |         Weather         |  Temperature   |");
            System.out.println("+----------------+-------------------------+----------------+");
            // displays the weather data for each city in the table
            weatherApp.displayWeatherData(city1, response1);
            weatherApp.displayWeatherData(city2, response2);
            System.out.println("+----------------+-------------------------+----------------+"); // end of the table
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // all of the JSON responses 
    public void displayWeatherData(String city, String jsonResponse) {
        try {
            JsonElement jelement = JsonParser.parseString(jsonResponse);
            JsonObject jsonObject = jelement.getAsJsonObject();
            JsonArray weatherArray = jsonObject.getAsJsonArray("weather");
            JsonObject weather = weatherArray.get(0).getAsJsonObject();
            String weatherDescription = weather.get("description").getAsString();
            double temperature = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();

            System.out.printf("| %-14s | %-23s | %-13.2f°C |%n", city, weatherDescription, temperature);
        } catch (Exception e) {
            System.out.printf("| %-14s | Error parsing data       |               |%n", city);
        }
    }

    private String getUserInput(String prompt) {// method use to get user input
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private String buildApiUrl(String city) { // method use to build the api url with the city name and the api key
        return String.format("%s?q=%s&appid=%s&units=metric", BASE_URL, city, API_KEY);
    }

    private String fetchDataFromApi(String apiUrl) throws Exception {// fetches the weather data from the api
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            }
        } else {
            System.out.println("Failed to fetch weather data. Please try again in a few minutes or later. Response Code: " + responseCode);
            return null;
        }
    }
}
