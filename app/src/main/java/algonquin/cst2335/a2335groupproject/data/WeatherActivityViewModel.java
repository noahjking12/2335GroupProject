package algonquin.cst2335.a2335groupproject.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.a2335groupproject.Forecast;

/** ViewModel used to store data for WeatherActivity page
 * @author Noah King
 * @version 1.0
 */
public class WeatherActivityViewModel extends ViewModel {

    /** Used to store saved forecast data for WeatherActivity */
    public MutableLiveData<ArrayList<Forecast>> savedForecasts = new MutableLiveData<>();
}
