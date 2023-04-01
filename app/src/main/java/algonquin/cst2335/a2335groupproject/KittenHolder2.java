package algonquin.cst2335.a2335groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.a2335groupproject.data.CatImageDatabase;
import algonquin.cst2335.a2335groupproject.data.CatListViewModel;
import algonquin.cst2335.a2335groupproject.databinding.KittenHolder2Binding;
import algonquin.cst2335.a2335groupproject.databinding.SavedImageListBinding;
import algonquin.cst2335.a2335groupproject.kitten.CatList;

public class KittenHolder2 extends AppCompatActivity {
    private KittenHolder2Binding kittenHolder2Binding;

    private SavedImageListBinding savedImageListBinding;

    private CatListViewModel catModel;
    CatDAO catDAO;
    private ArrayList<CatList> imageUrlList;
    private RecyclerView.Adapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        kittenHolder2Binding = KittenHolder2Binding.inflate(getLayoutInflater());
        setContentView(kittenHolder2Binding.getRoot());


        kittenHolder2Binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        CatImageDatabase db = Room.databaseBuilder(getApplicationContext(), CatImageDatabase.class,"myFavourites").build();
        catDAO = db.catDAO();

        catModel = new ViewModelProvider(this).get(CatListViewModel.class);
        imageUrlList = new ArrayList<>();

                Executor thread = Executors.newSingleThreadExecutor();
                thread.execute(()->{
                    List<CatList> savedImages = catDAO.getALLImages();
                    runOnUiThread(()->{
                        imageUrlList.addAll(savedImages);
                        kittenHolder2Binding.recyclerView.setAdapter(myAdapter);
                    });
                });



        myAdapter = new RecyclerView.Adapter<myViewHolder>()
        {
            @NonNull
            @Override
            public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                savedImageListBinding = SavedImageListBinding.inflate(getLayoutInflater(), parent,false);

                return new myViewHolder(savedImageListBinding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
                CatList imageOnRow = imageUrlList.get(position);
                Glide.with(holder.itemView.getContext())
                        .load(imageOnRow.getCatUrl())
                        .into(holder.savedImageList);

                holder.remove_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int imageToDeleted = holder.getAbsoluteAdapterPosition();
                        CatList selected = imageUrlList.get(imageToDeleted);
                        catModel.selectedImage.postValue(selected);

                        AlertDialog.Builder builder = new AlertDialog.Builder( KittenHolder2.this );

                        builder.setMessage("Do you want to remove this picture from your favourite list?")
                                .setTitle("Confirmation")
                                .setPositiveButton("Yes",(dialog, which) -> {

                                    CatList itemToDeleted = imageUrlList.get(imageToDeleted);
                                    Executor thread = Executors.newSingleThreadExecutor();
                                    thread.execute(()->{
                                        catDAO.deleteImages(itemToDeleted);
                                        imageUrlList.remove(imageToDeleted);

                                        runOnUiThread(()->{
                                            myAdapter.notifyItemRemoved(imageToDeleted);
                                            String imageRemoved = getResources().getString(R.string.kitten_imgaeRemove);
                                            Toast.makeText(v.getContext(), imageRemoved, Toast.LENGTH_LONG).show();
                                        });
                                    });
                                })
                                .setNegativeButton("Cancel", (dialog, which) -> {
                                  // do nothing
                                })
                                .create()
                                .show();


                    }
                });

        }

            @Override
            public int getItemCount() {
                return imageUrlList.size();
            }
        };

        kittenHolder2Binding.recyclerView.setAdapter(myAdapter);

    }
}
