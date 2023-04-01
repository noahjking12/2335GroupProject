package algonquin.cst2335.a2335groupproject.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.a2335groupproject.kitten.CatList;

public class CatListViewModel extends ViewModel {
    public MutableLiveData<ArrayList<CatList>> catViewModel = new MutableLiveData<>();

    public MutableLiveData<CatList> selectedImage = new MutableLiveData<>();
}
