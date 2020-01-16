
package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;

public class CurentQueue extends AppCompatActivity {
    ListView mylist;
    TextView queuenametext, text_delete_queue,enter_queue_text,leave_queue_text;
    ImageView leavebt,entergroup,back_bt,delete_queue;
    String queuename;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayAdapter<String> adapter;
    String docpath ,leavedocpath,personGivenName,personFamilyName ="";
    ArrayList<String> users;
    GoogleSignInClient mGoogleSignInClient;
    SearchView searchView;
    boolean manager_mode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_queue);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {

            personGivenName = acct.getGivenName();
            personFamilyName = acct.getFamilyName();

        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            personGivenName = user.getDisplayName();
        }

        back_bt = findViewById(R.id.btback);
        users = new ArrayList<>();
        mylist = findViewById(R.id.mylist);
        queuenametext = findViewById(R.id.queuename);
        leavebt = findViewById(R.id.leavebt);
        queuename = (String)getIntent().getSerializableExtra("queuename");
        queuenametext.setText(queuename);
        entergroup = findViewById(R.id.enter_queue);
        delete_queue = findViewById(R.id.delete_queue);
        manager_mode =(boolean)getIntent().getSerializableExtra("manager_mode");
        text_delete_queue = findViewById(R.id.text_delete_queue);
        searchView = findViewById(R.id.searchview);
        enter_queue_text = findViewById(R.id.enter_queue_text);
        leave_queue_text = findViewById(R.id.leave_queue_text);



        //show current queue list view
         db.collection(queuename)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                users = (ArrayList<String>)document.getData().get("arraylist");
                                docpath= document.getId();
                                adapter = new ArrayAdapter<String>(CurentQueue.this,android.R.layout.simple_list_item_1,users);
                                mylist.setAdapter(adapter);
                            }
                        } else {
                            Log.d("my log", "Error getting documents: ", task.getException());
                        }


                    }



                });

        if(manager_mode){
            entergroup.setVisibility(View.GONE);
            enter_queue_text.setVisibility(View.GONE);
            leavebt.setVisibility(View.GONE);
            leave_queue_text.setVisibility(View.GONE);
        }
        entergroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DocumentReference sfDocRef = db.collection(queuename).document(docpath);
                if(!(users.contains(personGivenName+" "+personFamilyName))) {
                    db.runTransaction(new Transaction.Function<Void>() {
                        @Override
                        public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                            DocumentSnapshot snapshot = transaction.get(sfDocRef);

                            ArrayList<String> playres = (ArrayList<String>) snapshot.getData().get("arraylist");

                                playres.add(personGivenName+ " " +personFamilyName);
                                users = playres;
                                transaction.update(sfDocRef, "arraylist", playres);

                            return null;


                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mylist.setAdapter(null);
                            adapter = new ArrayAdapter<String>(CurentQueue.this, android.R.layout.simple_list_item_1, users);
                            mylist.setAdapter(adapter);
                            Log.d("mylog", "Transaction success!");
                            Toast.makeText(CurentQueue.this, "You enter the queue successfully", Toast.LENGTH_LONG).show();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("mylog", "Transaction failure.", e);
                                }
                            });
                }
                else{
                    Toast.makeText(CurentQueue.this, "You already in queue", Toast.LENGTH_LONG).show();

                }

            }
        });

        leavebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final DocumentReference sfDocRef = db.collection(queuename).document(docpath);

                db.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot snapshot = transaction.get(sfDocRef);

                        ArrayList<String> playres = (ArrayList<String>) snapshot.getData().get("arraylist");

                        playres.remove(personGivenName+ " " +personFamilyName);
                        users = playres;
                        transaction.update(sfDocRef, "arraylist", playres);

                        return null;


                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mylist.setAdapter(null);
                        adapter = new ArrayAdapter<String>(CurentQueue.this, android.R.layout.simple_list_item_1, users);
                        mylist.setAdapter(adapter);
                        Log.d("mylog", "Transaction success!");
                        Toast.makeText(CurentQueue.this, "You remove from queue successfully", Toast.LENGTH_LONG).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("mylog", "Transaction failure.", e);
                            }
                        });


            }
        });




        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toChooseQueue = new Intent(CurentQueue.this,dashboard.class);
                toChooseQueue.putExtra("manager_mode",manager_mode);
                startActivity(toChooseQueue);
            }
        });
        if(!manager_mode){
            delete_queue.setVisibility(View.GONE);
            text_delete_queue.setVisibility(View.GONE);
        }
        delete_queue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Qname = searchView.getQuery().toString();
                if(users.contains(Qname)) {
                    Intent Go = new Intent(CurentQueue.this, CurentQueue.class);
                    Go.putExtra("queuename", Qname);
                    Go.putExtra("manager_mode",manager_mode);
                    startActivity(Go);
                }
            }
        });

    }
}
