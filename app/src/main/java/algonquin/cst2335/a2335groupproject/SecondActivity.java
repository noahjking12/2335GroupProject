package algonquin.cst2335.a2335groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import android.util.Base64;

import algonquin.cst2335.a2335groupproject.data.CatListViewModel;
import algonquin.cst2335.a2335groupproject.databinding.ActivitySecondBinding;
import algonquin.cst2335.a2335groupproject.databinding.ImageListBinding;
import algonquin.cst2335.a2335groupproject.ui.WeatherActivity;

public class SecondActivity extends AppCompatActivity {

    private  ActivitySecondBinding binding2;

    private ImageListBinding imageListBinding;

    private ArrayList<CatList> imageUrlList;

    private RecyclerView.Adapter myAdapter;

    private CatListViewModel catModel;
    private SharedPreferences myPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding2 = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding2.getRoot());

        setSupportActionBar(binding2.myToolbar);

        binding2.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //File sandbox = new File(getFilesDir(), "favourite.png");

        myPreferences = getPreferences(Context.MODE_PRIVATE);
        String widthFound = myPreferences.getString("Width","");
        String heightFound = myPreferences.getString("Height","");
        binding2.widthEdit.setText(widthFound);
        binding2.heightEdit.setText(heightFound);

        binding2.saveButton.setOnClickListener( click -> {

            String widthToSaved = binding2.widthEdit.getText().toString();
            String heightToSaved = binding2.heightEdit.getText().toString();
            SharedPreferences.Editor editor = myPreferences.edit();
            editor.putString("Width", widthToSaved);
            editor.putString("Height", heightToSaved);
            editor.apply();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String confirmation = getResources().getString(R.string.kitten_confirmation);
            String alertMessage = getResources().getString(R.string.kitten_AlertDialog_Message);
            String saveBtn = getResources().getString(R.string.kittin_save_btn);
            String snackBarMessage = getResources().getString(R.string.kitten_Snackbar);
            String noBtn = getResources().getString(R.string.kitten_No);

            builder.setTitle(confirmation)
                    .setMessage(alertMessage)
                    .setPositiveButton(saveBtn, (dialog, option)-> {
                        Snackbar.make(binding2.getRoot(),snackBarMessage, Snackbar.LENGTH_LONG).show();
                    })
                    .setNegativeButton(noBtn, (dialog, option)->{
                        // do nothing
                    })
                    .show();

        });


        catModel = new ViewModelProvider(this).get(CatListViewModel.class);
        imageUrlList = catModel.catViewModel.getValue();

        if (imageUrlList == null)
        {
            catModel.catViewModel.postValue( imageUrlList = new ArrayList<>() );
        }


        binding2.enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String width = binding2.widthEdit.getText().toString();
                String height = binding2.heightEdit.getText().toString();
                String url = "https://placekitten.com/"+ width + "/" + height;

                // instantiate the RequestQueue
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                // Request a binary response from the provided URL
                ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        // covert the Bitmap image to a Base64-encoded String
                        ByteArrayOutputStream imageConvert = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageConvert);
                        byte[] imageBytes = imageConvert.toByteArray();
                        String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                        // add the base64-encoded image URL to the list
                        String catUrl = "data:image/jpeg;base64," + base64Image;
                        CatList clObj = new CatList(catUrl, width, height);
                        imageUrlList.add(clObj);
                        myAdapter.notifyItemInserted(imageUrlList.size() - 1);
                    }
                }, 0, 0, null, null);

                // add the request to the RequestQueue
                queue.add(request);
            }
        });
//                String catUrl = url;
//                CatList clObj = new CatList(catUrl, width, height);
//                imageUrlList.add(clObj);
//                myAdapter.notifyItemInserted(imageUrlList.size() - 1);

        binding2.recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<myCatHolder>() {
            @NonNull
            @Override
            public myCatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                imageListBinding = ImageListBinding.inflate(getLayoutInflater(), parent, false);

                return new myCatHolder(imageListBinding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull myCatHolder holder, int position) {
                CatList imageOnRow = imageUrlList.get(position);
                // using Glide to load image into catImageList
                Glide.with(holder.itemView.getContext())
                        .load(imageOnRow.getCatUrl())
                        .into(holder.catImageList);

                holder.catImageList.setOnClickListener(new View.OnClickListener() {
                    String toastWidth = getResources().getString(R.string.kitten_toastMessage_width);
                    String toastHeight = getResources().getString(R.string.kitten_toastMessage_height);
                    @Override
                    public void onClick(View v) {
                        String toastMessage = toastWidth + " " + imageOnRow.getWidth() + toastHeight + " " + imageOnRow.getHeight();
                        Toast.makeText(v.getContext(), toastMessage, Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public int getItemCount() {
                return imageUrlList.size();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String kitten_instruction = getResources().getString(R.string.kitten_instruction);
        String instructionInfo = getResources().getString(R.string.kitten_detail);
        String okBtn = getResources().getString(R.string.kitten_ok);

        switch (item.getItemId())
        {
            case R.id.helpMenu:
                AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
                builder.setMessage(instructionInfo)
                        .setTitle(kitten_instruction)
                        .setNegativeButton(okBtn, (dialog, which) -> {
                            // do nothing
                        })
                        .create().show();
                break;

            case R.id.NasaItem:
                // go to Nasa page
                Intent nasaPge = new Intent(SecondActivity.this, NasaPhotos.class);
                startActivity(nasaPge);

                break;

            case R.id.NYTItem:
                // go to NewYorkTimes page
                Intent newYorkTimes = new Intent(SecondActivity.this, NewYorkTimes.class);
                startActivity(newYorkTimes);

                break;

            case R.id.WeatherItem:
                // go to Weather page
                Intent weatherPage = new Intent(SecondActivity.this, WeatherActivity.class);
                startActivity(weatherPage);

                break;
        }
        return true;

    }

    public class myCatHolder extends RecyclerView.ViewHolder{
        public ImageView catImageList;
        public myCatHolder(@NonNull View itemView) {
            super(itemView);
            catImageList = itemView.findViewById(R.id.catImageList);
        }
    }

}