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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyQueues extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    ListView mylist ;
    ArrayList<String> doc = new ArrayList<>();
    ArrayList<String> collectionslist = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    GoogleSignInClient mGoogleSignInClient;
    String personName,personGivenName,personFamilyName ="" ;
    boolean manager_mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_queues);

        mylist = findViewById(R.id.mylist);
        manager_mode = (boolean)getIntent().getSerializableExtra("manager mode");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            personName = acct.getDisplayName();
            personGivenName = acct.getGivenName();
            personFamilyName = acct.getFamilyName();
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            personGivenName = user.getDisplayName();
        }
        db.collection("collections list")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                collectionslist.add(document.getId());
                                Log.d("my log", document.getId());
                            }
                        } else {
                            Log.d("my log", "Error getting documents: ", task.getException());
                        }

                        if (manager_mode) {
                            doc.clear();
                            mylist.setAdapter(null);
                            for (final String queue:collectionslist){
                                db.collection(queue).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                if(document.getId().equals(personGivenName+" "+personFamilyName)) {
                                                    doc.add(queue);

                                                    Log.d("my log", document.getId());
                                                }
                                            }
                                        } else {
                                            Log.d("my log", "Error getting documents: ", task.getException());
                                        }
                                        adapter = new ArrayAdapter(MyQueues.this,android.R.layout.simple_list_item_1,doc);
                                        mylist.setAdapter(adapter);
                                    }
                                });
                            }

                        }
                        else{
                            doc.clear();
                            mylist.setAdapter(null);
                            for (final String queue:collectionslist){
                                db.collection(queue).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {

                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                ArrayList<String> parti = (ArrayList<String>)document.getData().get("arraylist");
                                                if(parti.contains(personGivenName+" "+personFamilyName)) {
                                                    doc.add(queue);
                                                    Log.d("my log", document.getData().get("arraylist").toString());
                                                }
                                            }
                                        } else {
                                            Log.d("my log", "Error getting documents: ", task.getException());
                                        }
                                        adapter = new ArrayAdapter(MyQueues.this,android.R.layout.simple_list_item_1,doc);
                                        mylist.setAdapter(adapter);
                                    }
                                });
                            }
                        }
                    }
                });


        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapter, View v, int position, long id){

                //based on item add info to intent
                Intent tousergroup = new Intent(MyQueues.this,usergroupviewActivty.class);
                tousergroup.putExtra("queuename",mylist.getItemAtPosition(position).toString());
                startActivity(tousergroup);
            }
        });
    }
}
