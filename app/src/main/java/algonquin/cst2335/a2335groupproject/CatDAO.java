package algonquin.cst2335.a2335groupproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import algonquin.cst2335.a2335groupproject.kitten.CatList;

/** DAO interface for performing CRUD operations
 * @author Yalin Su
 * @version 1.0
 */
@Dao
public interface CatDAO {

    /**
     * insert a CatList record into the database
     * @param cat the CatList object to be inserted
     * @return the id of the newly inserted record
     */
    @Insert
    long insertImage(CatList cat);

    /**
     * get all records from the CatList table
     * @return a list of CatList objects representing all record in the table
     */
    @Query("select * from CatList")
    List<CatList> getALLImages();

    /**
     * delete a record from the CatList table
     * @param cat cat
     */
    @Delete
    void deleteImages(CatList cat);
}
