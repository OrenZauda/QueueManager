
package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
    Button leave,entergroup;
    String queuename;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayAdapter<String> adapter;
    FirebaseAuth mFire ;
    String docpath ;
    String email;
    ArrayList<String> users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usergroupview_activty);

        email = (String)getIntent().getSerializableExtra("email");
        users = new ArrayList<>();
        mylist = findViewById(R.id.mylist);
        queuenametext = findViewById(R.id.queuename);
        leave = findViewById(R.id.leavebt);
        queuename = (String)getIntent().getSerializableExtra("queuename");
        queuenametext.setText(queuename);
        entergroup = findViewById(R.id.entergroup);
        db.collection("queues")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(queuename.equals(document.getData().get("queuename").toString())){
                                    users = (ArrayList<String>)document.getData().get("arraylist");
                                    docpath= document.getId();

                                }

                            }

                        adapter = new ArrayAdapter<String>(usergroupviewActivty.this,android.R.layout.simple_list_item_1,users);
                        mylist.setAdapter(adapter);
                    }
                });
        entergroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DocumentReference sfDocRef = db.collection("queues").document(docpath);

                db.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException{
                        DocumentSnapshot snapshot = transaction.get(sfDocRef);

                        ArrayList<String> playres = (ArrayList<String> )snapshot.getData().get("arraylist");
                        if(!(playres.contains(email))) {
                            playres.add(email);
                            users = playres;
                            transaction.update(sfDocRef, "arraylist", playres);

                        }
                        else{
                            Toast.makeText(usergroupviewActivty.this,"Your already in group",Toast.LENGTH_LONG);
                        }
                        // Success
                        return null;
                        


                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mylist.setAdapter(null);
                        adapter = new ArrayAdapter<String>(usergroupviewActivty.this,android.R.layout.simple_list_item_1,users);
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
        });

    }
}
