package algonquin.cst2335.a2335groupproject;
/**
 * @author Jinwei Li 040818950
 * @version 1.0
 */
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import algonquin.cst2335.a2335groupproject.databinding.ActivityNasaPhotos2Binding;
import algonquin.cst2335.a2335groupproject.databinding.ActivityNasaPhotosBinding;

public class NasaPhotos2 extends AppCompatActivity {

    private ActivityNasaPhotos2Binding binding;

    /**
     *restore the data that the user had already entered.
     * @param savedInstanceState savedInstanceState
     */
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