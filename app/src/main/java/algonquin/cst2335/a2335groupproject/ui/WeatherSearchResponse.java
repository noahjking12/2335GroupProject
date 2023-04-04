package algonquin.cst2335.a2335groupproject.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.a2335groupproject.Forecast;
import algonquin.cst2335.a2335groupproject.ForecastDAO;
import algonquin.cst2335.a2335groupproject.ForecastDatabase;
import algonquin.cst2335.a2335groupproject.NasaPhotos;
import algonquin.cst2335.a2335groupproject.NewYorkTimes;
import algonquin.cst2335.a2335groupproject.R;
import algonquin.cst2335.a2335groupproject.SecondActivity;
import algonquin.cst2335.a2335groupproject.data.WeatherActivityViewModel;
import algonquin.cst2335.a2335groupproject.databinding.ActivityWeatherBinding;
import algonquin.cst2335.a2335groupproject.databinding.ActivityWeatherSearchResponseBinding;

/** Weather search results page
 * @author Noah King
 * @version 1.0
 */
public class WeatherSearchResponse extends AppCompatActivity {

    /** Variable binding for weather search results page */
    ActivityWeatherSearchResponseBinding binding;

    /** ViewModel for saving WeatherActivity's data */
    WeatherActivityViewModel weatherModel;

    /** List of users saved forecasts */
    ArrayList<Forecast> savedForecasts;

    /** Access key for WeatherStack API */
    private final String API_KEY = "96c90f930cc4a93a156d3f749133bcd8";

    /** Volley request queue for api requests */
    RequestQueue queue;

    /** The weather icon as a bitmap */
    Bitmap image;

    /** The retrieved forecast */
    Forecast searchedForecast;

    /** DAO for interacting with the Forecast database */
    ForecastDAO forecastDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get a DAO for the Forecast database
        ForecastDatabase db = Room.databaseBuilder(getApplicationContext(), ForecastDatabase.class, "weatherstack").build();
        forecastDAO = db.forecastDAO();

        binding = ActivityWeatherSearchResponseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModelProvider and savedForecasts ArrayList
        weatherModel = new ViewModelProvider(this).get(WeatherActivityViewModel.class);
        savedForecasts = weatherModel.savedForecasts.getValue();

        // If savedForecasts has never been set, post to it an empty ArrayList
        if (savedForecasts == null) {
            weatherModel.savedForecasts.postValue( savedForecasts = new ArrayList<Forecast>());

            // Load all saved Forecasts
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                savedForecasts.addAll(forecastDAO.getAllForecasts()); // Get all Forecasts from Forecast database
            });
        }

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
                                            binding.cityWeatherIcon.setVisibility(View.VISIBLE);
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {
                                    // Do nothing, display without icon
                                });
                                queue.add(imageReq); // Add the image request to the queue
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // Get strings from res/string
                        String resFeelsLike = getResources().getString(R.string.feels_like);
                        String resHumidity = getResources().getString(R.string.humidity);
                        String resUvIndex = getResources().getString(R.string.uv_index);
                        String resWindSpeed = getResources().getString(R.string.wind);
                        String resVisibility = getResources().getString(R.string.visibility);

                        // Display the data
                        runOnUiThread(() -> {
                            binding.cityName.setText(cityName);
                            binding.cityName.setVisibility(View.VISIBLE);

                            binding.cityTemp.setText(Integer.toString(temperature));
                            binding.cityTemp.setVisibility(View.VISIBLE);

                            binding.cityWeatherIcon.setImageBitmap(image);
                            binding.cityWeatherIcon.setVisibility(View.VISIBLE);

                            binding.cityWeatherDesc.setText(description);
                            binding.cityWeatherDesc.setVisibility(View.VISIBLE);

                            binding.cityFeelsLike.setText(resFeelsLike + Integer.toString(feelsLike));
                            binding.cityFeelsLike.setVisibility(View.VISIBLE);

                            binding.cityHumidity.setText(resHumidity + Integer.toString(humidity) + "%");
                            binding.cityHumidity.setVisibility(View.VISIBLE);

                            binding.cityUvIndex.setText(resUvIndex + Integer.toString(uvIndex));
                            binding.cityUvIndex.setVisibility(View.VISIBLE);

                            binding.cityWindSpeed.setText(resWindSpeed + Integer.toString(windSpeed) + "km/h");
                            binding.cityWindSpeed.setVisibility(View.VISIBLE);

                            binding.cityVisibility.setText(resVisibility + Integer.toString(visibility));
                            binding.cityVisibility.setVisibility(View.VISIBLE);

                        });

                        // Get current date
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                        String date = sdf.format(new Date());

                        // Construct the Forecast object using retrieved data
                        searchedForecast = new Forecast(cityName, date, icon, description, temperature, feelsLike, humidity, uvIndex, windSpeed, visibility);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                (error) -> {
                    // Get string from res/string
                    String forecastError = getResources().getString(R.string.forecast_error);

                    // Show a Toast describing the error
                    Toast.makeText(WeatherSearchResponse.this, forecastError + error, Toast.LENGTH_SHORT).show();
                });
        queue.add(request); // Add request to the queue

        // Activate save forecast button
        binding.saveForecastBtn.setOnClickListener(clk -> {

            savedForecasts.add(searchedForecast); // Add the Forecast to savedForecasts

            // Save the Forecast in the database
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                long id = forecastDAO.insertForecast(searchedForecast);
                searchedForecast.id = id; // Database tells us what the id is
            });

            // Send user back to WeatherActivity
            Intent weatherPage = new Intent(WeatherSearchResponse.this, WeatherActivity.class);
            weatherPage.putExtra("SavedAForecast", true); // Tells WeatherActivity that a Forecast was saved
            startActivity(weatherPage);

        });

        setSupportActionBar(binding.weatherResponseToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.weather_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch( item.getItemId() )
        {
            case R.id.weatherHelp:

                // Get strings from res/string
                String weatherInstructions = getResources().getString(R.string.weather_response_instructions);
                String weatherHelpTitle = getResources().getString(R.string.weather_help);

                // Display AlertDialog providing instructions on how to use this activity
                AlertDialog.Builder builder = new AlertDialog.Builder( WeatherSearchResponse.this );
                builder.setMessage(weatherInstructions)
                        .setTitle(weatherHelpTitle)
                        .create()
                        .show();

                break;

            case R.id.kitten_bar_btn:
                // Send user to Kitten Images app
                Intent kittenPage = new Intent(WeatherSearchResponse.this, SecondActivity.class);
                startActivity( kittenPage );

                break;

            case R.id.nyt_bar_btn:
                // Send user to New York Times app
                Intent nytPage = new Intent(WeatherSearchResponse.this, NewYorkTimes.class);
                startActivity( nytPage );

                break;

            case R.id.nasa_bar_btn:
                // Send user to Nasa photos app
                Intent nasaPage = new Intent(WeatherSearchResponse.this, NasaPhotos.class);
                startActivity( nasaPage );

                break;
        }

        return true;
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