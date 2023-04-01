package algonquin.cst2335.a2335groupproject.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import algonquin.cst2335.a2335groupproject.CatDAO;
import algonquin.cst2335.a2335groupproject.kitten.CatList;

@Database(entities = {CatList.class}, version = 1)
public abstract class CatImageDatabase extends RoomDatabase {
    public abstract CatDAO catDAO();
}
