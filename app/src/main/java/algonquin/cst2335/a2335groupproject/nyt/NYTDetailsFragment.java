package algonquin.cst2335.a2335groupproject.nyt;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import algonquin.cst2335.a2335groupproject.databinding.DetailsNytBinding;


public class NYTDetailsFragment extends Fragment {


    Articles articleTopic;

    public NYTDetailsFragment(Articles article){
        articleTopic = article;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        DetailsNytBinding binding = DetailsNytBinding.inflate(inflater);

        binding.detailTopic.setText("Searched topic: "+articleTopic.message);
        binding.topicID.setText("History order: "+(int) articleTopic.id);

     return binding.getRoot();
    }
}
