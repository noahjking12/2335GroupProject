package algonquin.cst2335.a2335groupproject.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import algonquin.cst2335.a2335groupproject.Picture;

/**
 * This view model allows users to get live data from NasaActivity and Photo.
 * @author Jinwei Li
 */
public class NasaPhotoViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    public MutableLiveData<Picture> picture = new MutableLiveData< >();
}