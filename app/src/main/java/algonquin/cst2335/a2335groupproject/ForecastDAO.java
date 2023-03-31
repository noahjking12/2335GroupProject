package algonquin.cst2335.a2335groupproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/** DAO interface for performing CRUD operations that act on a user's saved forecasts
 * @author Noah King
 * @version 1.0
 */
@Dao
public interface ForecastDAO {

    /** Insert a new Forecast into the database
     * @param forecast The Forecast to be saved
     * @return The database generated id for the Forecast
     */
    @Insert
    public long insertForecast(Forecast forecast);

    /** Get all saved Forecasts from the database
     * @return A List of all of the user's saved Forecasts
     */
    @Query("Select * from Forecast")
    public List<Forecast> getAllForecasts();

    /** Delete a given Forecast from the database
     * @param forecast The Forecast to be deleted
     */
    @Delete
    public void deleteForecast(Forecast forecast);
}
