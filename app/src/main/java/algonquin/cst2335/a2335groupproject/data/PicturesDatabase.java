package algonquin.cst2335.a2335groupproject.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import algonquin.cst2335.a2335groupproject.Pictures;
import algonquin.cst2335.a2335groupproject.PicturesDAO;

@Database(entities = {Pictures.class},version = 1)
public abstract class PicturesDatabase extends RoomDatabase{
    public abstract PicturesDAO cmDAO();

}
