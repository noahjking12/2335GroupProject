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

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.a2335groupproject.data.ArticleDatabase;
import algonquin.cst2335.a2335groupproject.data.ArticleViewModel;
import algonquin.cst2335.a2335groupproject.databinding.ActivityNewYorkTimes2Binding;
import algonquin.cst2335.a2335groupproject.databinding.ActivityNewYorkTimesBinding;
import algonquin.cst2335.a2335groupproject.databinding.DetailsNytBinding;
import algonquin.cst2335.a2335groupproject.databinding.NytRecycleBinding;
import algonquin.cst2335.a2335groupproject.databinding.NytStoredHistoryBinding;
import algonquin.cst2335.a2335groupproject.nyt.ArticleSource;
import algonquin.cst2335.a2335groupproject.nyt.ArticleSourceDAO;
import algonquin.cst2335.a2335groupproject.nyt.ArticleSourceDatabase;
import algonquin.cst2335.a2335groupproject.nyt.Articles;
import algonquin.cst2335.a2335groupproject.nyt.NYTDetailsFragment;
import algonquin.cst2335.a2335groupproject.ui.WeatherActivity;
/**
 * This class provides activities for the second page.
 * @author Jiale Zhang
 */
public class NewYorkTimes2 extends AppCompatActivity {

    ArrayList<ArticleSource> articles;

    ArrayList<Articles> topic;
    RequestQueue queue = null;
    ArticleSourceDAO mDAO;

    ArticlesDAO aDAO;
    String url;
    private ArticleViewModel viewModel;


    private RecyclerView.Adapter myAdapter;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityNewYorkTimes2Binding binding = ActivityNewYorkTimes2Binding.inflate(getLayoutInflater());
        NytRecycleBinding binding1 = NytRecycleBinding.inflate(getLayoutInflater());
        /**
         * get a database ArticleSource
         */
        ArticleSourceDatabase db = Room.databaseBuilder(getApplicationContext(), ArticleSourceDatabase.class, "myMessageDatabase").build();
        mDAO = db.cmDAO();
/**
 * get layout to present lead paragraph
 */
        DetailsNytBinding detailBinding = DetailsNytBinding.inflate(getLayoutInflater());
        setSupportActionBar(binding.myToolbar);

/**
 * set up fragment in this page
 */
        viewModel = new ViewModelProvider(this).get(ArticleViewModel.class);
        viewModel.SelectedTopic.observe(this, ( newtopic) -> {
            FragmentManager fM = getSupportFragmentManager();
            FragmentTransaction transaction = fM.beginTransaction();
            NYTDetailsFragment articleFragment = new NYTDetailsFragment(newtopic);
            transaction.replace(R.id.fragmentLocation, articleFragment);
            transaction.addToBackStack("").commit();
        });

        if (articles == null) {
            viewModel.articles.postValue(articles = new ArrayList<>());
        }else{articles = viewModel.articles.getValue();}

        setContentView(binding.getRoot());
/**
 * get data from API by using Volley
 */
        queue = Volley.newRequestQueue(this);
    //    String topic = binding0.editText.getText().toString();
        String topic = getIntent().getStringExtra("Topic");
        String API_KEY = "CM3Ch8PhugEL5BmEa9S2mzMFsJcAIFXc";
        try {
            url = "https://api.nytimes.com/svc/search/v2/articlesearch.json?q=" + URLEncoder.encode(topic, "UTF-8") + "&api-key=" + API_KEY;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
/**
 * create JSON request
 */
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                (JSONObject response) -> {
                    try {
                        JSONObject mainObject = response.getJSONObject("response");
                        JSONArray docsArray = mainObject.getJSONArray("docs");
                        for (int i = 0; i < docsArray.length(); i++) {
                            JSONObject position = docsArray.getJSONObject(i);
                            String abstracts = position.getString("abstract");
                            String webUrl = position.getString("web_url");
                            String byline = position.getJSONObject("byline").getString("original");
                            String headline2 = position.getJSONObject("headline").getString("main");
                            String leadParagraph = position.getString("lead_paragraph");

                            runOnUiThread(new Runnable() {
                                /**
                                 * set view for the second page
                                 */
                                @Override
                                public void run() {
                                    binding1.webUrl.setText(webUrl);
                                    binding1.byline.setText(byline);
                                    binding1.abstracts.setText(abstracts);
                                    binding1.headline.setText(headline2);
                                    detailBinding.detailTopic.setText(leadParagraph);
                                }


                            });
                            ArticleSource newArticle = new ArticleSource(headline2, byline, abstracts, webUrl,leadParagraph);
                            articles.add(newArticle);
                        }

                        myAdapter.notifyItemInserted(articles.indexOf(articles));


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                (error) -> {
                    Toast.makeText(NewYorkTimes2.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                });
        queue.add(request);

        //  });
/**
 * create recycle view for the second page
 */
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<NewYorkTimes2.MyRowHolder2>() {
            /**
             *
             * @param parent   The ViewGroup into which the new View will be added after it is bound to
             *                 an adapter position.
             * @param viewType The view type of the new View.
             * @return NewYorkTimes2.MyRowHolder2(root)
             */
            @NonNull
            @Override
            public NewYorkTimes2.MyRowHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                NytRecycleBinding roomBinding = NytRecycleBinding.inflate(getLayoutInflater(), parent, false);
                View root = roomBinding.getRoot();
                return new NewYorkTimes2.MyRowHolder2(root);

            }

            /**
             *
             * @param holder   The ViewHolder which should be updated to represent the contents of the
             *                 item at the given position in the data set.
             * @param position The position of the item within the adapter's data set.
             */
            @Override
            public void onBindViewHolder(@NonNull MyRowHolder2 holder, int position) {
                ArticleSource message = articles.get(position);
                holder.abstractText.setText(message.getAbstracts());
                holder.bylineText.setText(message.getByline());
                holder.urlText.setText(message.getWebLink());
                holder.headlineText.setText(message.getHeadline());
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


    }

    /**
     *
     * @param menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;
    }

    /**
     * set up toolbar function
     * @param item
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String instruction = getResources().getString(R.string.instruction);
        String details = getResources().getString(R.string.details);
        switch (item.getItemId()) {
            case R.id.helpMenu:
                // show the instruction
                AlertDialog.Builder builder = new AlertDialog.Builder(NewYorkTimes2.this);
                builder.setMessage(details)
                        .setTitle(instruction)
                        .create().show();
                break;
            case R.id.NasaItem:
                // Go to Nasa activity

                Intent nasaPage = new Intent(NewYorkTimes2.this, NasaPhotos.class);
                startActivity(nasaPage);

                break;

            case R.id.KittenItem:


                Intent kittenPage = new Intent(NewYorkTimes2.this, SecondActivity.class);

                startActivity( kittenPage );

                break;
            case R.id.NYTItem:
                // Go to NewYorkTimes activity

                Intent nytPage = new Intent(NewYorkTimes2.this, NewYorkTimes.class);
                startActivity(nytPage);


                break;

            case R.id.WeatherItem:

                Intent weatherPage = new Intent(NewYorkTimes2.this, WeatherActivity.class);

                startActivity(weatherPage);

                break;


        }

        return true;
    }

    /**
     * this class defines the data stored in each row
     */
    class MyRowHolder2 extends RecyclerView.ViewHolder {
        TextView headlineText, urlText, bylineText, abstractText;

        /**
         *
         * @param itemView
         */
        public MyRowHolder2(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(clk ->{
                int position = getAbsoluteAdapterPosition();
                ArticleSource selected = articles.get(position);
                viewModel.SelectedTopic.postValue(selected);
            });
            headlineText = itemView.findViewById(R.id.headline);
            urlText = itemView.findViewById(R.id.web_url);
            bylineText = itemView.findViewById(R.id.byline);
            abstractText = itemView.findViewById(R.id.abstracts);

        }
    }
}