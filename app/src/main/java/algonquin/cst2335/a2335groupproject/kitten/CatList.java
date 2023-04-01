package algonquin.cst2335.a2335groupproject.kitten;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CatList {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;
    @ColumnInfo(name = "catUrl")
    private String catUrl;
    @ColumnInfo(name = "width")
    private String width;
    @ColumnInfo(name = "height")
    private String height;
    @ColumnInfo(name = "dateSaved")
    private String dateSaved;

    public CatList(String catUrl, String width, String height, String dateSaved){
        this.catUrl = catUrl;
        this.width = width;
        this.height = height;
        this.dateSaved = dateSaved;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }
    public  String getCatUrl(){
        return catUrl;
    }

    public String getWidth(){
        return width;
    }

    public String getHeight(){
        return height;
    }
    public String getDateSaved(){
        return dateSaved;
    }

}
