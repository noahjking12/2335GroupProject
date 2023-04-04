package algonquin.cst2335.a2335groupproject;

import android.os.Parcelable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PicturesDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertPicture(Picture m);
    @Query("Select * from Picture")
    public List<Picture> getAllPictures();
    @Delete
    public  void deletePicture(Picture m);
}
