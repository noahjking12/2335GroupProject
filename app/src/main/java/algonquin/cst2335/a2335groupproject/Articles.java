package algonquin.cst2335.a2335groupproject;


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

    Articles(String message, boolean isSentButton)
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
