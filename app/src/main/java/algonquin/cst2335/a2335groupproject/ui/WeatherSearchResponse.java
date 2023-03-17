package algonquin.cst2335.a2335groupproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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


    }
}