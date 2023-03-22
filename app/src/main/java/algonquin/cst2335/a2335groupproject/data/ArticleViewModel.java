package algonquin.cst2335.a2335groupproject.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.a2335groupproject.Articles;


public class ArticleViewModel extends ViewModel {

    public MutableLiveData<ArrayList<Articles>> messages = new MutableLiveData< >();

}
