package algonquin.cst2335.a2335groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.a2335groupproject.data.ArticleDatabase;
import algonquin.cst2335.a2335groupproject.data.ArticleViewModel;
import algonquin.cst2335.a2335groupproject.databinding.ActivityNewYorkTimesBinding;
import algonquin.cst2335.a2335groupproject.databinding.NytStoredHistoryBinding;
import algonquin.cst2335.a2335groupproject.nyt.ArticleSource;
import algonquin.cst2335.a2335groupproject.nyt.Articles;
import algonquin.cst2335.a2335groupproject.nyt.NYTDetailsFragment;
import algonquin.cst2335.a2335groupproject.ui.WeatherActivity;

public class NewYorkTimes extends AppCompatActivity {

    ArrayList<Articles> articles;
    ArticlesDAO mDAO;
    private RecyclerView.Adapter myAdapter;
    private ArticleViewModel viewModel;

private ActivityNewYorkTimesBinding binding;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = ActivityNewYorkTimesBinding.inflate(getLayoutInflater());
        setSupportActionBar(binding.myToolbar);
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

                NytStoredHistoryBinding roomBinding = NytStoredHistoryBinding.inflate(getLayoutInflater(),parent,false);
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
            String choose = getResources().getString(R.string.choose_topic);
            String userInput=binding.editText.getText().toString();
            if (userInput.equals("") ) {
                Context context = getApplicationContext();
                CharSequence text = choose;
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }else {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("Date", userInput);
                editor.apply();
                articles.add(new Articles(userInput,false));
                myAdapter.notifyItemInserted(articles.size()-1);
                Intent secondPage = new Intent(NewYorkTimes.this, NewYorkTimes2.class);
                startActivity(secondPage);}});
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String instruction = getResources().getString(R.string.instruction);
        String details =getResources().getString(R.string.details);
        switch( item.getItemId() )
        {
            case R.id.helpMenu:
                // show the instruction
                AlertDialog.Builder builder = new AlertDialog.Builder(NewYorkTimes.this);
                builder.setMessage(details)
                        .setTitle(instruction)
                        .create().show();
                break;

            case R.id.NasaItem:
                // Go to Nasa activity

                Intent nasaPage = new Intent(NewYorkTimes.this, NasaPhotos.class);
                startActivity(nasaPage);

                break;

            case R.id.KittenItem:


                Intent kittenPage = new Intent(NewYorkTimes.this, SecondActivity.class);

                startActivity( kittenPage );

                break;
            case R.id.NYTItem:
                // Go to NewYorkTimes activity

                Intent nytPage = new Intent(NewYorkTimes.this, NewYorkTimes.class);
                startActivity(nytPage);


                break;

            case R.id.WeatherItem:

                Intent weatherPage = new Intent(NewYorkTimes.this, WeatherActivity.class);

                startActivity(weatherPage);

                break;

        }

        return true;
    }


    class MyRowHolder extends RecyclerView.ViewHolder{
        TextView messageText;


        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk->{
                int position = getAbsoluteAdapterPosition();
                Articles clickMessage = articles.get(position);
                String question =getResources().getString(R.string.question);
                String confirm =getResources().getString(R.string.confirm);
                String deleted =getResources().getString(R.string.deleted);
                String undo =getResources().getString(R.string.undo);
                String cancel =getResources().getString(R.string.cancel);
                String yes =getResources().getString(R.string.yes);
                AlertDialog.Builder builder = new AlertDialog.Builder(NewYorkTimes.this);
                builder.setMessage(confirm + messageText.getText())
                        .setTitle(question)
                        .setPositiveButton(yes, (dialog, which) -> {

                            Executor thread = Executors.newSingleThreadExecutor();
                            thread.execute(() -> {
                                mDAO.deleteMessage(clickMessage);
                                articles.remove(position);

                                runOnUiThread(()->{
                                    myAdapter.notifyItemRemoved(position); //update the recycleview
                                    Snackbar.make(messageText, deleted+position+1,Snackbar.LENGTH_LONG)
                                            .setAction(undo, click ->{
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

                        .setNegativeButton(cancel, (dialog, cl) -> {
                        })
                        .create().show();

            });



            messageText = itemView.findViewById(R.id.topics);


        }
    }}