package algonquin.cst2335.a2335groupproject.nyt;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.a2335groupproject.databinding.DetailsNytBinding;
/**
 * This class creates method for fragment.
 * @author Jiale Zhang
 */

public class NYTDetailsFragment extends Fragment {


    ArticleSource articleTopic;

    /**
     *
     * @param article article details
     */
    public NYTDetailsFragment(ArticleSource article){
        articleTopic = article;
    }

    /**
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return binding.getRoot()
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        DetailsNytBinding binding = DetailsNytBinding.inflate(inflater);

        binding.detailTopic.setText(articleTopic.leadParagraph);
     return binding.getRoot();
    }
}
