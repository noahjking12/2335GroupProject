package algonquin.cst2335.a2335groupproject;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/** myViewHolder is a custom ViewHolder class that extends RecyclerView.ViewHolder
 *  This class is to support the remove button and savedImageList ImageView for KittenHolder2 class
 * @author Yalin Su
 * @version 1.0
 */
public class myViewHolder extends RecyclerView.ViewHolder{
    /**
     * ImageView that displays the saved images
     */
    public ImageView savedImageList;
    /**
     * Button that allows user to remove the saved image from the list
     */
    public Button remove_btn;

    /**
     * constructor for myViewHolder
     * @param itemView the view that represents a single list item
     */
    public myViewHolder(@NonNull View itemView){
        super(itemView);
        savedImageList = itemView.findViewById(R.id.savedImageList);
        remove_btn = itemView.findViewById(R.id.remove_btn);

    }
}
