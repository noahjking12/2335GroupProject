package algonquin.cst2335.a2335groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.a2335groupproject.data.ArticleDatabase;
import algonquin.cst2335.a2335groupproject.data.ArticleViewModel;
import algonquin.cst2335.a2335groupproject.databinding.ActivityNewYorkTimes2Binding;
import algonquin.cst2335.a2335groupproject.databinding.ActivityNewYorkTimesBinding;
import algonquin.cst2335.a2335groupproject.databinding.StoredHistoryBinding;

public class NewYorkTimes extends AppCompatActivity {

    ArrayList<Articles> articles;
    ArticlesDAO mDAO;
    private RecyclerView.Adapter myAdapter;
    private ArticleViewModel viewModel;

private ActivityNewYorkTimesBinding binding;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = ActivityNewYorkTimesBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        ArticleDatabase db = Room.databaseBuilder(getApplicationContext(), ArticleDatabase.class, "database-name").build();
        mDAO = db.cmDAO();

        viewModel = new ViewModelProvider(this).get(ArticleViewModel.class);
        articles= viewModel.messages.getValue();

        if(articles == null){
            viewModel.messages.postValue(articles = new ArrayList<>());
        }
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<NewYorkTimes.MyRowHolder>() {
            @NonNull
            @Override
            public NewYorkTimes.MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                StoredHistoryBinding roomBinding = StoredHistoryBinding.inflate(getLayoutInflater(),parent,false);
                View root=roomBinding.getRoot();
                return new NewYorkTimes.MyRowHolder(root);

            }

            @Override //what are the textViews set to for row position
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                Articles message=articles.get(position);
                holder.messageText.setText(message.getMessage());

            }

            @Override
            public int getItemCount() {
                return articles.size();
            }

        });


        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String date = prefs.getString("Date", "");


        binding.Searchbutton.setOnClickListener(clk ->{
            String userInput=binding.editText.getText().toString();
            if (userInput.equals("") ) {
                Context context = getApplicationContext();
                CharSequence text = "Please choose the topic.";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }else {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("Date", userInput);
                editor.apply();
                articles.add(new Articles(userInput,false));
                myAdapter.notifyItemInserted(articles.size()-1);
                //  binding.editText.setText("");
                Intent nasaPage = new Intent(NewYorkTimes.this, NewYorkTimes2.class);
                startActivity(nasaPage);}});
    }


    class MyRowHolder extends RecyclerView.ViewHolder{
        TextView messageText;


        public MyRowHolder(@NonNull View itemView) {
            super(itemView);


            itemView.setOnClickListener(clk->{
                int position = getAbsoluteAdapterPosition();
                Articles clickMessage = articles.get(position);


                AlertDialog.Builder builder = new AlertDialog.Builder(NewYorkTimes.this);
                builder.setMessage("Do you want to delete the message: " + messageText.getText())
                        .setTitle("Question")
                        .setPositiveButton("OK", (dialog, which) -> {

                            Executor thread = Executors.newSingleThreadExecutor();
                            thread.execute(() -> {
                                mDAO.deleteMessage(clickMessage);
                                articles.remove(position);

                                runOnUiThread(()->{
                                    myAdapter.notifyItemRemoved(position); //update the recycleview
                                    Snackbar.make(messageText, "You deleted message #"+position,Snackbar.LENGTH_LONG)
                                            .setAction("Undo", click ->{
                                                Executor thread2 = Executors.newSingleThreadExecutor();
                                                thread2.execute(() -> {
                                                    mDAO.insertMessage(clickMessage);
                                                    articles.add(position,clickMessage);
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
            messageText = itemView.findViewById(R.id.topics);


        }
    }}