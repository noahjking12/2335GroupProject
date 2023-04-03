package algonquin.cst2335.a2335groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;


import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.util.Base64;

import algonquin.cst2335.a2335groupproject.data.CatImageDatabase;
import algonquin.cst2335.a2335groupproject.data.CatListViewModel;
import algonquin.cst2335.a2335groupproject.databinding.ActivitySecondBinding;
import algonquin.cst2335.a2335groupproject.databinding.ImageListBinding;
import algonquin.cst2335.a2335groupproject.kitten.CatList;
import algonquin.cst2335.a2335groupproject.ui.WeatherActivity;

/** Kitten PlaceHolder app front page
 * @author Yalin Su
 * @version 1.0
 */
public class SecondActivity extends AppCompatActivity {

    /**
     * variable binding2 for Kitten PlaceHolder activity
     */
    private  ActivitySecondBinding binding2;

    /**
     * variable imageListBinding for Kitten PlaceHolder activity
     */
    private ImageListBinding imageListBinding;

    /**
     * An ArrayList that holds CatList object, containing image URLs of kitten images
     */
    private ArrayList<CatList> imageUrlList;

    /**
     * Adapter to populate the RecyclerView with CatList data
     */
    private RecyclerView.Adapter myAdapter;

    /**
     * ViewModel for saving SecondActivity's data
     */
    private CatListViewModel catModel;
    /**
     * SharedPreferences instance to store and retrieve data for the width and height edit text
     */
    private SharedPreferences myPreferences;

    /**
     * CatList object representing a single kitten image in the list
     */
    CatList clObj;
    /**
     * DAO for interacting with the kitten database
     */
    CatDAO catDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding2 = ActivitySecondBinding.inflate(getLayoutInflater());

        CatImageDatabase db = Room.databaseBuilder(getApplicationContext(), CatImageDatabase.class,"myFavourites").build();
        catDAO = db.catDAO();

        setContentView(binding2.getRoot());

        setSupportActionBar(binding2.myToolbar);

        binding2.recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // load the width and height edit text with the last entered by the user
        myPreferences = getPreferences(Context.MODE_PRIVATE);
        String widthFound = myPreferences.getString("Width","");
        String heightFound = myPreferences.getString("Height","");
        binding2.widthEdit.setText(widthFound);
        binding2.heightEdit.setText(heightFound);

        // handle the user saving the width and height text for next time
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

        // initialize the CatListViewModel instance using the ViewModelProvider
        catModel = new ViewModelProvider(this).get(CatListViewModel.class);


        // retrieve the list of kitten images from the ViewModel's LiveData
        imageUrlList = catModel.catViewModel.getValue();

        // if the imageUrlList is null, then create a new empty ArrayList
        // and update the ViewModel's LiveData with the new list
        if (imageUrlList == null)
        {
            catModel.catViewModel.postValue( imageUrlList = new ArrayList<>() );
        }


        /**
         * onClickListener for the enter button
         * When the button is clicked, the width and height that entered by the user
         * are used to fetch an image of a kitten from the given URL using Volley.
         * The retrieved image is then converted to a Base64-encoded String and added to the imageUrlList.
         */
        binding2.enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String width = binding2.widthEdit.getText().toString();
                String height = binding2.heightEdit.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
                String currentDateandTime = sdf.format(new Date());
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
                        clObj = new CatList(catUrl, width, height, currentDateandTime);
                        imageUrlList.add(clObj);
                        myAdapter.notifyItemInserted(imageUrlList.size() - 1);
                    }
                }, 0, 0, null, null);

                // add the request to the RequestQueue
                queue.add(request);
            }
        });


        /**
         * Set the adapter for the recyclerView with a custom RecyclerView.Adapter
         * implementation that inflates the ImageListBinding layout, binds the data
         * to the ViewHolder, and handles the click events for displaying image details
         * and saving images.
         */
        binding2.recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<myCatHolder>() {
            @NonNull
            @Override
            public myCatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // inflate the ImageListBinding layout and create a ViewHolder instance
                imageListBinding = ImageListBinding.inflate(getLayoutInflater(), parent, false);

                return new myCatHolder(imageListBinding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull myCatHolder holder, int position) {
                // get the CatList object at the current position
                CatList imageOnRow = imageUrlList.get(position);
                // load the image into the ImageView using Glide
                Glide.with(holder.itemView.getContext())
                        .load(imageOnRow.getCatUrl())
                        .into(holder.catImageList);

                /**
                 * set onClickListener when user click on an image
                 * it will go to the fragment page and display that image details
                 */
                holder.catImageList.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        CatImageDetailsFragment catFragment = new CatImageDetailsFragment(imageOnRow);
                        activity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentLocation, catFragment)
                                .addToBackStack("")
                                .commit();
                    }
                });

                /**
                 * set onClickListener when user click the add to favourite button
                 * it will saved the selected image to the database
                 */
                holder.add_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // create a single-thread executor to perform the database operation
                        Executor thread = Executors.newSingleThreadExecutor();
                        // Execute the operation on the new thread
                        thread.execute(()->{
                            // create a new CatList object with the image details
                            clObj = new CatList(imageOnRow.getCatUrl(), imageOnRow.getWidth(), imageOnRow.getHeight(), imageOnRow.getDateSaved());
                            // add the new CatList object to the imageUrlList
                            imageUrlList.add(clObj);
                            // insert the new CatList object into the database
                            catDAO.insertImage(clObj);
                            clObj.getId();

                            // update the UI on the main thread
                            runOnUiThread(()->{
                                // show a Toast message after the user saved the image
                                String imageSaved = getResources().getString(R.string.kitten_imageSavedInfo);
                                Toast.makeText(v.getContext(), imageSaved, Toast.LENGTH_LONG).show();
                            });

                        });
                    }
                });

            }

            /**
             * returns the total number of items in the imageUrlList
             * this method is used by the RecyclerView to determine the number of items to be displayed
             * @return the number of items in the imageUrlList.
             */
            @Override
            public int getItemCount() {
                return imageUrlList.size();
            }
        });

    }

    /**
     * inflate the menu_bar and kitten_favourites_menu layout from the menu folder for this activity
     *
     * @param menu The options menu in which you place your items.
     *
     * @return true to display the menu, false to hide the menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        getMenuInflater().inflate(R.menu.kitten_favourites_menu, menu);
        return true;
    }

    /**
     * handle the selection of menu items in the options menu
     * different action is executed based on the selected item
     *
     * @param item The menu item that was selected.
     *
     * @return true to response the menu item click event
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String kitten_instruction = getResources().getString(R.string.kitten_instruction);
        String instructionInfo = getResources().getString(R.string.kitten_detail);
        String okBtn = getResources().getString(R.string.kitten_ok);

        // determine the selected menu item and execute the corresponding action
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

            case R.id.view_favourites:
                // go to view favourites page
                Intent kittenHolder2 = new Intent(SecondActivity.this, KittenHolder2.class);
                startActivity(kittenHolder2);
        }
        return true;

    }

    /**
     * a custom ViewHolder class for the RecyclerView, that is represents a single
     * item in the list of kitten images.
     * the ViewHolder holds an ImageView to display the kitten image and a Button to add an image to the favourite list.
     * it also handles click events on the list item
     */
    public class myCatHolder extends RecyclerView.ViewHolder{
        /**
         * ImageView for displaying the kitten image in the RecyclerView list item
         */
        public ImageView catImageList;
        /**
         * Button to add an kitten image to the favourite list
         */
        public Button add_btn;

        /**
         * constructor for myCatHolder
         * @param itemView the view that represents a single list item
         */
        public myCatHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(click ->{
                int whichImage = getAbsoluteAdapterPosition();
                CatList selected = imageUrlList.get(whichImage);
                catModel.selectedImage.postValue(selected);
            });
            catImageList = itemView.findViewById(R.id.catImageList);
            add_btn = itemView.findViewById(R.id.add_btn);
        }
    }

}