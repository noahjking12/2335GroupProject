package algonquin.cst2335.a2335groupproject.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.a2335groupproject.nyt.ArticleSource;
import algonquin.cst2335.a2335groupproject.nyt.Articles;


public class ArticleViewModel extends ViewModel {

    public MutableLiveData<ArrayList<Articles>> messages = new MutableLiveData<>();
    public MutableLiveData<ArrayList<ArticleSource>> articles = new MutableLiveData< >();
public MutableLiveData<Articles> SelectedTopic=new MutableLiveData<>();
}
