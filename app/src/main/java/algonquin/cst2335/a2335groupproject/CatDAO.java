package algonquin.cst2335.a2335groupproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import algonquin.cst2335.a2335groupproject.kitten.CatList;

@Dao
public interface CatDAO {

    @Insert
    long insertImage(CatList cat);
    @Query("select * from CatList")
    List<CatList> getALLImages();
    @Delete
    void deleteImages(CatList cat);
}
