package personal_projects;
// Interface for displaying weather data based upon the city and the json response.
public interface WeatherDataDisplay {
    void displayWeatherData(String city, String jsonResponse);
}