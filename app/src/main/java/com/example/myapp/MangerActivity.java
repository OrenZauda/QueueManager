package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MangerActivity extends AppCompatActivity implements Serializable {

    Button create_bt;
    ImageView back_bt;
    EditText group_name, max_players;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListView mylist ;
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manger);


        back_bt = findViewById(R.id.back);
        create_bt =findViewById(R.id.btcreategroup);
        group_name = findViewById(R.id.group_name);
        max_players = findViewById(R.id.max_players);
        mylist = findViewById(R.id.mylist);
        email = (String)getIntent().getSerializableExtra("email");


        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent todashboard = new Intent(MangerActivity.this,dashboardActivity.class);
                startActivity(todashboard);
            }
        });

        create_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(group_name.getText().toString().isEmpty()){
                    group_name.setError("Please enter queue name");
                   // group_name.requestFocus();
                }
                if(max_players.getText().toString().isEmpty()){
                    max_players.setError("Please enter number");
                    max_players.requestFocus();
                }
                else if(max_players.getText().toString().isEmpty()&&group_name.getText().toString().isEmpty()){
                    Toast.makeText(MangerActivity.this,"Fields are empty",Toast.LENGTH_LONG).show();

                }
                if (!group_name.getText().toString().isEmpty()&&!max_players.getText().toString().isEmpty()){
                    String queuename= group_name.getText().toString();
                    int num= Integer.parseInt(max_players.getText().toString());
                    // Create a new user with a first and last queuename
                    Map<String, Object> data = new HashMap<>();
                    ArrayList <String> players = new ArrayList<String>();
                    data.put("arraylist", players);
                    data.put("criteria", 1815);
                    data.put("queuename", queuename);
                    data.put("max_players", num);
                    data.put("manager",email);



                    // Add a new document with a generated ID
                    db.collection("queues")
                            .add(data)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("MangerActivity", "DocumentSnapshot added with ID: " + documentReference.getId());
                                    Toast.makeText(MangerActivity.this,"group created successfuly",Toast.LENGTH_LONG).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("MangerActivity", "Error adding document", e);
                                    Toast.makeText(MangerActivity.this,"faild to create group",Toast.LENGTH_LONG).show();
                                }
                            });
                }



            }
        });

//               doc.clear();
//               mylist.setAdapter(null);
//                db.collection("queues")
//
//                        .get()
//                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                if (task.isSuccessful()) {
//
//                                    for (QueryDocumentSnapshot document : task.getResult()) {
//
//                                        doc.add(document.getData().get("queuename").toString());
//
//                                        Log.d("avi", document.getId() + " => " + document.getData().get("queuename").toString());
//                                    }
//                                } else {
//                                    Log.d("avi", "Error getting documents: ", task.getException());
//                                }
//                                adapter = new ArrayAdapter<String>(MangerActivity.this,android.R.layout.simple_list_item_1,doc);
//                                mylist.setAdapter(adapter);
//                            }
//                        });


    }
}

