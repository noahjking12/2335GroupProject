package algonquin.cst2335.a2335groupproject;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Pictures {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="ID")
    public long id;
    @ColumnInfo(name="Date")
    String date;
    @ColumnInfo(name="IsSearchButton")
    boolean isSearchButton;

    Pictures(String date,  boolean isSearchButton)
    {
        this.date=date;
        this.isSearchButton=isSearchButton;
    }

    public String getDate(){
       return date;
    }

    public boolean getIsSearchButton(){
        return isSearchButton;
    }

}
