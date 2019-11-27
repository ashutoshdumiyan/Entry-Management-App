package com.example.innovaccer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private List<HistoryItem> listItems;
    private Context context;

    public HistoryAdapter(List<HistoryItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);
        return new HistoryAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        final HistoryItem listItem = listItems.get(position);
        final int pos = position;
        holder.gname.setText(listItem.getGname());
        holder.gid.setText(listItem.getGid());
        holder.gphone.setText(listItem.getGphone());
        holder.itime.setText(listItem.getItime());
        holder.outtime.setText(listItem.getOuttime());
        holder.hname.setText(listItem.getHname());
        holder.hid.setText(listItem.getHid());
        holder.hphone.setText(listItem.getHphone());
        holder.hadd.setText(listItem.getHadd());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView gname, gid, gphone, itime, outtime, hname, hid, hphone, hadd;
        public Button checkout_button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gname = itemView.findViewById(R.id.gname);
            gid = itemView.findViewById(R.id.gid);
            gphone = itemView.findViewById(R.id.gphone);
            itime = itemView.findViewById(R.id.itime);
            outtime = itemView.findViewById(R.id.outtime);
            hname = itemView.findViewById(R.id.hname);
            hid = itemView.findViewById(R.id.hid);
            hphone = itemView.findViewById(R.id.hphone);
            hadd = itemView.findViewById(R.id.hadd);
        }
    }
}
