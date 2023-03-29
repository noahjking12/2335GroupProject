package algonquin.cst2335.a2335groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import algonquin.cst2335.a2335groupproject.databinding.ActivityNewYorkTimes2Binding;
import algonquin.cst2335.a2335groupproject.databinding.ActivityNewYorkTimesBinding;
import algonquin.cst2335.a2335groupproject.databinding.NytRecycleBinding;
import algonquin.cst2335.a2335groupproject.nyt.ArticleSource;

public class NewYorkTimes2 extends AppCompatActivity {

    ArrayList<ArticleSource> articles;
    RequestQueue queue = null;
    ArticlesDAO mDAO;
    String url;

    protected  String topic;


    private RecyclerView.Adapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       ActivityNewYorkTimes2Binding binding = ActivityNewYorkTimes2Binding.inflate(getLayoutInflater());
       ActivityNewYorkTimesBinding binding0=ActivityNewYorkTimesBinding.inflate(getLayoutInflater());
        NytRecycleBinding binding1=NytRecycleBinding.inflate(getLayoutInflater());

        setSupportActionBar(binding.myToolbar);
        setContentView(binding.getRoot());

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
                return articles.size();
            }

        });


        queue = Volley.newRequestQueue(this);
        binding0.Searchbutton.setOnClickListener(clk ->{
            Intent fromPrevious = getIntent();
            String topic = fromPrevious.getStringExtra("Topic");
            String API_KEY ="ca4ced9661a84b5280c9a0967210b3f6";
            try {
                url = "https://api.openweathermap.org/data/2.5/weather?q="+ URLEncoder.encode(topic,"UTF-8")+"&appid="+API_KEY;
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            JsonObjectRequest  request = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    (JSONObject response) -> {
                        try {
                          //  JSONObject mainObject = response.getJSONObject("docs");
                            JSONArray docsArray = response.getJSONArray("docs");
                            JSONArray headlineArray = response.getJSONArray("headline");
                            JSONObject position0 = docsArray.getJSONObject(0);
                            JSONObject position1 = headlineArray.getJSONObject(0);

//                            String iconurl ="https://openweathermap.org/img/w/" + position0.getString("icon")+ ".png" ;
//                            String icon1= position0.getString("icon")+".png";
                            //        String icon = parseIconName(icon1);

                            String abstracts = position0.getString("abstract");
                            String webUrl = position0.getString("web_url");
                            String headline = position1.getString("headline");
                            String byline = position0.getString("byline");



//                            try {
//                                // Check if this icon has already been downloaded, if so just load it
//                                String pathname = getFilesDir() + "/" + icon1;
//                                File file = new File( getFilesDir(), pathname);
////                                    if (file.exists()) {
////                                        bitmap = BitmapFactory.decodeFile(pathname);
////                                    } else {
//
//                                ImageRequest imgReq = new ImageRequest(iconurl, new Response.Listener<Bitmap>() {
//
//                                    @Override
//                                    public void onResponse(Bitmap image) {
//                                        // Do something with loaded bitmap...
//                                        bitmap=image;
//
//                                        FileOutputStream fOut = null;
//                                        try {
//                                            fOut = openFileOutput(icon1, Context.MODE_PRIVATE);
//                                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
//                                            binding.icon.setImageBitmap(bitmap);
//                                            fOut.flush();
//                                            fOut.close();
//
//
//                                        } catch (FileNotFoundException e) {
//                                            throw new RuntimeException(e);
//                                        } catch (IOException e) {
//                                            throw new RuntimeException(e);
//                                        }
//                                    }
//                                }, 1024, 1024, ImageView.ScaleType.CENTER, null,
//                                        (error) -> {
//                                            Toast.makeText(MainActivity.this, "No icon", Toast.LENGTH_SHORT).show();
//
//                                        });
//                                queue.add(imgReq);
////                                    }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    binding1.webUrl.setText("The current temperature is " + webUrl);
                                    binding1.byline.setText("The min temperature is " + byline);
                                    binding1.abstracts.setText("The max temperature is " + abstracts);
                                    binding1.headline.setText("The description is " + headline);

                                }


                            });
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    (error) -> {
                        Toast.makeText(NewYorkTimes2.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                    });
            queue.add(request);

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