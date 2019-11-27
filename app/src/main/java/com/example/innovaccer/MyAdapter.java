package com.example.innovaccer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private List<Item> listItems;
    private Context context;

    public MyAdapter(List<Item> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Item listItem = listItems.get(position);
        final int pos = position;
        holder.gname.setText(listItem.getGname());
        holder.gid.setText(listItem.getGid());
        holder.gphone.setText(listItem.getGphone());
        holder.itime.setText(listItem.getItime());
        holder.hname.setText(listItem.getHname());
        holder.hid.setText(listItem.getHid());
        holder.hphone.setText(listItem.getHphone());
        holder.hadd.setText(listItem.getHadd());
        holder.id.setText(listItem.getId());

        holder.checkout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String out_time = Calendar.getInstance().getTime().toString();
                final String msg =  listItem.getGname() + "\n" +  listItem.getGphone() + "\n" + listItem.getItime() + "\n" + out_time + "\n" + "Host Name: " + listItem.getHname() + "\n" +  listItem.getHadd();
//                Log.i("GuestId",listItem.getGid());
                db.collection("Guests").document(listItem.getId())
                        .update("checkout", out_time)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //next three lines are to remove the item from recycler view
                                    listItems.remove(pos);
                                    notifyItemRemoved(pos);
                                    notifyItemRangeChanged(pos, listItems.size());
                                    sendMail(listItem.getGid().substring(19), msg);
                                }
                                else {
                                    Toast.makeText(context, "Error! Please try again", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }

    private void sendMail(String ml, String ms) {
        String mail = ml;
        String message = ms;
        String subject = "Your last visit is over";

        JavaMailAPI javaMailAPI = new JavaMailAPI(context,mail,subject,message);
        javaMailAPI.execute();
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView gname, gid, gphone, itime, hname, hid, hphone, hadd, id;
        public Button checkout_button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gname = itemView.findViewById(R.id.gname);
            gid = itemView.findViewById(R.id.gid);
            gphone = itemView.findViewById(R.id.gphone);
            itime = itemView.findViewById(R.id.itime);
            hname = itemView.findViewById(R.id.hname);
            hid = itemView.findViewById(R.id.hid);
            hphone = itemView.findViewById(R.id.hphone);
            hadd = itemView.findViewById(R.id.hadd);
            id = itemView.findViewById(R.id.id);
            checkout_button = itemView.findViewById(R.id.checkout_button);
        }
    }
}
