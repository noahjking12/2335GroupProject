package algonquin.cst2335.a2335groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.a2335groupproject.databinding.ActivityNewYorkTimes2Binding;
import algonquin.cst2335.a2335groupproject.databinding.ActivityNewYorkTimesBinding;

public class NewYorkTimes2 extends AppCompatActivity {

    ArrayList<Articles> messages;

    ArticlesDAO mDAO;
    private ActivityNewYorkTimes2Binding binding;

    private RecyclerView.Adapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_york_times2);

        myAdapter = new RecyclerView.Adapter<Articles.MyRowHolder>() {



            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    binding = ActivityNewYorkTimes2Binding.inflate(getLayoutInflater(),parent,false);
                    View root=binding.getRoot();
                    return new MyRowHolder(root);}



            @Override //what are the textViews set to for row position
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                Articles message=messages.get(position);
                holder.messageText.setText(message.getMessage());


            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position) {
                Articles obj = messages.get(position);
                if(obj.isSentButton){
                    return 1;
                }else
                    return 0;
            }
        };
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    class MyRowHolder extends RecyclerView.ViewHolder{
        TextView messageText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk->{
                int position = getAbsoluteAdapterPosition();
                Articles clickMessage = messages.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(NewYorkTimes2.this);
                builder.setMessage("Do you want to delete the message: " + messageText.getText())
                        .setTitle("Question")
                        .setPositiveButton("OK", (dialog, which) -> {

                            Executor thread = Executors.newSingleThreadExecutor();
                            thread.execute(() -> {
                                mDAO.deleteMessage(clickMessage);
                                messages.remove(position);

                                runOnUiThread(()->{
                                    myAdapter.notifyItemRemoved(position); //update the recycleview
                                    Snackbar.make(messageText, "You deleted message #"+position,Snackbar.LENGTH_LONG)
                                            .setAction("Undo", click ->{
                                                Executor thread2 = Executors.newSingleThreadExecutor();
                                                thread2.execute(() -> {
                                                    mDAO.insertMessage(clickMessage);
                                                    messages.add(position,clickMessage);
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
            messageText = itemView.findViewById(R.id.editText);

        }
    }

}