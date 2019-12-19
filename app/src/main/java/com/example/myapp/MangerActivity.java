package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MangerActivity extends AppCompatActivity implements Serializable {

    Button btback,btcreate,btmygroups;
    TextView options ;
    EditText groupname,playersnum;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList <String> doc = new ArrayList<>();
    ListView mylist ;
    ArrayAdapter <String> adapter;
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manger);


        btback = findViewById(R.id.btback);
        btcreate =findViewById(R.id.btcreategroup);
        btmygroups = findViewById(R.id.btmygroups);
        groupname = findViewById(R.id.Textgroupname);
        playersnum = findViewById(R.id.textnumplayer);
        mylist = findViewById(R.id.mylist);
        email = (String)getIntent().getSerializableExtra("email");







        btcreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String queuename= groupname.getText().toString();
                int num= Integer.parseInt(playersnum.getText().toString());
                if(queuename.isEmpty()){
                    groupname.setError("Please enter queue name");
                    groupname.requestFocus();
                }
                if(playersnum.getText().toString().isEmpty()){
                    playersnum.setError("Please enter number");
                    playersnum.requestFocus();
                }
                else if(playersnum.getText().toString().isEmpty()&&queuename.isEmpty()){
                    Toast.makeText(MangerActivity.this,"Fields are empty",Toast.LENGTH_LONG).show();

                }
                if (!queuename.isEmpty()&&!playersnum.getText().toString().isEmpty()){


                    // Create a new user with a first and last queuename
                    Map<String, Object> data = new HashMap<>();
                    ArrayList <String> players = new ArrayList<String>();
                    data.put("arraylist", players);
                    data.put("criteria", 1815);
                    data.put("queuename", queuename);
                    data.put("maxplayers", num);
                    data.put("manager",email);



                    // Add a new document with a generated ID
                    db.collection("queues")
                            .add(data)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("MangerActivity", "DocumentSnapshot added with ID: " + documentReference.getId());

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("MangerActivity", "Error adding document", e);
                                }
                            });
                }



            }
        });

        btmygroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toManagergroups = new Intent (MangerActivity.this,Managergroupslist.class);
                toManagergroups.putExtra("email",email);
                startActivity(toManagergroups);
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

        });
    }
}

