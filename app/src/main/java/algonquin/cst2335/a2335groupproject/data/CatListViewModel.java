package algonquin.cst2335.a2335groupproject.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.a2335groupproject.kitten.CatList;

/** This is the ViewModel class that store data for SecondActivity and KittenHolder2 page
 * @author Yalin Su
 * @version 1.0
 */
public class CatListViewModel extends ViewModel {
    /**
     * used to hold the list of kitten images
     */
    public MutableLiveData<ArrayList<CatList>> catViewModel = new MutableLiveData<>();

    /**
     * used to hold the currently selected kitten image
     */
    public MutableLiveData<CatList> selectedImage = new MutableLiveData<>();
}
