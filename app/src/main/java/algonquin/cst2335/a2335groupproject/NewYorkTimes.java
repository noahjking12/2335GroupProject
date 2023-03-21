package algonquin.cst2335.a2335groupproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.a2335groupproject.data.ArticleViewModel;
import algonquin.cst2335.a2335groupproject.databinding.ActivityNewYorkTimes2Binding;
import algonquin.cst2335.a2335groupproject.databinding.ActivityNewYorkTimesBinding;

public class NewYorkTimes extends AppCompatActivity {

    ArrayList<Articles> messages;
    ArticlesDAO mDAO;
    private RecyclerView.Adapter myAdapter;

private ActivityNewYorkTimesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNewYorkTimesBinding.inflate(getLayoutInflater());

        ArticleViewModel chatModel = new ViewModelProvider(this).get(ArticleViewModel.class);
        messages= chatModel.messages.getValue();

        setContentView(binding.getRoot());

        if(messages == null)
        {
            chatModel.messages.setValue( messages = new ArrayList<>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                //first load old message
                List<Articles> previousMessages = mDAO.getAllMessages();
                messages.addAll(previousMessages);
                runOnUiThread(()->{
                    binding.recycleView.setAdapter(myAdapter);
                });

            });}

        binding.Searchbutton.setOnClickListener(clk->{

            if (binding.editText.getText() == null) {
                Context context = getApplicationContext();
                CharSequence text = "Please enter the topic.";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();}

            SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = prefs.edit();

            String editText=binding.editText.getText().toString();

            editor.putString("Date",editText);

            editor.apply();

            String stored = prefs.getString("Date","");
            binding.articleSearched.setText(stored);


            Intent secondPage = new Intent(NewYorkTimes.this, NewYorkTimes2.class);
            startActivity(secondPage);


        });



    }
}