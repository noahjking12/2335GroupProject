package algonquin.cst2335.a2335groupproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import algonquin.cst2335.a2335groupproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    /** Variable binding for main activity */
    private ActivityMainBinding binding;

    /** Button that brings the user to the weather activity */
    private Button weatherBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Establish variable binding for main activity
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Go to weather activity
        weatherBtn = binding.WeatherStackButton;;
        weatherBtn.setOnClickListener(clk -> {
            Intent weatherPage = new Intent(MainActivity.this, WeatherActivity.class);
            startActivity(weatherPage);
        });
    }
}