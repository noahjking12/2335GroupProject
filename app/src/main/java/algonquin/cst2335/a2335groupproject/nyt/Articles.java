package algonquin.cst2335.a2335groupproject.nyt;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Articles {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="ID")

    public long id;
    @ColumnInfo(name="Topic")
    String message;

    boolean isSentButton;

    public Articles(String message, boolean isSentButton)
    {
        this.message=message;
        this.isSentButton=isSentButton;
    }

    public String getMessage(){
       return message;
    }


    public boolean getIsSentButton(){
        return isSentButton;
    }

}
