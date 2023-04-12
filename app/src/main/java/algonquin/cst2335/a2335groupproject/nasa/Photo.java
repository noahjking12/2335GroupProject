package algonquin.cst2335.a2335groupproject.nasa;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Jinwei Li 040818950
 * @version 1.0
 */
public class Photo implements Parcelable {
    public Bitmap getBitmap() {

        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private Bitmap bitmap;

    public Photo(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    // Other fields and methods...

    /**
     *
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(bitmap, flags);
        // Write other fields...
    }

    private Photo(Parcel in) {
        bitmap = in.readParcelable(getClass().getClassLoader());
        // Read other fields...
    }

    // Other Parcelable methods...
}
