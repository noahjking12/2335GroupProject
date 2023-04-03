package algonquin.cst2335.a2335groupproject.nyt;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ArticleSourceDAO {
//    @Insert
//    public long insertMessage(ArticleSource m);


    @Query("Select * from ArticleSource")
    public List<ArticleSource> getAllMessages();
    @Delete
    public  void deleteMessage(ArticleSource m);
}