package com.example.innovaccer.ui.current;

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

import com.example.innovaccer.Item;
import com.example.innovaccer.MyAdapter;
import com.example.innovaccer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CurrentFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Item> listItems = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressBar pb;
    private LinearLayout linearLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_current_visitors, container, false);
        linearLayout = root.findViewById(R.id.layout1);
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
                                if (documentSnapshot.getString("checkout") != null) {
                                    continue;
                                }
                                Item it = new Item(
                                        "Visitor Name: "+documentSnapshot.getString("guestname"),
                                        "Visitor E-mail Id: "+documentSnapshot.getString("guestid"),
                                        "Visitor phone: "+documentSnapshot.getString("guestphone"),
                                        "Visitor check-in time: "+documentSnapshot.getString("intime"),
                                        "Host Name: "+documentSnapshot.getString("hostname"),
                                        "Host E-mail Id: "+documentSnapshot.getString("hostemail"),
                                        "Host phone: "+documentSnapshot.getString("hostphone"),
                                        "Address Visited: "+documentSnapshot.getString("address"),
                                        ""+documentSnapshot.getString("id")
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
                            textView.setText("No visitors are currently checked in.");
                            textView.setTextSize((float) 20);
                            linearLayout.addView(textView);
                        }
                        MyAdapter adapter = new MyAdapter(listItems,getActivity());
                        recyclerView.setAdapter(adapter);
                    }
                });
        return root;
    }
}