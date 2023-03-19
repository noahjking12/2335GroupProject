package algonquin.cst2335.a2335groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


import java.io.File;
import java.util.ArrayList;

import algonquin.cst2335.a2335groupproject.data.CatListViewModel;
import algonquin.cst2335.a2335groupproject.databinding.ActivitySecondBinding;
import algonquin.cst2335.a2335groupproject.databinding.ImageListBinding;

public class SecondActivity extends AppCompatActivity {

    private  ActivitySecondBinding binding2;

    private ArrayList<CatList> imageUrlList;

    private RecyclerView.Adapter myAdapter;

    private CatListViewModel catModel;
    private SharedPreferences myPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding2 = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding2.getRoot());



        binding2.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //File sandbox = new File(getFilesDir(), "favourite.png");

        myPreferences = getPreferences(Context.MODE_PRIVATE);
        String widthFound = myPreferences.getString("Width","not found");
        String heightFound = myPreferences.getString("Height","not found");
        binding2.widthEdit.setText(widthFound);
        binding2.heightEdit.setText(heightFound);

        binding2.saveButton.setOnClickListener( click -> {
            String widthToSaved = binding2.widthEdit.getText().toString();
            String heightToSaved = binding2.heightEdit.getText().toString();
            SharedPreferences.Editor editor = myPreferences.edit();
            editor.putString("Width", widthToSaved);
            editor.putString("Height", heightToSaved);

            editor.apply();

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
                String catUrl = url;
                CatList clObj = new CatList(catUrl);
                imageUrlList.add(clObj);
                myAdapter.notifyItemInserted(imageUrlList.size() - 1);
                //binding2.widthEdit.setText("");
                //binding2.heightEdit.setText("");
            }
        });
        binding2.recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<myCatHolder>() {
            @NonNull
            @Override
            public myCatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                ImageListBinding imageListBinding = ImageListBinding.inflate(getLayoutInflater(), parent, false);

                return new myCatHolder(imageListBinding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull myCatHolder holder, int position) {
                CatList imageOnRow = imageUrlList.get(position);
                // using Glide to load image into catImageList
                Glide.with(holder.itemView.getContext())
                        .load(imageOnRow.getCatUrl())
                        .into(holder.catImageList);

            }

            @Override
            public int getItemCount() {
                return imageUrlList.size();
            }
        });

    }

    public class myCatHolder extends RecyclerView.ViewHolder{
        public ImageView catImageList;
        public myCatHolder(@NonNull View itemView) {
            super(itemView);
            catImageList = itemView.findViewById(R.id.catImageList);
        }
    }

}