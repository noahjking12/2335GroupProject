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
import android.widget.Toast;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.a2335groupproject.data.CatImageDatabase;
import algonquin.cst2335.a2335groupproject.data.CatListViewModel;
import algonquin.cst2335.a2335groupproject.databinding.KittenHolder2Binding;
import algonquin.cst2335.a2335groupproject.databinding.SavedImageListBinding;
import algonquin.cst2335.a2335groupproject.kitten.CatList;

/** KittenHolder2 is the my favourite page of showing all the saved kitten images
 * This page allow user to view and delete the saved kitten images from the list
 * @author Yalin Su
 * @version 1.0
 */
public class KittenHolder2 extends AppCompatActivity {
    /**
     * variable kittenHolder2Binding for KittenHolder2 activity
     */
    private KittenHolder2Binding kittenHolder2Binding;

    /**
     * variable savedImageListBinding for KittenHolder2 activity
     */
    private SavedImageListBinding savedImageListBinding;

    /**
     * ViewModel for managing and storing the kitten images
     */
    private CatListViewModel catModel;
    /**
     * DAO for accessing the kitten database of saved kitten images
     */
    CatDAO catDAO;
    /**
     * An ArrayList that holds CatList object, containing saved image URLs of kitten images
     */
    private ArrayList<CatList> imageUrlList;
    /**
     * Adapter to manage the list of saved kitten images
     */
    private RecyclerView.Adapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        kittenHolder2Binding = KittenHolder2Binding.inflate(getLayoutInflater());
        setContentView(kittenHolder2Binding.getRoot());

        // set the layout manager for the RecyclerView
        kittenHolder2Binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // initialize the CatImageDatabase and get a catDAO instance
        CatImageDatabase db = Room.databaseBuilder(getApplicationContext(), CatImageDatabase.class,"myFavourites").build();
        catDAO = db.catDAO();

        // Initialize the CatListViewModel and create an empty ArrayList for the imageUrlList
        catModel = new ViewModelProvider(this).get(CatListViewModel.class);
        imageUrlList = new ArrayList<>();

                // execute a background thread to fetch saved kitten images from the database
                Executor thread = Executors.newSingleThreadExecutor();
                thread.execute(()->{
                    List<CatList> savedImages = catDAO.getALLImages();
                    runOnUiThread(()->{
                        // add all saved images to imageUrlList and set the adapter for the RecyclerView
                        imageUrlList.addAll(savedImages);
                        kittenHolder2Binding.recyclerView.setAdapter(myAdapter);
                    });
                });

        // initialize the custom RecyclerView.Adapter, myAdapter with a ViewHolder of type 'myViewHolder'
        myAdapter = new RecyclerView.Adapter<myViewHolder>()
        {
            @NonNull
            @Override
            public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // inflate the SavedImageListBinding layout and create a ViewHolder instance
                savedImageListBinding = SavedImageListBinding.inflate(getLayoutInflater(), parent,false);

                return new myViewHolder(savedImageListBinding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
                // get the CatList object at the current position
                CatList imageOnRow = imageUrlList.get(position);
                // load the saved images into the savedImageList ImageView of the holder using Glide
                Glide.with(holder.itemView.getContext())
                        .load(imageOnRow.getCatUrl())
                        .into(holder.savedImageList);

                /**
                 * set onClickListener for the remove button
                 * when user click the remove button, it will remove the selected image from the favourite list
                 */
                holder.remove_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // get the adapter position of the image to be removed
                        int imageToDeleted = holder.getAbsoluteAdapterPosition();
                        // get the CatList object at the current position in imageUrlList
                        CatList selected = imageUrlList.get(imageToDeleted);
                        // notify the changed of value to object selectedImage in catModel, so that it updates the UI
                        catModel.selectedImage.postValue(selected);

                        // create an AlertDialog to ask user's confirmation before deletion
                        AlertDialog.Builder builder = new AlertDialog.Builder( KittenHolder2.this );
                        String alertMessage = getResources().getString(R.string.kitten_favourite_remove);
                        String confirmation = getResources().getString(R.string.kitten_confirmation);
                        String yes = getResources().getString(R.string.kitten_yes);
                        String no = getResources().getString(R.string.kitten_No);

                        builder.setMessage(alertMessage)
                                .setTitle(confirmation)
                                .setPositiveButton(yes,(dialog, which) -> {

                                    // if user chose to delete the image, then remove the image from the imageUrlList
                                    // and delete from the database
                                    CatList itemToDeleted = imageUrlList.get(imageToDeleted);
                                    Executor thread = Executors.newSingleThreadExecutor();
                                    thread.execute(()->{
                                        catDAO.deleteImages(itemToDeleted);
                                        imageUrlList.remove(imageToDeleted);

                                        // update UI on the main thread
                                        runOnUiThread(()->{
                                            myAdapter.notifyItemRemoved(imageToDeleted);
                                            String imageRemoved = getResources().getString(R.string.kitten_imgaeRemove);
                                            Toast.makeText(v.getContext(), imageRemoved, Toast.LENGTH_LONG).show();
                                        });
                                    });
                                })
                                .setNegativeButton(no, (dialog, which) -> {
                                  // do nothing if user chose cancel
                                })
                                .create()
                                .show();
                    }
                });

        }

            /**
             * returns the total number of items in the imageUrlList
             * this method is used by the RecyclerView to determine the number of items to be displayed
             * @return the number of items in the imageUrlList
             */
            @Override
            public int getItemCount() {
                return imageUrlList.size();
            }
        };

    }
}
