package algonquin.cst2335.a2335groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import algonquin.cst2335.a2335groupproject.databinding.ActivityMainBinding;
import algonquin.cst2335.a2335groupproject.ui.WeatherActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());


        setSupportActionBar(binding.myToolbar);

        setContentView(binding.getRoot());

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch( item.getItemId() )
        {
            case R.id.NasaItem:
                // Go to Nasa activity

                    Intent nasaPage = new Intent(MainActivity.this, NasaPhotos.class);
                    startActivity(nasaPage);

                break;

            case R.id.KittenItem:


                    Intent kittenPage = new Intent(MainActivity.this, SecondActivity.class);

                    startActivity( kittenPage );

                break;
            case R.id.NYTItem:
                // Go to NewYorkTimes activity

                    Intent nytPage = new Intent(MainActivity.this, NewYorkTimes.class);
                    startActivity(nytPage);


                break;

            case R.id.WeatherItem:

                    Intent weatherPage = new Intent(MainActivity.this, WeatherActivity.class);

                    startActivity(weatherPage);

                break;
        }

        return true;
    }
}