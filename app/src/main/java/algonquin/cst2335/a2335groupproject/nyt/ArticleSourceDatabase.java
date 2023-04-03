package algonquin.cst2335.a2335groupproject.nyt;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import algonquin.cst2335.a2335groupproject.nyt.Articles;
import algonquin.cst2335.a2335groupproject.ArticlesDAO;


@Database(entities = {ArticleSource.class},version = 1)
public abstract class ArticleSourceDatabase extends RoomDatabase{
    public abstract ArticleSourceDAO cmDAO();

}