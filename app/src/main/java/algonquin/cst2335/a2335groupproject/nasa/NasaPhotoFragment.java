package algonquin.cst2335.a2335groupproject.nasa;


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
    public NasaPhotoFragment(Picture picture) {
        this.selectedPicture = picture;
        this.type = "searched";
    }
    public NasaPhotoFragment(Picture picture, String type) {
        this.selectedPicture = picture;
        this.type = type;
    }

    public static NasaPhotoFragment newInstance(Picture picture) {

        return new NasaPhotoFragment(picture);
    }

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