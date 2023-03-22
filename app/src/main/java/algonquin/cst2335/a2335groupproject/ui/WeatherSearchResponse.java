package algonquin.cst2335.a2335groupproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import algonquin.cst2335.a2335groupproject.Forecast;
import algonquin.cst2335.a2335groupproject.databinding.ActivityWeatherBinding;
import algonquin.cst2335.a2335groupproject.databinding.ActivityWeatherSearchResponseBinding;

/** Weather search results page
 * @author Noah King
 * @version 1.0
 */
public class WeatherSearchResponse extends AppCompatActivity {

    /** Variable binding for weather search results page */
    ActivityWeatherSearchResponseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWeatherSearchResponseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get searched city from previous activity
        String searchedCity = getIntent().getStringExtra("cityName");

        // ***********************************
        // DISPLAYING DUMMY DATA
        Forecast searchedForecast = new Forecast("Canada", searchedCity, "Dec. 25th, 2023", "Rainy", 1, 1, 1, 1, 1, 10);
        displayForecast(searchedForecast);
        // *************************************

        // Activate save forecast button
        binding.saveForecastBtn.setOnClickListener(clk -> {
            // Show a Toast stating that the forecast was successfully saved
            Context context = getApplicationContext();
            CharSequence text = "Forecast for " + searchedCity + " successfully saved!";
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(context, text, duration).show();
        });
    }

    /** Display a given forecast on screen
     * @param forecast The forecast to display
     */
    private void displayForecast(Forecast forecast) {
        binding.cityName.setText(forecast.getCity());
        binding.cityTemp.setText(Integer.toString(forecast.getTemperature()));
        binding.cityWeatherDesc.setText(forecast.getDescription());
        binding.cityFeelsLike.setText("Feels like: " + Integer.toString(forecast.getFeelsLike()));
        binding.cityHumidity.setText("Humidity: " + Integer.toString(forecast.getHumidity()) + "%");
        binding.cityUvIndex.setText("UV index: " + Integer.toString(forecast.getUvIndex()));
        binding.cityWindSpeed.setText("Wind speed: " + Integer.toString(forecast.getWindSpeed()) + "km/h");
        binding.cityVisibility.setText("Visibility: " +Integer.toString(forecast.getVisibility()));
    }
}