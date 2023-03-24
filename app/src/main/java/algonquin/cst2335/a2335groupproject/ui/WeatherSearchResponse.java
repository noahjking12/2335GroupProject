package algonquin.cst2335.a2335groupproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    /** Access key for WeatherStack API */
    private final String API_KEY = "96c90f930cc4a93a156d3f749133bcd8";

    /** Volley request queue for api requests */
    RequestQueue queue;

    /** The weather icon as a bitmap */
    Bitmap image;

    /** The retrieved forecast */
    Forecast searchedForecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWeatherSearchResponseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        queue = Volley.newRequestQueue(this); // Establish RequestQueue

        // Get searched city from previous activity
        String cityName = getIntent().getStringExtra("cityName");

        String url = null; // url for get request

        try {
            url = "http://api.weatherstack.com/current?access_key="
                    + API_KEY
                    + "&query="
                    + URLEncoder.encode(cityName, "UTF-8")
                    + "&units=m";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Create json request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                (response) -> {
                    try {
                        // Get country of forecast
                        JSONObject location = response.getJSONObject("location");
                        String country = location.getString("country");

                        JSONObject currentForecast = response.getJSONObject("current"); // Current forecast data

                        // Get all require Forecast data
                        String iconUrl = currentForecast.getJSONArray("weather_icons").getString(0); // Url of icon
                        String icon = parseIconName(iconUrl); // File name of icon

                        String description = currentForecast.getJSONArray("weather_descriptions").getString(0);
                        int temperature = currentForecast.getInt("temperature");
                        int feelsLike = currentForecast.getInt("feelslike");
                        int humidity = currentForecast.getInt("humidity");
                        int uvIndex = currentForecast.getInt("uv_index");
                        int windSpeed = currentForecast.getInt("wind_speed");
                        int visibility = currentForecast.getInt("visibility");

                        try {
                            // Check if this icon has already been downloaded, if so just load it
                            String pathname = getFilesDir() + "/" + icon;
                            File file = new File(pathname);
                            if (file.exists()) {
                                image = BitmapFactory.decodeFile(pathname);
                            } else {
                                // Request the icon
                                ImageRequest imageReq = new ImageRequest(iconUrl, new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap response) {
                                        image = response; // Set image to the bitmap retrieved
                                        try {
                                            // Store the image locally
                                            image.compress(Bitmap.CompressFormat.PNG, 100, WeatherSearchResponse.this.openFileOutput(icon, Activity.MODE_PRIVATE));
                                            binding.cityWeatherIcon.setImageBitmap(image); // Display on screen
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {
                                    // Show a Toast explaining the error
                                    Toast.makeText(WeatherSearchResponse.this, "Could not load weather icon: " + error, Toast.LENGTH_SHORT).show();
                                });
                                queue.add(imageReq); // Add the image request to the queue
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // Display the data
                        runOnUiThread(() -> {
                            binding.cityName.setText(cityName);
                            binding.cityTemp.setText(Integer.toString(temperature));
                            binding.cityWeatherIcon.setImageBitmap(image);
                            binding.cityWeatherDesc.setText(description);
                            binding.cityFeelsLike.setText("Feels like: " + Integer.toString(feelsLike));
                            binding.cityHumidity.setText("Humidity: " + Integer.toString(humidity) + "%");
                            binding.cityUvIndex.setText("UV index: " + Integer.toString(uvIndex));
                            binding.cityWindSpeed.setText("Wind speed: " + Integer.toString(windSpeed) + "km/h");
                            binding.cityVisibility.setText("Visibility: " +Integer.toString(visibility));
                        });

                        // Get current date
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                        String date = sdf.format(new Date());

                        // Construct the Forecast object using retrieved data
                        searchedForecast = new Forecast(country, cityName, date, icon, description, temperature, feelsLike, humidity, uvIndex, windSpeed, visibility);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                (error) -> {
                    // Show a Toast describing the error
                    Toast.makeText(WeatherSearchResponse.this, "Failed to retrieve Forecast: " + error, Toast.LENGTH_SHORT).show();
                });
        queue.add(request); // Add request to the queue

        // Activate save forecast button
        binding.saveForecastBtn.setOnClickListener(clk -> {
            // Show a Toast stating that the forecast was successfully saved
            Context context = getApplicationContext();
            CharSequence text = "Forecast for " + cityName + " successfully saved!";
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(context, text, duration).show();
        });
    }

    /** Helper method that parses the file name of the icon from the url the api provides
     * @param iconUrl The url of the given icon
     * @return The icons name
     */
    private String parseIconName(String iconUrl) {
        String[] splitStr = iconUrl.split("/");
        int len = splitStr.length;

        return splitStr[len - 1];
    }
}