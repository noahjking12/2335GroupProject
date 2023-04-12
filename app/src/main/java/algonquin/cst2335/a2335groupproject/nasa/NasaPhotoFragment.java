package algonquin.cst2335.a2335groupproject.nasa;
/**
 * @author Jinwei Li
 * @version 1.0
 */

import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.a2335groupproject.MainActivity;
import algonquin.cst2335.a2335groupproject.Picture;
import algonquin.cst2335.a2335groupproject.data.PicturesDatabase;
import algonquin.cst2335.a2335groupproject.data.SavedViewModel;
import algonquin.cst2335.a2335groupproject.databinding.FragmentNasaPhotoBinding;


public class NasaPhotoFragment extends Fragment {
    private String type;
    Picture selectedPicture = new Picture();


    public NasaPhotoFragment( ) {

    }

    /**
     * selected pic from th user
     * @param picture picture
     */
    public NasaPhotoFragment(Picture picture) {
        this.selectedPicture = picture;
        this.type = "searched";
    }

    /**
     *
     * @param picture selected pic from th user
     * @param type the type of the pic
     */
    public NasaPhotoFragment(Picture picture, String type) {
        this.selectedPicture = picture;
        this.type = type;
    }

    /**
     *
     * @param picture picture
     * @return new NasaPhotoFragment(picture)
     */
    public static NasaPhotoFragment newInstance(Picture picture) {

        return new NasaPhotoFragment(picture);
    }

    /**
     *
     * @param inflater Inflates the layout for this fragment, and sets the picture details and options to save/delete the picture.
     * @param container  If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should
     *                   not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState  If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The root View of the fragment's layout
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FragmentNasaPhotoBinding binding = FragmentNasaPhotoBinding.inflate(inflater);
        if(this.selectedPicture.getPhoto() != null){
            binding.image.setImageBitmap(this.selectedPicture.getPhoto().getBitmap());
            binding.rover.setText(this.selectedPicture.getRoverName());
            binding.camera.setText(this.selectedPicture.getCameraName());
            binding.link.setText(this.selectedPicture.getImgSrc());
            if(type == "saved"){
                binding.save.setVisibility(View.GONE);
            }
            else{
                binding.delete.setVisibility(View.GONE);
            }

            //save picture
            binding.save.setOnClickListener(click->{
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Save Confirmation")
                        .setMessage("Do you want to save the photo?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Positive button clicked
                                Executor thread = Executors.newSingleThreadExecutor();
                                thread.execute(()->{
                                    PicturesDatabase db = NasaActivity.db;
                                    db.picDAO().insertPicture(selectedPicture);


                                    savePhotoToFile(selectedPicture);
                                    ((NasaActivity)getActivity()).savedViewModel.addPicture(selectedPicture);
                                });
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Negative button clicked
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

            });

            //delete picture
            binding.delete.setOnClickListener(click->{
                Executor thread = Executors.newSingleThreadExecutor();
                thread.execute(() -> {
                    PicturesDatabase db = NasaActivity.db;
                    db.picDAO().deletePicture(selectedPicture);
                    ((NasaActivity)getActivity()).savedViewModel.removePicture(selectedPicture);
                });
                ((NasaActivity)getActivity()).onBackPressed();
            });

            //click to open photo in web browser

            binding.link.setOnClickListener(clik->{
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedPicture.getImgSrc()));
                startActivity(intent);
            });
        }

        return binding.getRoot();
    }

    /**
     * Saves the selected picture's photo to a file
     * with the format of JPG in the internal storage directory of the app.
     * @param selectedPicture the picture to be saved to a file.
     */
    private void savePhotoToFile(Picture selectedPicture) {
        int id = selectedPicture.getId();
        Bitmap image = selectedPicture.getPhoto().getBitmap();
        File file = new File(getContext().getFilesDir(), ""+id+".jpg");
        System.out.println(file.getAbsolutePath());
        try{
            FileOutputStream fos = new FileOutputStream(file);

            // Compress the bitmap image and write it to the output stream
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            // Close the output stream
            fos.close();
        }catch (Exception e){e.printStackTrace();}
    }



}