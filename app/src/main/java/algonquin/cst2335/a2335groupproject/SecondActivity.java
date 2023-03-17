package algonquin.cst2335.a2335groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;


import algonquin.cst2335.a2335groupproject.databinding.ActivitySecondBinding;

public class SecondActivity extends AppCompatActivity {

    private  ActivitySecondBinding binding2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding2 = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding2.getRoot());


        binding2.enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String width = binding2.widthEdit.getText().toString();
                String height = binding2.heightEdit.getText().toString();

                String url = "https://placekitten.com/" + width + "/" + height;

                Glide.with(SecondActivity.this)
                        .load(url)
                        .into(binding2.catImageView);

            }
        });
    }

}