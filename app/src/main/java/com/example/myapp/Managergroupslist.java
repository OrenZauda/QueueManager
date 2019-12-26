package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Managergroupslist extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    ListView mylist ;
    ArrayList<String> doc = new ArrayList<>();
    FirebaseFirestore db;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managergroupslist);
        db = FirebaseFirestore.getInstance();
        mylist = findViewById(R.id.mylist);
        email = (String)getIntent().getSerializableExtra("email");

        doc.clear();
        mylist.setAdapter(null);
        db.collection("queues")

                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getData().get("manager").toString().equals(email)) {
                                    doc.add(document.getData().get("queuename").toString());


                                    Log.d("avi", document.getId() + " => " + document.getData().get("queuename").toString());
                                }
                            }
                        } else {
                            Log.d("avi", "Error getting documents: ", task.getException());
                        }
                        adapter = new ArrayAdapter<String>(Managergroupslist.this,android.R.layout.simple_list_item_1,doc);
                        mylist.setAdapter(adapter);
                    }
                });
    }
}
