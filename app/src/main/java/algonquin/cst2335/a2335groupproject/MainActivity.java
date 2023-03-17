package algonquin.cst2335.a2335groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

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
    }
}