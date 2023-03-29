package algonquin.cst2335.a2335groupproject.nyt;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ArticleSource {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="ID")

    public long id;
    @ColumnInfo(name="Headline")
    String headline;
    @ColumnInfo(name="Byline")
    String byline;
    @ColumnInfo(name="Abstract")
    String abstracts;
    @ColumnInfo(name="Link")
    String webLink;


    ArticleSource(String headline, String byline,String abstracts,String webLink)
    {
        this.headline=headline;
        this.byline=byline;
        this.abstracts=abstracts;
        this.webLink=webLink;


    }

    public String getHeadline(){
        return headline;
    }

    public String getByline(){
        return byline;
    }
    public String getAbstracts(){
        return abstracts;
    }
    public String getWebLink(){
        return webLink;
    }}
