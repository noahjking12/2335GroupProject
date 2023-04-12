package algonquin.cst2335.a2335groupproject;
/**
 * @author Jinwei Li
 */

import android.os.Parcelable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao

public interface PicturesDAO {
    /**
     *
     * @param m Picture m
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertPicture(Picture m);

    /**
     *
     * @return pictures
     */
    @Query("Select * from Picture")
    public List<Picture> getAllPictures();

    /**
     *
     * @param m Picture m
     */
    @Delete
    public  void deletePicture(Picture m);

}
