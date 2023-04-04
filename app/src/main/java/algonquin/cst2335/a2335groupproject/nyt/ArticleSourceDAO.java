package algonquin.cst2335.a2335groupproject.nyt;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
/**
 * This interface is used for the article elements from API.
 * @author Jiale Zhang
 */
@Dao
public interface ArticleSourceDAO {
//    @Insert
//    public long insertMessage(ArticleSource m);

    /**
     *
     * @return ArticleSource
     */
    @Query("Select * from ArticleSource")
    public List<ArticleSource> getAllMessages();

}