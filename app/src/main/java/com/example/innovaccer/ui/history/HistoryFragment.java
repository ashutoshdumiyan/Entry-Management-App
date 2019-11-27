package com.example.innovaccer.ui.history;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.innovaccer.HistoryAdapter;
import com.example.innovaccer.HistoryItem;
import com.example.innovaccer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private LinearLayout linearLayout;
    private ProgressBar pb;
    private List<HistoryItem> listItems = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_history, container, false);
        linearLayout = root.findViewById(R.id.layout2);
        linearLayout.setGravity(Gravity.CENTER);
        pb = root.findViewById(R.id.progressBar1);
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        if(listItems.size()>0)
            listItems.clear();
        db.collection("Guests").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                if (documentSnapshot.getString("checkout") == null) {
                                    continue;
                                }
                                HistoryItem it = new HistoryItem(
                                        "Visitor Name: "+documentSnapshot.getString("guestname"),
                                        "Visitor E-mail Id: "+documentSnapshot.getString("guestid"),
                                        "Visitor phone: "+documentSnapshot.getString("guestphone"),
                                        "Visitor check-in time: "+documentSnapshot.getString("intime"),
                                        "Visitor check-out time: "+documentSnapshot.getString("checkout"),
                                        "Host Name: "+documentSnapshot.getString("hostname"),
                                        "Host E-mail Id: "+documentSnapshot.getString("hostemail"),
                                        "Host phone: "+documentSnapshot.getString("hostphone"),
                                        "Address Visited: "+documentSnapshot.getString("address")
                                );
                                listItems.add(it);
                            }
                        }
                        else {
                            Toast.makeText(root.getContext(), "Error! Please try again.", Toast.LENGTH_SHORT).show();
                        }
                        linearLayout.setGravity(Gravity.NO_GRAVITY);
                        pb.setVisibility(View.GONE);
                        if (listItems.size() == 0) {
                            TextView textView = new TextView(root.getContext());
                            textView.setText("Nothing to show here.");
                            textView.setTextSize((float) 20);
                            linearLayout.addView(textView);
                        }
                        HistoryAdapter historyAdapter = new HistoryAdapter(listItems,getActivity());
                        recyclerView.setAdapter(historyAdapter);
                    }
                });
        return root;
    }
}