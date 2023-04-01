package algonquin.cst2335.a2335groupproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import algonquin.cst2335.a2335groupproject.databinding.KittenDetailsFragmentBinding;
import algonquin.cst2335.a2335groupproject.kitten.CatList;

public class CatImageDetailsFragment extends Fragment {

    CatList selected;
    KittenDetailsFragmentBinding detailsBinding;
    ArrayList<CatList> imageUrlList;
    CatDAO catDAO;

    public CatImageDetailsFragment(CatList catList){
        selected = catList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        detailsBinding = KittenDetailsFragmentBinding.inflate(inflater);

        Glide.with(this)
                        .load( selected.getCatUrl() )
                        .into(detailsBinding.detailImageView);

        detailsBinding.widthText.setText( "The width of this image is: "+ selected.getWidth() );
        detailsBinding.heightText.setText( "The height of this image is: " + selected.getHeight() );
        detailsBinding.dateText.setText( selected.getDateSaved() );

        return detailsBinding.getRoot();
    }
}
