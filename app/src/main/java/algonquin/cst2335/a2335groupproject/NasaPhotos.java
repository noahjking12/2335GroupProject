package algonquin.cst2335.a2335groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.a2335groupproject.data.SearchViewModel;
import algonquin.cst2335.a2335groupproject.databinding.ActivityNasaPhotos2Binding;
import algonquin.cst2335.a2335groupproject.databinding.ActivityNasaPhotosBinding;


public class NasaPhotos extends AppCompatActivity {

    ArrayList<Pictures> pictures;
    PicturesDAO mDAO;

    private RecyclerView.Adapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityNasaPhotosBinding binding = ActivityNasaPhotosBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        SearchViewModel searchModel = new ViewModelProvider(this).get(SearchViewModel.class);
        pictures= searchModel.messages.getValue();

binding.search.setOnClickListener(clk ->{
    if (binding.editText.getText() == null) {
        Context context = getApplicationContext();
        CharSequence text = "Please choose the date.";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();}

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();

        String editText=binding.storedHistory.getText().toString();

        editor.putString("Date",editText);

        editor.apply();

        String stored = prefs.getString("Date","");
        binding.storedHistory.setText(stored);


        Intent nasaPage = new Intent(NasaPhotos.this, NasaPhotos2.class);
        startActivity(nasaPage);


        myAdapter = new RecyclerView.Adapter<NasaPhotos.MyRowHolder>() {
            @NonNull
            @Override
            public NasaPhotos.MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                ActivityNasaPhotos2Binding  roomBinding = ActivityNasaPhotos2Binding.inflate(getLayoutInflater(),parent,false);
                View root=roomBinding.getRoot();
                return new NasaPhotos.MyRowHolder(root);

            }

            @Override //what are the textViews set to for row position
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                Pictures message=pictures.get(position);
                holder.messageText.setText(message.getDate());


            }

            @Override
            public int getItemCount() {
                return pictures.size();
            }

            @Override
            public int getItemViewType(int position) {
                Pictures obj = pictures.get(position);
                if(obj.isSearchButton){
                    return 1;
                }else
                    return 0;
            }
        };});
        ActivityNasaPhotos2Binding binding2 = ActivityNasaPhotos2Binding.inflate(getLayoutInflater());
        binding2.recycleView.setLayoutManager(new LinearLayoutManager(this));





        View parentLayout = findViewById(android.R.id.content);
        Snackbar.make(parentLayout, "This is main activity", Snackbar.LENGTH_LONG)
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                .show();

}


class MyRowHolder extends RecyclerView.ViewHolder{
    TextView messageText;


    public MyRowHolder(@NonNull View itemView) {
        super(itemView);


        itemView.setOnClickListener(clk->{
            int position = getAbsoluteAdapterPosition();
                    Pictures clickMessage = pictures.get(position);


                        AlertDialog.Builder builder = new AlertDialog.Builder(NasaPhotos.this);
                        builder.setMessage("Do you want to delete the message: " + messageText.getText())
                               .setTitle("Question")
                                .setPositiveButton("OK", (dialog, which) -> {

                            Executor thread = Executors.newSingleThreadExecutor();
                            thread.execute(() -> {
                            mDAO.deleteMessage(clickMessage);
                                pictures.remove(position);

                       runOnUiThread(()->{
                           myAdapter.notifyItemRemoved(position); //update the recycleview
                           Snackbar.make(messageText, "You deleted message #"+position,Snackbar.LENGTH_LONG)
                                   .setAction("Undo", click ->{
                                       Executor thread2 = Executors.newSingleThreadExecutor();
                                       thread2.execute(() -> {
                                                   mDAO.insertMessage(clickMessage);
                                           pictures.add(position,clickMessage);
                                               runOnUiThread(()->{myAdapter.notifyItemInserted(position);});
                                   });})
                                   .show();

                       });

                        });
                        })

                        .setNegativeButton("Cancel", (dialog, cl) -> {
                        })
                        .create().show();





        });
        messageText = itemView.findViewById(R.id.date);


    }
}}