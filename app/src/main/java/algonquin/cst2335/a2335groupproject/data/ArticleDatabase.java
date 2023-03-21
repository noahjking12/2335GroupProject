package algonquin.cst2335.a2335groupproject.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import algonquin.cst2335.a2335groupproject.Articles;
import algonquin.cst2335.a2335groupproject.ArticlesDAO;


@Database(entities = {Articles.class},version = 1)
public abstract class ArticleDatabase extends RoomDatabase{
    public abstract ArticlesDAO cmDAO();

}
