package algonquin.cst2335.a2335groupproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;


import algonquin.cst2335.a2335groupproject.databinding.KittenDetailsFragmentBinding;
import algonquin.cst2335.a2335groupproject.kitten.CatList;

/** This is a Fragment class that displaying detail info of a selected kitten image by the user
 * It shows the kitten image with its width, height and the date was loaded
 * @author Yalin Su
 * @version 1.0
 */
public class CatImageDetailsFragment extends Fragment {

    /**
     * the CatList object representing the selected kitten image
     */
    CatList selected;
    /**
     * binding object for the KittenDetailsFragmentBinding layout
     */
    KittenDetailsFragmentBinding detailsBinding;

    /**
     * constructor for the CatImageDetailsFragment class
     * initializes the selected CatList object
     * @param catList the CatList object representing the selected kitten image
     */
    public CatImageDetailsFragment(CatList catList){
        selected = catList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflates the KittenDetailsFragment layout
        detailsBinding = KittenDetailsFragmentBinding.inflate(inflater);

        String widthText = getResources().getString(R.string.kitten_toastMessage_width);
        String heightText = getResources().getString(R.string.kitten_toastMessage_height);
        // loads the selected kitten image using Glide
        Glide.with(this)
                        .load( selected.getCatUrl() )
                        .into(detailsBinding.detailImageView);

        // sets the width, height and date loaded for the textViews
        detailsBinding.widthText.setText( widthText + " " + selected.getWidth() );
        detailsBinding.heightText.setText( heightText + " " + selected.getHeight() );
        detailsBinding.dateText.setText( selected.getDateSaved() );

        return detailsBinding.getRoot();
    }
}
