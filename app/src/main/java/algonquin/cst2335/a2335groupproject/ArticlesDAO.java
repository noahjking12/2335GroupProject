package algonquin.cst2335.a2335groupproject;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import algonquin.cst2335.a2335groupproject.nyt.ArticleSource;
import algonquin.cst2335.a2335groupproject.nyt.Articles;

@Dao
public interface ArticlesDAO {
    @Insert
    public long insertMessage(Articles m);
    @Query("Select * from Articles")
    public List<Articles> getAllMessages();
    @Delete
    public  void deleteMessage(Articles m);
}
