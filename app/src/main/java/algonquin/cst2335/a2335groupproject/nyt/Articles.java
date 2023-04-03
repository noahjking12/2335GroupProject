package algonquin.cst2335.a2335groupproject.nyt;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
/**
 * This class allows users to get the topics searched.
 * @author Jiale Zhang
 */
@Entity
public class Articles {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="ID")

    public long id;
    @ColumnInfo(name="Topic")
    String message;

    boolean isSentButton;

    /**
     *
     * @param message input from users
     * @param isSentButton whether is searched or not
     */
    public Articles(String message, boolean isSentButton)
    {
        this.message=message;
        this.isSentButton=isSentButton;
    }

    /**
     *
     * @return message
     */
    public String getMessage(){
       return message;
    }

    /**
     *
     * @return isSentButton
     */
    public boolean getIsSentButton(){
        return isSentButton;
    }

}
