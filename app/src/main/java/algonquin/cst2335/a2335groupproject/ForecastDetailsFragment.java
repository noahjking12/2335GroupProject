package algonquin.cst2335.a2335groupproject;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;

import algonquin.cst2335.a2335groupproject.databinding.ForecastDetailsLayoutBinding;
import algonquin.cst2335.a2335groupproject.ui.WeatherActivity;

/** Fragment that displays whatever Forecast a user selects from their saved forecasts
 * @author Noah King
 * @version 1.0
 */
public class ForecastDetailsFragment extends Fragment {

    /** The Forecast to display in the fragment */
    Forecast selected;

    /** Constructs a new ForecastDetailsFragment using the Forecast you wish to display
     * @param selected
     */
    public ForecastDetailsFragment(Forecast selected) {
        this.selected = selected;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        ForecastDetailsLayoutBinding binding = ForecastDetailsLayoutBinding.inflate(inflater); // get binding

        // Get strings from res/string
        String resFeelsLike = getResources().getString(R.string.feels_like);
        String resHumidity = getResources().getString(R.string.humidity);
        String resUvIndex = getResources().getString(R.string.uv_index);
        String resWindSpeed = getResources().getString(R.string.wind);
        String resVisibility = getResources().getString(R.string.visibility);

        // Display the selected forecasts data in the fragment
        binding.fragmentCityName.setText(selected.getCity());
        binding.fragmentWeatherDesc.setText(selected.getDescription());
        binding.fragmentTemp.setText(Integer.toString(selected.getTemperature()));
        binding.fragmentFeelsLike.setText(resFeelsLike + Integer.toString(selected.getFeelsLike()));
        binding.fragmentHumidity.setText(resHumidity + Integer.toString(selected.getHumidity()) + "%");
        binding.fragmentUvIndex.setText(resUvIndex + Integer.toString(selected.getUvIndex()));
        binding.fragmentWindSpeed.setText(resWindSpeed + Integer.toString(selected.getWindSpeed()) + "km/h");
        binding.fragmentVisibility.setText(resVisibility + Integer.toString(selected.getVisibility()));

        // Load icon from files and display it
        String pathname = getContext().getFilesDir() + "/" + selected.getIcon();
        File file = new File(pathname);

        if (file.exists()) {
            Bitmap forecastIcon = BitmapFactory.decodeFile(pathname);
            binding.fragmentWeatherIcon.setImageBitmap(forecastIcon);
        } // If can't find icon, display without

        return binding.getRoot();
    }
}
