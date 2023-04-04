package algonquin.cst2335.a2335groupproject;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/** Creates the Forecast database on the disk of the device
 * @author Noah King
 * @version 1.0
 */
@Database(entities = {Forecast.class}, version = 1)
public abstract class ForecastDatabase extends RoomDatabase {

    /** Provides a DAO for interacting with this database
     * @return The ForecastDAO for interacting with this database
     */
    public abstract ForecastDAO forecastDAO();
}
