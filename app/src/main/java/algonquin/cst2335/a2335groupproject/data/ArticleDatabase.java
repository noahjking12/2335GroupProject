package algonquin.cst2335.a2335groupproject.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import algonquin.cst2335.a2335groupproject.nyt.Articles;
import algonquin.cst2335.a2335groupproject.ArticlesDAO;

/**
 * This database stores the topics searched.
 * @author Jiale Zhang
 */

@Database(entities = {Articles.class},version = 1)
public abstract class ArticleDatabase extends RoomDatabase{
    /**
     *
     * @return database from ArticleDAO
     */
    public abstract ArticlesDAO cmDAO();

}
