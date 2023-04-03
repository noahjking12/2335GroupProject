package algonquin.cst2335.a2335groupproject.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import algonquin.cst2335.a2335groupproject.CatDAO;
import algonquin.cst2335.a2335groupproject.kitten.CatList;

/** This class defines the schema and version for the database and provides access to the catDAO interface
 * @author Yalin Su
 * @version 1.0
 */
@Database(entities = {CatList.class}, version = 1)
public abstract class CatImageDatabase extends RoomDatabase {
    /**
     * abstract method to get an instance of the CatDAO interface for database operations
     * @return an instance of CatDAO
     */
    public abstract CatDAO catDAO();
}
