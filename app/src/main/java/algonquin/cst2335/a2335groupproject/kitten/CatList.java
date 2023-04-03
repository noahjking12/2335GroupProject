package algonquin.cst2335.a2335groupproject.kitten;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/** This is an entity class that represents a single record of the CatList in the database
 * @author Yalin Su
 * @version 1.0
 */
@Entity
public class CatList {

    /**
     * auto-generated identifier in the database
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    /**
     * kitten image url of the CatList
     */
    @ColumnInfo(name = "catUrl")
    private String catUrl;

    /**
     * the width of the kitten image
     */
    @ColumnInfo(name = "width")
    private String width;

    /**
     * the height of the kitten image
     */
    @ColumnInfo(name = "height")
    private String height;

    /**
     * Date of the kitten image was saved
     */
    @ColumnInfo(name = "dateSaved")
    private String dateSaved;

    /**
     * constructor for creating a CatList object
     * @param catUrl the url of the kitten image
     * @param width the width of the kitten image
     * @param height the height of the kitten image
     * @param dateSaved the date of the kitten image was saved
     */

    public CatList(String catUrl, String width, String height, String dateSaved){
        this.catUrl = catUrl;
        this.width = width;
        this.height = height;
        this.dateSaved = dateSaved;
    }

    /**
     * the id of the CatList record
     * @return the id of the record
     */
    public long getId(){
        return id;
    }

    /**
     * set the id of the CatList record
     * @param id the id to be set for the record
     */
    public void setId(long id){
        this.id = id;
    }

    /**
     * the url of the kitten image
     * @return the url of the kitten image
     */
    public  String getCatUrl(){
        return catUrl;
    }

    /**
     * the width of the kitten image
     * @return the width of the kitten image
     */
    public String getWidth(){
        return width;
    }

    /**
     * the height of the kitten image
     * @return the height of the kitten image
     */
    public String getHeight(){
        return height;
    }

    /**
     * the date of the kitten image was saved
     * @return the date of the kitten image was saved
     */
    public String getDateSaved(){
        return dateSaved;
    }

}
