package algonquin.cst2335.a2335groupproject.data;
/**
 * @author Jinwei Li 040818950
 * @version 1.0
 */
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.a2335groupproject.Picture;


public class SavedViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Picture>> pictures = new MutableLiveData<ArrayList<Picture>>();

    /**
     *
     * @param p
     */
    public void addPicture(Picture p) {
        if (p != null) {
            ArrayList<Picture> list = pictures.getValue();
            if (list == null) {
                list = new ArrayList<>();
                pictures.setValue(list);
            }
            list.add(p);
        }
    }

    /**
     *
     * @param p
     */
    public void removePicture(Picture p) {
        if (p != null) {
            ArrayList<Picture> list = pictures.getValue();
            if (list != null) {
                list.remove(p);
                pictures.setValue(list);
            }
        }
    }
}