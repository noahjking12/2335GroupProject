package algonquin.cst2335.a2335groupproject.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.a2335groupproject.Pictures;

public class SearchViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Pictures>> messages = new MutableLiveData< >();

    public MutableLiveData<Pictures> selectMessage = new MutableLiveData<>();

}
