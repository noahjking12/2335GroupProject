package algonquin.cst2335.a2335groupproject.data;
/**
 * @author Jinwei Li 040818950
 * @version 1.0
 */
import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;

import algonquin.cst2335.a2335groupproject.Picture;


public class SearchViewModel extends ViewModel {
    /** Used to store saved forecast data for NasaActivity */
    public MutableLiveData<ArrayList<Picture>> pictures = new MutableLiveData<ArrayList<Picture>>();
    /** Used to store the selected forecast that is being loaded into a fragment */
    public MutableLiveData<String> date = new MutableLiveData<>();


}
