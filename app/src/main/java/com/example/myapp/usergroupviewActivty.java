
package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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

public class usergroupviewActivty extends AppCompatActivity {
    ListView mylist;
    TextView queuenametext;
    ImageView leavebt,entergroup;
    String queuename;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayAdapter<String> adapter;
    String docpath ,personGivenName,personFamilyName ="";
    ArrayList<String> users;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usergroupview_activty);
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

        users = new ArrayList<>();
        mylist = findViewById(R.id.mylist);
        queuenametext = findViewById(R.id.queuename);
        leavebt = findViewById(R.id.leavebt);
        queuename = (String)getIntent().getSerializableExtra("queuename");
        queuenametext.setText(queuename);
        entergroup = findViewById(R.id.enter_queue);


        //show current queue list view
         db.collection(queuename)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("my log", document.getId() + " => " + document.getData());
                                users = (ArrayList<String>)document.getData().get("arraylist");
                                docpath= document.getId();
                                adapter = new ArrayAdapter<String>(usergroupviewActivty.this,android.R.layout.simple_list_item_1,users);
                                mylist.setAdapter(adapter);
                            }
                        } else {
                            Log.d("my log", "Error getting documents: ", task.getException());
                        }


                    }



                });

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
                            adapter = new ArrayAdapter<String>(usergroupviewActivty.this, android.R.layout.simple_list_item_1, users);
                            mylist.setAdapter(adapter);
                            Log.d("mylog", "Transaction success!");
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
                    Toast.makeText(usergroupviewActivty.this, "Your already in queue", Toast.LENGTH_LONG).show();

                }

            }
        });

        leavebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
