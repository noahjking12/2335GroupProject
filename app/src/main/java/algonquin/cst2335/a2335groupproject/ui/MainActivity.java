package algonquin.cst2335.a2335groupproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import algonquin.cst2335.a2335groupproject.NasaPhotos;
import algonquin.cst2335.a2335groupproject.NewYorkTimes;
import algonquin.cst2335.a2335groupproject.SecondActivity;
import algonquin.cst2335.a2335groupproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());


        setContentView(binding.getRoot());

        binding.KittenPlaceholderButton.setOnClickListener( button -> {
            Intent nextPage = new Intent(MainActivity.this, SecondActivity.class);

            startActivity( nextPage );
        });

        binding.WeatherStackButton.setOnClickListener( btn -> {
            Intent nextPage = new Intent(MainActivity.this, WeatherActivity.class);

            startActivity(nextPage);
        });

        // Go to NewYorkTimes activity
        binding.NewYorkTimesButton.setOnClickListener(clk -> {
            Intent nasaPage = new Intent(MainActivity.this, NewYorkTimes.class);
            startActivity(nasaPage);
        });


        // Go to weather activity
        binding.NasaMarsRoverButton.setOnClickListener(clk -> {
            Intent nasaPage = new Intent(MainActivity.this, NasaPhotos.class);
            startActivity(nasaPage);
        });
    }
}