package algonquin.cst2335.a2335groupproject;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class myViewHolder extends RecyclerView.ViewHolder{
    public ImageView savedImageList;
    public Button remove_btn;

    public myViewHolder(@NonNull View itemView){
        super(itemView);
        savedImageList = itemView.findViewById(R.id.savedImageList);
        remove_btn = itemView.findViewById(R.id.remove_btn);

    }
}
