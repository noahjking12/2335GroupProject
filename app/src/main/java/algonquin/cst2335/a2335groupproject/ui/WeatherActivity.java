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
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import algonquin.cst2335.a2335groupproject.Forecast;
import algonquin.cst2335.a2335groupproject.R;
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
                holder.forecastCountry.setText(savedForecast.getCountry());
                holder.forecastCity.setText(savedForecast.getCity());
                holder.forecastDate.setText(savedForecast.getDate());
                holder.forecastTemp.setText(Integer.toString(savedForecast.getTemperature()));
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

        // *****************************************************
        // * TEST DATA FOR SAVED FORECASTS

//        savedForecasts.add(new Forecast("canada", "ottawa", "Jan 17th", "Sunny",15, 13, 60, 1, 10, 10));
//        savedForecasts.add(new Forecast("u.s", "nyc", "Aug 30th", "Sunny",15, 13, 60, 1, 10, 10));
//        savedForecasts.add(new Forecast("canada", "mount pearl", "Dec. 25th", "Sunny",15, 13, 60, 1, 10, 10));
//        myAdapter.notifyDataSetChanged();


        // *****************************************************
    }

    /** Represents a row in the saved forecasts recycler view
     * @author Noah King
     * @version 1.0
     */
    class MyRowHolder extends RecyclerView.ViewHolder {
        /** Delete button for the forecast */
        Button forecastDeleteBtn;

        /** Country of the forecast */
        TextView forecastCountry;

        /** City of the forecast */
        TextView forecastCity;

        /** Date forecast was saved */
        TextView forecastDate;

        /** Temperature of the forecast */
        TextView forecastTemp;

//        /** Icon representing a description of the forecast */
//        ImageView forecastIcon; ** IMPLEMENT LATER

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            // Access views for the row
            forecastDeleteBtn = itemView.findViewById(R.id.forecastDeleteBtn);
            forecastCountry = itemView.findViewById(R.id.forecastCountry);
            forecastCity = itemView.findViewById(R.id.forecastCity);
            forecastDate = itemView.findViewById(R.id.forecastDate);
            forecastTemp = itemView.findViewById(R.id.forecastTemp);
//            forecastIcon = itemView.findViewById(R.id.forecastIcon); ** IMPLEMENT LATER

            // Activate delete button
            forecastDeleteBtn.setOnClickListener(clk -> {
                int position = getAdapterPosition(); // position of forecast in array list

                // Show an AlertDialog when the user clicks the delete button asking if they want to delete the forecast
                AlertDialog.Builder builder = new AlertDialog.Builder(WeatherActivity.this);
                builder.setMessage("Do you want to delete this forecast?")
                    .setTitle("Delete saved forecast:")
                    .setNegativeButton("No", (dialog, cl) -> {})
                    .setPositiveButton("Yes", (dialog, cl) -> {
                        // Remove forecast if they click "Yes"
                        savedForecasts.remove(position);
                        myAdapter.notifyItemRemoved(position);

                        // Show Snackbar stating that you deleted the forecast
                        Snackbar.make(forecastDeleteBtn, "Forecast successfully deleted.", Snackbar.LENGTH_LONG).show();
                    })
                .create()
                .show();
            });
        }
    }
}