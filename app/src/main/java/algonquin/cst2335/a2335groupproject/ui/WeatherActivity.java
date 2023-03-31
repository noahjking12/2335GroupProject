package algonquin.cst2335.a2335groupproject.ui;

import androidx.annotation.NonNull;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

import algonquin.cst2335.a2335groupproject.Forecast;
import algonquin.cst2335.a2335groupproject.ForecastDetailsFragment;
import algonquin.cst2335.a2335groupproject.MainActivity;
import algonquin.cst2335.a2335groupproject.NasaPhotos;
import algonquin.cst2335.a2335groupproject.NewYorkTimes;
import algonquin.cst2335.a2335groupproject.R;
import algonquin.cst2335.a2335groupproject.SecondActivity;
import algonquin.cst2335.a2335groupproject.data.WeatherActivityViewModel;
import algonquin.cst2335.a2335groupproject.databinding.ActivityWeatherBinding;
import algonquin.cst2335.a2335groupproject.databinding.SavedForecastBinding;

/** Weather app front page
 * @author Noah King
 * @version 1.0
 */
public class WeatherActivity extends AppCompatActivity {

    /** Variable binding for Weather activity */
    ActivityWeatherBinding binding;

    /** Adapter for saved forecast recycler view */
    RecyclerView.Adapter myAdapter;

    /** ViewModel for saving WeatherActivity's data */
    WeatherActivityViewModel weatherModel;

    /** List of users saved forecasts */
    ArrayList<Forecast> savedForecasts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWeatherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModelProvider and savedForecasts ArrayList
        weatherModel = new ViewModelProvider(this).get(WeatherActivityViewModel.class);
        savedForecasts = weatherModel.savedForecasts.getValue();

        // If savedForecasts has never been set, post to it an empty ArrayList
        if (savedForecasts == null) {
            weatherModel.savedForecasts.postValue( savedForecasts = new ArrayList<Forecast>());
        }

        // Specify a single column scrolling vertically for saved forecasts
        binding.forecastsRecycleView.setLayoutManager(new LinearLayoutManager(this));

        binding.forecastsRecycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Create layout for a saved forecast row
                SavedForecastBinding binding = SavedForecastBinding.inflate(getLayoutInflater(), parent, false);
                return new MyRowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                // Set values for objects in saved forecast row
                Forecast savedForecast = savedForecasts.get(position);
                holder.forecastCity.setText(savedForecast.getCity());
                holder.forecastDate.setText(savedForecast.getDate());
                holder.forecastTemp.setText(Integer.toString(savedForecast.getTemperature()));

                // Load icon from files and display it
                String pathname = getFilesDir() + "/" + savedForecast.getIcon();
                File file = new File(pathname);

                if (file.exists()) {
                    Bitmap forecastIcon = BitmapFactory.decodeFile(pathname);
                    holder.forecastIcon.setImageBitmap(forecastIcon);
                } // If can't find icon, display without

            }

            @Override
            public int getItemCount() {
                return savedForecasts.size();
            }

        });

        // Load city search bar with the last city search by the user
        SharedPreferences prefs = getSharedPreferences("MyWeatherPrefs", Context.MODE_PRIVATE);
        String lastSearchedCity = prefs.getString("lastSearchedCity", "");

        if (!lastSearchedCity.equals("")) {
            binding.searchCityEditText.setText(lastSearchedCity);
        }

        // Handle the user searching for a city's forecast
        binding.searchBtn.setOnClickListener(clk -> {
            String cityName = binding.searchCityEditText.getText().toString(); // Get city entered by user
            // Only respond to the search if the EditText is not empty
            if (!cityName.equals("")) {
                // Update users shared preferences to reflect the newest search
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("lastSearchedCity", cityName);
                editor.apply();

                // Bring the user to the results page
                Intent resultsPage = new Intent( WeatherActivity.this, WeatherSearchResponse.class);
                resultsPage.putExtra("cityName", cityName); // Send the searched city as a variable to results page
                startActivity(resultsPage);
            }
        });

        // When a saved forecast is clicked, load it into a fragment
        weatherModel.selectedForecast.observe(this, (selectedForecast) -> {
            ForecastDetailsFragment forecastFragment = new ForecastDetailsFragment(selectedForecast);
            getSupportFragmentManager().beginTransaction().add(R.id.weatherFragmentLocation, forecastFragment).addToBackStack("").commit();
        });

        // *****************************************************
        // * TEST DATA FOR SAVED FORECASTS

        savedForecasts.add(new Forecast("ottawa", "Jan 17th", "wsymbol_0033_cloudy_with_light_rain_night.png", "Sunny",15, 13, 60, 1, 10, 10));
        savedForecasts.add(new Forecast("nyc", "Aug 30th", "wsymbol_0033_cloudy_with_light_rain_night.png", "Sunny",15, 13, 60, 1, 10, 10));
        savedForecasts.add(new Forecast("mount pearl", "Dec. 25th", "wsymbol_0033_cloudy_with_light_rain_night.png", "Sunny",15, 13, 60, 1, 10, 10));
        myAdapter.notifyDataSetChanged();


        // *****************************************************
        setSupportActionBar(binding.weatherToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.weather_menu, menu); // inflate toolbar

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch( item.getItemId() )
        {
            case R.id.weatherHelp:
                // Get strings from res/strings
                String weatherInstructions = getResources().getString(R.string.weather_instructions);
                String weatherHelpTitle = getResources().getString(R.string.weather_help);

                // Display AlertDialog providing instructions on how to use this activity
                AlertDialog.Builder builder = new AlertDialog.Builder( WeatherActivity.this );
                builder.setMessage(weatherInstructions)
                        .setTitle(weatherHelpTitle)
                        .create()
                        .show();

                break;

            case R.id.kitten_bar_btn:
                // Send user to Kitten Images app
                Intent kittenPage = new Intent(WeatherActivity.this, SecondActivity.class);
                startActivity( kittenPage );

                break;

            case R.id.nyt_bar_btn:
                // Send user to New York Times app
                Intent nytPage = new Intent(WeatherActivity.this, NewYorkTimes.class);
                startActivity( nytPage );

                break;

            case R.id.nasa_bar_btn:
                // Send user to Nasa photos app
                Intent nasaPage = new Intent(WeatherActivity.this, NasaPhotos.class);
                startActivity( nasaPage );

                break;

        }

        return true;
    }

    /** Represents a row in the saved forecasts recycler view
     * @author Noah King
     * @version 1.0
     */
    class MyRowHolder extends RecyclerView.ViewHolder {
        /** Delete button for the forecast */
        Button forecastDeleteBtn;

        /** City of the forecast */
        TextView forecastCity;

        /** Date forecast was saved */
        TextView forecastDate;

        /** Temperature of the forecast */
        TextView forecastTemp;

        /** Icon representing a description of the forecast */
        ImageView forecastIcon;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            // Access views for the row
            forecastDeleteBtn = itemView.findViewById(R.id.forecastDeleteBtn);
            forecastCity = itemView.findViewById(R.id.forecastCity);
            forecastDate = itemView.findViewById(R.id.forecastDate);
            forecastTemp = itemView.findViewById(R.id.forecastTemp);
            forecastIcon = itemView.findViewById(R.id.forecastIcon);

            // Register a click event on the forecast to load a fragment that displays it
            itemView.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                Forecast selected = savedForecasts.get(position);

                weatherModel.selectedForecast.postValue(selected);
            });

            // Activate delete button
            forecastDeleteBtn.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition(); // position of forecast in array list

                // Get strings from res/strings
                String deleteMsg = getResources().getString(R.string.delete_forecast_msg);
                String deleteTitle = getResources().getString(R.string.delete_forecast_title);
                String forecastDeleted = getResources().getString(R.string.forecast_deleted);
                String noBtn = getResources().getString(R.string.no_button);
                String yesBtn = getResources().getString(R.string.yes_button);

                // Show an AlertDialog when the user clicks the delete button asking if they want to delete the forecast
                AlertDialog.Builder builder = new AlertDialog.Builder(WeatherActivity.this);
                builder.setMessage(deleteMsg)
                    .setTitle(deleteTitle)
                    .setNegativeButton(noBtn, (dialog, cl) -> {})
                    .setPositiveButton(yesBtn, (dialog, cl) -> {
                        // Remove forecast if they click "Yes"
                        savedForecasts.remove(position);
                        myAdapter.notifyItemRemoved(position);

                        // Show Snackbar stating that you deleted the forecast
                        Snackbar.make(forecastDeleteBtn, forecastDeleted, Snackbar.LENGTH_LONG).show();
                    })
                .create()
                .show();
            });
        }
    }
}