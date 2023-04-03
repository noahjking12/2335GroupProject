package algonquin.cst2335.a2335groupproject.nyt;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import algonquin.cst2335.a2335groupproject.nyt.Articles;
import algonquin.cst2335.a2335groupproject.ArticlesDAO;
/**
 * This database stores the elements of articles.
 * @author Jiale Zhang
 */

@Database(entities = {ArticleSource.class},version = 1)
public abstract class ArticleSourceDatabase extends RoomDatabase{
    /**
     *
     * @return ArticleSourceDAO
     */
    public abstract ArticleSourceDAO cmDAO();

}