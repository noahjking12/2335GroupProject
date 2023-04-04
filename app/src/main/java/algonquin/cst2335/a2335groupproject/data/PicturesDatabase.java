package algonquin.cst2335.a2335groupproject.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import algonquin.cst2335.a2335groupproject.Picture;
import algonquin.cst2335.a2335groupproject.PicturesDAO;

@Database(entities = {Picture.class},version = 1, exportSchema = false)
public abstract class PicturesDatabase extends RoomDatabase{
    public abstract PicturesDAO picDAO();

}
