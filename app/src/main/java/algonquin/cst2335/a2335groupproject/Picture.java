package algonquin.cst2335.a2335groupproject;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import algonquin.cst2335.a2335groupproject.nasa.Photo;

@Entity
public class Picture implements Serializable {


    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name="ID")
    private int id;
    @ColumnInfo(name="ImageUrl")
    private String imgSrc;
    @ColumnInfo(name="RoverName")
    private String roverName;
    @ColumnInfo(name="CameraName")
    private String cameraName;



    @Ignore
    private Photo photo;

    public Picture(){}
    @Ignore
    // Constructor
    /**
     *
     */
    public Picture(int id, String imgSrc, String roverName, String cameraName) {


        this.id = id;
        this.imgSrc = imgSrc;
        this.roverName = roverName;
        this.cameraName = cameraName;
    }

    // Getters and setters

    /**
     *
     * @return id
     */
    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getRoverName() {
        return roverName;
    }

    public void setRoverName(String roverName) {
        this.roverName = roverName;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }
    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap bitmap) {
        this.photo = new Photo(bitmap);
    }

}
