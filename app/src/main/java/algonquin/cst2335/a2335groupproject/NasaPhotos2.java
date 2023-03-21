package algonquin.cst2335.a2335groupproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import algonquin.cst2335.a2335groupproject.databinding.ActivityNasaPhotos2Binding;
import algonquin.cst2335.a2335groupproject.databinding.ActivityNasaPhotosBinding;

public class NasaPhotos2 extends AppCompatActivity {

    private ActivityNasaPhotos2Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNasaPhotos2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());//+Binding

        Intent fromPrevious = getIntent();
        String editText = fromPrevious.getStringExtra("editText");

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));


    }
}