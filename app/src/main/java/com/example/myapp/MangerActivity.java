package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MangerActivity extends AppCompatActivity {
    Button btback,btcreate,btmygroups;
    EditText groupname,playersnum;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manger);

        btback = findViewById(R.id.btback);
        btcreate =findViewById(R.id.btcreategroup);
        btmygroups = findViewById(R.id.btcreategroup);
        groupname = findViewById(R.id.Textgroupnum);
        playersnum = findViewById(R.id.textnumplayer);

//        btcreate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String queuename= groupname.getText().toString();
//                int num= Integer.parseInt(playersnum.getText().toString());
//                if(queuename.isEmpty()){
//                    groupname.setError("Please enter queuename");
//                    groupname.requestFocus();
//                }
//                if(playersnum.getText().toString().isEmpty()){
//                    playersnum.setError("Please enter number");
//                    playersnum.requestFocus();
//                }
//                else if(playersnum.getText().toString().isEmpty()&&queuename.isEmpty()){
//                    Toast.makeText(MangerActivity.this,"Fields are empty",Toast.LENGTH_LONG).show();
//
//                }
//                if (!queuename.isEmpty()&&!playersnum.getText().toString().isEmpty()){
//
//                }
//
//                // Create a new user with a first and last queuename
//                Map<String, Object> queue = new HashMap<>();
//                ArrayList <String> players = new ArrayList<String>();
//                queue.put("arraylist", players);
//                queue.put("criteria", 1815);
//                queue.put("queuename", queuename);
//                queue.put("maxplayers", num);
//
//
//                // Add a new document with a generated ID
//                db.collection("queues")
//                        .add(queue)
//                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                            @Override
//                            public void onSuccess(DocumentReference documentReference) {
//                                Log.d("MangerActivity", "DocumentSnapshot added with ID: " + documentReference.getId());
//
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.w("MangerActivity", "Error adding document", e);
//                            }
//                        });
//
//            }
//        });
    }
}
