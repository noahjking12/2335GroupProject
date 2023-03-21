package algonquin.cst2335.a2335groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import algonquin.cst2335.a2335groupproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Establish variable binding for main activity
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Go to weather activity
        binding.NasaMarsRoverButton.setOnClickListener(clk -> {
            Intent nasaPage = new Intent(MainActivity.this, NasaPhotos.class);
            startActivity(nasaPage);
        });
    }
}