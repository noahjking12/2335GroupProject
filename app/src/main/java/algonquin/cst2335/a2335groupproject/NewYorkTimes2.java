package algonquin.cst2335.a2335groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import algonquin.cst2335.a2335groupproject.data.ArticleViewModel;
import algonquin.cst2335.a2335groupproject.databinding.ActivityNewYorkTimes2Binding;
import algonquin.cst2335.a2335groupproject.databinding.ActivityNewYorkTimesBinding;
import algonquin.cst2335.a2335groupproject.databinding.NytRecycleBinding;
import algonquin.cst2335.a2335groupproject.nyt.ArticleSource;

public class NewYorkTimes2 extends AppCompatActivity {

    ArrayList<ArticleSource> articles;
    RequestQueue queue = null;
    ArticlesDAO mDAO;
    String url;
    private ArticleViewModel viewModel;


    private RecyclerView.Adapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       ActivityNewYorkTimes2Binding binding = ActivityNewYorkTimes2Binding.inflate(getLayoutInflater());
       ActivityNewYorkTimesBinding binding0=ActivityNewYorkTimesBinding.inflate(getLayoutInflater());
        NytRecycleBinding binding1=NytRecycleBinding.inflate(getLayoutInflater());
        setSupportActionBar(binding.myToolbar);
        setContentView(binding1.getRoot());

        viewModel = new ViewModelProvider(this).get(ArticleViewModel.class);
        articles= viewModel.articles.getValue();

        if(articles == null){
            viewModel.articles.postValue(articles=new ArrayList<>());
        }
        queue = Volley.newRequestQueue(this);
       // binding0.Searchbutton.setOnClickListener(clk ->{
        //    Intent fromPrevious = getIntent();
            String topic = binding0.editText.getText().toString();
                 //   fromPrevious.getStringExtra("Topic");
            String API_KEY ="CM3Ch8PhugEL5BmEa9S2mzMFsJcAIFXc";
            try {
                url = "https://api.nytimes.com/svc/search/v2/articlesearch.json?q="+ URLEncoder.encode(topic,"UTF-8")+"&api-key="+API_KEY;
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            JsonObjectRequest  request = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    (JSONObject response) -> {
                        try {

                            JSONObject mainObject = response.getJSONObject("response");
                            JSONArray docsArray = mainObject.getJSONArray("docs");
                            for(int i = 0 ; i < docsArray.length() ; i++){
                                JSONObject p = docsArray.getJSONObject(i);
                               // JSONObject headLine1 = mainObject.getJSONObject("docs");

                            //    String headline2 = p.getString("headline").;

                            JSONObject position0 = docsArray.getJSONObject(0);
                            String abstracts = position0.getString("abstract");
                            String webUrl = position0.getString("web_url");
                          String byline = p.getJSONObject("byline").getString("original");
                          String headline2 = p.getJSONObject("headline").getString("main");
                        runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding1.webUrl.setText( webUrl);
                                        binding1.byline.setText( byline);
                                        binding1.abstracts.setText( abstracts);
                                        binding1.headline.setText( headline2);

                                    }


                                });
                                ArticleSource newArticle= new ArticleSource(headline2,byline,abstracts,webUrl);
                                articles.add(newArticle);}

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

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<NewYorkTimes2.MyRowHolder2>() {
            @NonNull
            @Override
            public NewYorkTimes2.MyRowHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                NytRecycleBinding roomBinding=NytRecycleBinding.inflate(getLayoutInflater(),parent,false);
                View root=roomBinding.getRoot();
                return new NewYorkTimes2.MyRowHolder2(root);

            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder2 holder, int position) {
                ArticleSource message=articles.get(position);
                holder.abstractText.setText(message.getAbstracts());
                holder.bylineText.setText(message.getByline());
                holder.urlText.setText(message.getWebLink());
                holder.headlineText.setText(message.getHeadline());
            }


            @Override
            public int getItemCount() {
                return  articles.size();
            }

        });




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
                AlertDialog.Builder builder = new AlertDialog.Builder(NewYorkTimes2.this);
                builder.setMessage(details)
                        .setTitle(instruction)
                        .create().show();
                break;

        }

        return true;
    }
    class MyRowHolder2 extends RecyclerView.ViewHolder{
        TextView headlineText,urlText,bylineText,abstractText;


        public MyRowHolder2(@NonNull View itemView) {
            super(itemView);
            headlineText=itemView.findViewById(R.id.headline);
            urlText=itemView.findViewById(R.id.web_url);
            bylineText=itemView.findViewById(R.id.byline);
            abstractText=itemView.findViewById(R.id.abstracts);
       }
    }}