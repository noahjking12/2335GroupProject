package algonquin.cst2335.a2335groupproject.data;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;

import algonquin.cst2335.a2335groupproject.Picture;


public class SearchViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Picture>> pictures = new MutableLiveData<ArrayList<Picture>>();

    public MutableLiveData<String> date = new MutableLiveData<>();


}
