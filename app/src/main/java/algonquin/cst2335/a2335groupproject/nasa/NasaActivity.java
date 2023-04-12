package algonquin.cst2335.a2335groupproject.nasa;
/**
 * @author Jinwei Li 040818950
 * @version 1.0
 */

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import android.app.Activity;
import android.content.Context;

import android.content.SharedPreferences;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.a2335groupproject.MainActivity;
import algonquin.cst2335.a2335groupproject.Picture;
import algonquin.cst2335.a2335groupproject.PicturesDAO;
import algonquin.cst2335.a2335groupproject.R;
import algonquin.cst2335.a2335groupproject.data.NasaPhotoViewModel;
import algonquin.cst2335.a2335groupproject.data.PicturesDatabase;
import algonquin.cst2335.a2335groupproject.data.SavedViewModel;
import algonquin.cst2335.a2335groupproject.data.SearchViewModel;
import algonquin.cst2335.a2335groupproject.databinding.ActivityNasaPhotosBinding;



public class NasaActivity extends AppCompatActivity {



    static FragmentManager fragmentManager;
    private RequestQueue requestQueue;

    PicturesDAO mDAO;
    public SearchViewModel searchViewModel;
    public SavedViewModel savedViewModel;
    private NasaPhotoViewModel photoViewModel;

    protected Fragment historyListFragment, searchListFragment;

    protected static PicturesDatabase db;

    /**
     * A Bundle object that contains the saved state of the activity before it was paused or destroyed.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityNasaPhotosBinding binding = ActivityNasaPhotosBinding.inflate(getLayoutInflater());
        fragmentManager = getSupportFragmentManager();
        setContentView(binding.getRoot());

        // Create the RequestQueue object
        requestQueue = Volley.newRequestQueue(this);

        db = Room.databaseBuilder(getApplicationContext(), PicturesDatabase.class, "NASA-Pictures").build();
        mDAO = db.picDAO();


        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        savedViewModel = new ViewModelProvider(this).get(SavedViewModel.class);
        photoViewModel = new ViewModelProvider(this).get(NasaPhotoViewModel.class);

        /** getter user input of search picture  and save search history to data
         * initial savedViewModel
         */
        if(savedViewModel.pictures.getValue() == null){
            Executor loadPictureFromDB = Executors.newSingleThreadExecutor();
            loadPictureFromDB.execute(()->{

                savedViewModel.pictures.postValue(loadImages(mDAO.getAllPictures()));
            });

        }
        /**getter input of user and search in data
         * initial searchViewModel
         */
        if(searchViewModel.pictures.getValue() == null){
            searchViewModel.pictures.postValue(new ArrayList<>());
        }


        /**
         * use SharedPreferences the keep last search date
         */
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String date = prefs.getString("Date", "");
        binding.editText.setText(date);

        binding.history.setOnClickListener(clik->{
            loadFragment(historyListFragment = new NasaPhotoListFragment("h", fragmentManager, savedViewModel, photoViewModel));
            Snackbar.make(binding.history, "Loading history...", Snackbar.LENGTH_LONG).show();
        });

        binding.search.setOnClickListener(clk ->{
            String userInput=binding.editText.getText().toString();
            if (userInput.equals("") ) {
                Context context = getApplicationContext();
                CharSequence text = "Please choose the date.";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }else {
                searchViewModel.date.postValue(userInput);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("Date", userInput);
                editor.apply();
                Snackbar.make(binding.search, "Loading search result...", Snackbar.LENGTH_LONG).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startSearch(userInput, new PhotosCallback() {
                            @Override
                            public void onPhotosLoaded(ArrayList<Picture> ps) {


                                searchViewModel.pictures.postValue(ps);
                                searchViewModel.pictures.observe(NasaActivity.this, (newData)->{
                                    loadFragment(searchListFragment = new NasaPhotoListFragment("s", fragmentManager, searchViewModel, photoViewModel));

                                });

                            }

                        });
                    }
                }).start();


            }});


}

    /**
     *
     * @param allPictures  provides functionality to load images for a list of pictures.
     * @return (ArrayList<Picture>)allPictures
     */
    private ArrayList<Picture> loadImages(List<Picture> allPictures) {
        try{
            for (int i = 0; i < allPictures.size(); i++){
                String pathname = getFilesDir() + "/" + allPictures.get(i).getId() + ".jpg";
                File file = new File(pathname);
                if(file.exists()){
                    Bitmap image = BitmapFactory.decodeFile(pathname);

                    allPictures.get(i).setPhoto(image);

                }
                else{
                    System.out.println("File not exist in Path: " + getFilesDir().getAbsolutePath() + "/" + allPictures.get(i).getId() + ".png");
                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return (ArrayList<Picture>)allPictures;
    }

    /**
     *
     * @param fragment
     */
    private void loadFragment(Fragment fragment){
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.nasa_fragment, fragment);
    fragmentTransaction.addToBackStack(null);
    fragmentTransaction.commit();
}

    /**
     * search the date of the picture (from API)and call back
     * @param date The date to search for photos in the format of "YYYY-MM-DD".
     * @param callback The PhotosCallback object that will receive the loaded photos.
     *
     * @return This method does not return anything.
     */
    public void startSearch(String date, PhotosCallback callback){
    ArrayList<Picture> photos = new ArrayList<>();
    String url = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol="+date+"&api_key=tFfFOWlhU9zByM04PJAcm56wpAdNsNXkwrQ0WSEg";
        try{

            String stringURL = new StringBuilder()
                    .append("https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=")
                    .append(URLEncoder.encode(date, "UTF-8"))
                    .append("&api_key=tFfFOWlhU9zByM04PJAcm56wpAdNsNXkwrQ0WSEg").toString();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL,null,
                    (response) -> {
                            try {
                                JSONArray jsonArray = response.getJSONArray("photos");

                                final int len = jsonArray.length();

                                for (int i = 0; i < len; i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    JSONObject camera = jsonObject.getJSONObject("camera");
                                    String cameraName = camera.getString("full_name");
                                    JSONObject rover = jsonObject.getJSONObject("rover");
                                    String roverName = rover.getString("name");
                                    String imageUrl = jsonObject.getString("img_src");
                                    int id = jsonObject.getInt("id");
                                    Picture photo = new Picture(id, imageUrl, roverName, cameraName);

                                    ImageRequest imageRequest = new ImageRequest(redirectUrl(imageUrl), imgResponse -> {
                                        photo.setPhoto(imgResponse);
                                        photos.add(photo);
                                        if (photos.size() == len) {
                                            callback.onPhotosLoaded(photos);
                                        }
                                    }, 0,0, ImageView.ScaleType.CENTER, null,
                                            (error)->{
                                                error.printStackTrace();
                                                //Toast.makeText(NasaPhotos.this, ""+error, Toast.LENGTH_SHORT).show();
                                            });
                                    requestQueue.add(imageRequest);

                                }



                            }
                            catch (JSONException  e) { e.printStackTrace(); }
                    },
                    (error) -> { });
            requestQueue.add(request);

    } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
    }



}

static String redirectUrl(String url){
      return url.replace("http://mars.jpl.nasa.gov/","https://mars.nasa.gov/");
}




public interface PhotosCallback {
    void onPhotosLoaded(ArrayList<Picture> photos);
}

}