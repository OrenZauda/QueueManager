package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class JoinQueue extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    ListView mylist ;
    ArrayList<String> doc = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joinqueue);
        searchView = findViewById(R.id.searchview);
        searchView.setFocusable(false);
        mylist = findViewById(R.id.mylist);



        doc.clear();
        mylist.setAdapter(null);

        db.collection("collections list")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                doc.add(document.getId());

                                Log.d("my log", document.getId());
                            }
                        } else {
                            Log.d("my log", "Error getting documents: ", task.getException());
                        }
                        adapter = new ArrayAdapter<String>(JoinQueue.this,android.R.layout.simple_list_item_1,doc);
                        mylist.setAdapter(adapter);
                    }
                });


        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapter, View v, int position,long id){

                //based on item add info to intent
                Intent tousergroup = new Intent(JoinQueue.this,usergroupviewActivty.class);
                tousergroup.putExtra("queuename",mylist.getItemAtPosition(position).toString());
                startActivity(tousergroup);
            }
        });
    }


}
