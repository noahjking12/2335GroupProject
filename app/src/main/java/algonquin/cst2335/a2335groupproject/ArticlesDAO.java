package algonquin.cst2335.a2335groupproject;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import algonquin.cst2335.a2335groupproject.nyt.ArticleSource;
import algonquin.cst2335.a2335groupproject.nyt.Articles;
/**
 * This interface allows users to manipulate the topics searched.
 * @author Jiale Zhang
 */
@Dao
public interface ArticlesDAO {
    /**
     *
     * @param topic user input
     * @return store the data into database
     */
    @Insert
    public long insertMessage(Articles topic);

    /**
     *
     * @return perform the query and get information from database
     */
    @Query("Select * from Articles")
    public List<Articles> getAllMessages();

    /**
     *
     * @param topic user input
     */
    @Delete
    public  void deleteMessage(Articles topic);
}
