package algonquin.cst2335.a2335groupproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PicturesDAO {
    @Insert
    public long insertMessage(Pictures m);
    @Query("Select * from Pictures")
    public List<Pictures> getAllMessages();
    @Delete
    public  void deleteMessage(Pictures m);
}
