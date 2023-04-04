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
import algonquin.cst2335.a2335groupproject.nasa.NasaActivity;
import algonquin.cst2335.a2335groupproject.nyt.ArticleSource;
import algonquin.cst2335.a2335groupproject.nyt.Articles;
import algonquin.cst2335.a2335groupproject.nyt.NYTDetailsFragment;
import algonquin.cst2335.a2335groupproject.ui.WeatherActivity;
/**
 * This class provides activities for the first page.
 * @author Jiale Zhang
 */
public class NewYorkTimes extends AppCompatActivity {

    ArrayList<Articles> articles;

    ArticlesDAO mDAO;
    private RecyclerView.Adapter myAdapter;
    private ArticleViewModel viewModel;

private ActivityNewYorkTimesBinding binding;

    /**
     *
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         *  connect the layout for the first page
         */
        binding = ActivityNewYorkTimesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        /**
         * start the action for toolbar
         */
        setSupportActionBar(binding.myToolbar);

/**
 * connect the Articledatabse
 */
        ArticleDatabase db = Room.databaseBuilder(getApplicationContext(), ArticleDatabase.class, "database-name").build();
        mDAO = db.cmDAO();
/**
 * get viewmodel and store data into Article arraylist
 */
        viewModel = new ViewModelProvider(this).get(ArticleViewModel.class);
        articles= viewModel.messages.getValue();

        if(articles == null){
            viewModel.messages.postValue(articles = new ArrayList<>());
        }

        /**
         * set up recycleView for topic history
         */
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<NewYorkTimes.MyRowHolder>() {
            /**
             *
             * @param parent   The ViewGroup into which the new View will be added after it is bound to
             *                 an adapter position.
             * @param viewType The view type of the new View.
             * @return
             */
            @NonNull
            @Override
            public NewYorkTimes.MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                NytStoredHistoryBinding roomBinding = NytStoredHistoryBinding.inflate(getLayoutInflater(),parent,false);
                View root=roomBinding.getRoot();
                return new NewYorkTimes.MyRowHolder(root);

            }

            /**
             *
             * @param holder   The ViewHolder which should be updated to represent the contents of the
             *                 item at the given position in the data set.
             * @param position The position of the item within the adapter's data set.
             */
            @Override //what are the textViews set to for row position
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                Articles message=articles.get(position);

                holder.messageText.setText(message.getMessage());

            }

            /**
             *
             * @return articles.size()
             */
            @Override
            public int getItemCount() {
                return articles.size();
            }

        });
        /**
         * store the searched history and activate the second activity
         */
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
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
                editor.putString("SearchedTopic", userInput);
                editor.apply();
                articles.add(new Articles(userInput,false));
                // Save the Forecast in the database
                Articles topic = new Articles(userInput,false);
                        Executor thread = Executors.newSingleThreadExecutor();
                        thread.execute(() -> {
                            long id = mDAO.insertMessage(topic);
                            topic.id = id; });
                myAdapter.notifyItemInserted(articles.size()-1);
                Intent secondPage = new Intent(NewYorkTimes.this, NewYorkTimes2.class);
                secondPage.putExtra("Topic",userInput);
                startActivity(secondPage);}});
    }

    /**
     *
     * @param menu menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;
    }

    /**
     *
     * @param item
     * @return true
     */
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

                Intent nasaPage = new Intent(NewYorkTimes.this, NasaActivity.class);
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

    /**
     * this class defines the data stored in each row
     */
    class MyRowHolder extends RecyclerView.ViewHolder{
        /**
         * declare texview
         */
        TextView messageText;

        /**
         *
         * @param itemView item view
         */
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
                /**
                 * this alert dialog asks the user if they want to delete the searched history
                 */
                AlertDialog.Builder builder = new AlertDialog.Builder(NewYorkTimes.this);
                builder.setMessage(confirm + messageText.getText())
                        .setTitle(question)
                        .setNegativeButton(cancel, (dialog, cl) -> {
                        })
                        .setPositiveButton(yes, (dialog, which) -> {
                            String userInput=binding.editText.getText().toString();
                            Articles topic = new Articles(userInput,false);
                            Executor thread = Executors.newSingleThreadExecutor();
                            thread.execute(() -> {
                                mDAO.deleteMessage(topic);
                                articles.remove(position);});
                                    myAdapter.notifyItemRemoved(position); //update the recycleview
                                    Snackbar.make(messageText, deleted+position+1,Snackbar.LENGTH_LONG)
                                            .setAction(undo, click ->{
                                                Executor thread2 = Executors.newSingleThreadExecutor();
                                                thread2.execute(() -> {
                                                    mDAO.insertMessage(clickMessage);
                                                    articles.add(position,clickMessage);});
                                                    runOnUiThread(()->{myAdapter.notifyItemInserted(position);});
                                                });})

                        .create()
                        .show();

            });



            messageText = itemView.findViewById(R.id.topics);


        }
    }}