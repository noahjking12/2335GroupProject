package algonquin.cst2335.a2335groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

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

import algonquin.cst2335.a2335groupproject.data.PicturesDatabase;
import algonquin.cst2335.a2335groupproject.data.SearchViewModel;
import algonquin.cst2335.a2335groupproject.databinding.ActivityNasaPhotos2Binding;
import algonquin.cst2335.a2335groupproject.databinding.ActivityNasaPhotosBinding;
import algonquin.cst2335.a2335groupproject.databinding.SavedHistoryBinding;


public class NasaPhotos extends AppCompatActivity {

    ArrayList<Pictures> pictures;
    PicturesDAO mDAO;
    private SearchViewModel viewModel;
    private RecyclerView.Adapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityNasaPhotosBinding binding = ActivityNasaPhotosBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        PicturesDatabase db = Room.databaseBuilder(getApplicationContext(), PicturesDatabase.class, "database-name").build();
        mDAO = db.cmDAO();

        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        pictures= viewModel.messages.getValue();

      if(pictures == null){
        viewModel.messages.postValue(pictures = new ArrayList<>());
}
        binding.recycleViewFirstPage.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleViewFirstPage.setAdapter(myAdapter = new RecyclerView.Adapter<NasaPhotos.MyRowHolder>() {
            @NonNull
            @Override
            public NasaPhotos.MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                SavedHistoryBinding roomBinding = SavedHistoryBinding.inflate(getLayoutInflater(),parent,false);
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

        });


        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String date = prefs.getString("Date", "");


        binding.search.setOnClickListener(clk ->{
            String userInput=binding.editText.getText().toString();
            if (userInput.equals("") ) {
                Context context = getApplicationContext();
                CharSequence text = "Please choose the date.";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }else {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("Date", userInput);
                editor.apply();
                pictures.add(new Pictures(userInput));
                myAdapter.notifyItemInserted(pictures.size()-1);
                //  binding.editText.setText("");
                Intent nasaPage = new Intent(NasaPhotos.this, NasaPhotos2.class);
                startActivity(nasaPage);}});
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
        messageText = itemView.findViewById(R.id.messageText);


    }
}}