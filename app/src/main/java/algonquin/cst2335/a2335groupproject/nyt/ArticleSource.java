package algonquin.cst2335.a2335groupproject.nyt;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
/**
 * This class allows users to get elements from API.
 * @author Jiale Zhang
 */
@Entity
public class ArticleSource extends ArrayList<ArticleSource> {
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
    @ColumnInfo(name="lead_paragraph")
    String leadParagraph;

    /**
     *
     * @param headline headline from New York Times API
     * @param byline author from New York Times API
     * @param abstracts abstract from New York Times API
     * @param webLink url link to the article
     * @param leadParagraph lead paragraph from New York Times API
     */
    public ArticleSource(String headline, String byline, String abstracts, String webLink,String leadParagraph)
    {
        this.headline=headline;
        this.byline=byline;
        this.abstracts=abstracts;
        this.webLink=webLink;
        this.leadParagraph=leadParagraph;


    }

    /**
     *
     * @return headline headline from New York Times API
     */
    public String getHeadline(){
        return headline;
    }

    /**
     *
     * @return byline author from New York Times API
     */
    public String getByline(){
        return byline;
    }

    /**
     *
     * @return abstracts abstract from New York Times API
     */
    public String getAbstracts(){
        return abstracts;
    }

    /**
     *
     * @return webLink url link to the article
     */
    public String getWebLink(){
        return webLink;
    }

    /**
     *
     * @return leadParagraph lead paragraph from New York Times API
     */
    public String getleadParagraph(){
        return leadParagraph;
    }}
