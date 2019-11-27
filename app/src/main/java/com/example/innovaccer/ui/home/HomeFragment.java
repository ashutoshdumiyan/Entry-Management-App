package com.example.innovaccer.ui.home;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.innovaccer.JavaMailAPI;
import com.example.innovaccer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Button cin, cout;
    private TextInputLayout hostname_layout, guestname_layout, hostid_layout, guestid_layout, hostphone_layout, guestphone_layout, hostadd_layout;
    private EditText hostname, guestname, hostid, guestid, hostphone, guestphone, hostadd;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog pd;
    private View root;
    private final static int SEND_SMS_PERMISSION_REQ=1;

    private boolean isValidAddress(String add) {
        if (add.isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private Boolean isValidName(String name) {
        String name_Pattern = "^[\\p{L} .'-]+$";
        Pattern pattern = Pattern.compile(name_Pattern);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    private Boolean isValidMobile(String number) {
        String num_Pattern = "^[1-9]{1}[0-9]{9}$";
        Pattern pattern = Pattern.compile(num_Pattern);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    private boolean checkPermission(String sendSms) {

        int checkpermission = ContextCompat.checkSelfPermission(root.getContext(), sendSms);
        return checkpermission == PackageManager.PERMISSION_GRANTED;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        pd = new ProgressDialog(root.getContext());
        cin = root.findViewById(R.id.checkin);
        hostname = root.findViewById(R.id.hostname);
        hostname_layout = root.findViewById(R.id.hostname_layout);
        hostid = root.findViewById(R.id.hostid);
        hostid_layout = root.findViewById(R.id.hostid_layout);
        hostphone = root.findViewById(R.id.hostphone);
        hostphone_layout = root.findViewById(R.id.hostphone_layout);
        hostadd_layout = root.findViewById(R.id.hostadd_layout);
        hostadd = root.findViewById(R.id.hostadd);
        guestname = root.findViewById(R.id.guestname);
        guestname_layout = root.findViewById(R.id.guestname_layout);
        guestid = root.findViewById(R.id.guestid);
        guestid_layout = root.findViewById(R.id.guestid_layout);
        guestphone = root.findViewById(R.id.guestphone);
        guestphone_layout = root.findViewById(R.id.guestphone_layout);
        if(!checkPermission(Manifest.permission.SEND_SMS)){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.SEND_SMS},SEND_SMS_PERMISSION_REQ);
        }
        cin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hostname_layout.setError(null);
                hostid_layout.setError(null);
                hostphone_layout.setError(null);
                hostadd_layout.setError(null);
                guestname_layout.setError(null);
                guestid_layout.setError(null);
                guestphone_layout.setError(null);
                if (!isValidName(hostname.getText().toString().trim())) {
                    hostname_layout.setError("Invalid Name");
                    return;
                }
                if (!isValidEmail(hostid.getText().toString().trim())) {
                    hostid_layout.setError("Invalid Email-Id");
                    return;
                }
                if (!isValidMobile(hostphone.getText().toString().trim())) {
                    hostphone_layout.setError("Invalid Phone No.");
                    return;
                }
                if (!isValidAddress(hostadd.getText().toString().trim())) {
                    hostadd_layout.setError("Invalid address");
                    return;
                }
                if (!isValidName(guestname.getText().toString().trim())) {
                    guestname_layout.setError("Invalid Name");
                    return;
                }
                if (!isValidEmail(guestid.getText().toString().trim())) {
                    guestid_layout.setError("Invalid Email-Id");
                    return;
                }
                if (!isValidMobile(guestphone.getText().toString().trim())) {
                    guestphone_layout.setError("Invalid Phone No.");
                    return;
                }
                pd.setTitle("Checking In");
                pd.setMessage("Please wait...");
                pd.show();
                final String hostPhone = hostphone.getText().toString().trim();
                final String msg = "A visitor just checked in:-\n" + "Name: " + guestname.getText().toString().trim() + "\n" + "E-Mail: " + guestid.getText().toString().trim() + "\n" + "Phone: " + guestphone.getText().toString().trim();
                // Add check in functionality here
                Map<String, Object> map = new HashMap<>();
                map.put("hostname", hostname.getText().toString().trim());
                map.put("hostemail", hostid.getText().toString().trim());
                map.put("hostphone", hostphone.getText().toString().trim());
                map.put("address", hostadd.getText().toString().trim());
                map.put("intime", Calendar.getInstance().getTime().toString());
                map.put("guestname", guestname.getText().toString().trim());
                map.put("guestid", guestid.getText().toString().trim());
                map.put("guestphone", guestphone.getText().toString().trim());
                map.put("checkout",null);
                String id = db.collection("Guests").document().getId();
                map.put("id", id);
                db.collection("Guests").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (checkPermission(Manifest.permission.SEND_SMS)) {
                                SmsManager.getDefault().sendTextMessage(hostPhone, null, msg, null, null);
                            }
                            else{
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQ);
                                if(checkPermission(Manifest.permission.SEND_SMS)) {
                                    SmsManager.getDefault().sendTextMessage(hostphone.getText().toString().trim(), null, msg, null,null);
                                }
                            }
                            sendMail(hostid.getText().toString().trim(),msg);
                            Toast.makeText(root.getContext(), "Checked In successfully", Toast.LENGTH_LONG).show();
                        } else {

                            Toast.makeText(root.getContext(), "Error! Please try again", Toast.LENGTH_LONG).show();
                        }
                        pd.dismiss();
                        hostname.setText(null);
                        hostid.setText(null);
                        hostphone.setText(null);
                        hostadd.setText(null);
                        guestname.setText(null);
                        guestid.setText(null);
                        guestphone.setText(null);
                    }
                });
            }
        });
        return root;
    }

    private void sendMail(String ml, String ms) {
        String mail = ml;
        String message = ms;
        String subject = "New visitor is here";

        JavaMailAPI javaMailAPI = new JavaMailAPI(getActivity(),mail,subject,message);
        javaMailAPI.execute();
    }
}