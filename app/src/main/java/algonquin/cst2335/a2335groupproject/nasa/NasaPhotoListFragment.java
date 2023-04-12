package algonquin.cst2335.a2335groupproject.nasa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.a2335groupproject.Picture;
import algonquin.cst2335.a2335groupproject.R;
import algonquin.cst2335.a2335groupproject.data.NasaPhotoViewModel;
import algonquin.cst2335.a2335groupproject.data.PicturesDatabase;
import algonquin.cst2335.a2335groupproject.data.SavedViewModel;
import algonquin.cst2335.a2335groupproject.data.SearchViewModel;
import algonquin.cst2335.a2335groupproject.databinding.FragmentNasaPhotosBinding;

public class NasaPhotoListFragment extends Fragment {
    private ArrayList<Picture> pictures;
    private ViewModel viewModel;
    private NasaPhotoViewModel photoViewModel;
    private String type = "s";
    private FragmentManager fragmentManager;
    private NasaAdapter myAdapter;
    private FragmentNasaPhotosBinding binding;
    public NasaPhotoListFragment() {
    }


    /**
     *
     * @param type the type of the NASA photos to display ("s" for search results or "saved" for saved photos).
     * @param fragmentManager the FragmentManager used to manage the fragments within the activity.
     * @param viewModel the ViewModel that contains the list of NASA photos.
     * @param photoViewModel  the NasaPhotoViewModel used to communicate between the fragments and the activity.
     */
    public NasaPhotoListFragment(String type, FragmentManager fragmentManager, ViewModel viewModel, NasaPhotoViewModel photoViewModel ) {
        if(type == "s"){
            this.pictures = SearchViewModel.class.cast(viewModel).pictures.getValue();
        }
        else{
            this.pictures = SavedViewModel.class.cast(viewModel).pictures.getValue();
        }

        this.type = type;
        this.fragmentManager = fragmentManager;
        this.viewModel = viewModel;
        this.photoViewModel = photoViewModel;

    }

    /**
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState This fragment is being re-constructed from a previous saved state as given here.
     * @return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentNasaPhotosBinding.inflate(inflater);
        binding.recycleViewFirstPage.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.recycleViewFirstPage.setAdapter(myAdapter = new NasaAdapter(pictures,fragmentManager));

        return binding.getRoot();
    }


    class NasaAdapter extends RecyclerView.Adapter<NasaRowHolder> {
        private ArrayList<Picture> ps;
        private FragmentManager fragmentManager;

        public NasaAdapter(ArrayList<Picture> pictures, FragmentManager fragmentManager) {
            this.ps = pictures;
            this.fragmentManager = fragmentManager;
        }

        /**
         *
         * @param parent
         * @param viewType
         * @return new NasaRowHolder(itemView)
         */

        @NonNull
        @Override
        public NasaRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.picture_thumbnail, parent, false);

            return new NasaRowHolder(itemView);
        }

        /**
         *
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(@NonNull NasaRowHolder holder, int position) {

                Picture photo = ps.get(position);
                holder.roverName.setText(photo.getRoverName());
                holder.imageView.setImageBitmap(photo.getPhoto().getBitmap());

        }

        /**
         *
         * @return null
         */
        @Override
        public int getItemCount() {
            return ps == null ? 0:ps.size();
        }


    }

    class NasaRowHolder extends RecyclerView.ViewHolder {


        ImageView imageView;
        TextView roverName;

        /**
         *
         * @param v
         */
        public void onClick(View v){
            int position = getAdapterPosition();
            Picture picture = pictures.get(position);
            NasaPhotoFragment fragment2 = NasaPhotoFragment.newInstance(picture);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.nasa_fragment, fragment2);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        public NasaRowHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.thumbnail);
            roverName = itemView.findViewById(R.id.rover);

            itemView.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(NasaPhotoListFragment.this.getContext());
                if (type == "h") {
                    SavedViewModel svm = (SavedViewModel)viewModel;
                    Picture selectedSaved = svm.pictures.getValue().get(position);
                    builder.setMessage("Take action!" + roverName.getText())
                            .setTitle("Action")
                            /*.setPositiveButton("Delete", (dialog, which) -> {
                                Executor thread = Executors.newSingleThreadExecutor();
                                thread.execute(() -> {
                                    PicturesDatabase db = NasaActivity.db;
                                    db.picDAO().deletePicture(selectedSaved);
                                    ArrayList<Picture> currentSavedPhotos = svm.pictures.getValue();
                                    currentSavedPhotos.remove(selectedSaved);
                                    svm.pictures.postValue(currentSavedPhotos);



                                });
                                myAdapter.notifyItemRemoved(position); //update the recycleview
                            })*/.setPositiveButton("Detail", (dialog, which) -> {
                                photoViewModel.picture.postValue(selectedSaved);
                                photoViewModel.picture.observe(NasaPhotoListFragment.this, (newPhoto) -> {
                                    FragmentManager fMgr = NasaActivity.fragmentManager;
                                    FragmentTransaction tx = fMgr.beginTransaction();
                                    NasaPhotoFragment photoFragment = new NasaPhotoFragment(newPhoto,"saved");

                                    tx.replace(R.id.nasa_fragment, photoFragment, "nasa_photo");
                                    tx.addToBackStack(null);
                                    tx.commit();
                                });
                            })
                            .setNegativeButton("Cancel", (dialog, cl) -> {
                            })
                            .create().show();

                } else {
                    SearchViewModel searchViewModel = (SearchViewModel)viewModel;
                    Picture selectedPhoto = searchViewModel.pictures.getValue().get(position);
                    builder.setMessage("Do you want to See full size picture: " + roverName.getText())
                            .setTitle("Question")
                            .setPositiveButton("OK", (dialog, which) -> {
                                // Pass the photos list to another activity


                                photoViewModel.picture.postValue(selectedPhoto);
                                photoViewModel.picture.observe(NasaPhotoListFragment.this, (newPhoto) -> {
                                    FragmentManager fMgr = NasaActivity.fragmentManager;
                                    FragmentTransaction tx = fMgr.beginTransaction();
                                    NasaPhotoFragment photoFragment = new NasaPhotoFragment(newPhoto, "searched");

                                    tx.replace(R.id.nasa_fragment, photoFragment, "nasa_photo");
                                    tx.addToBackStack(null);
                                    tx.commit();
                                });


                            })

                            .setNegativeButton("Cancel", (dialog, cl) -> {
                            })
                            .create().show();
                }


            });
        }

    }
}
