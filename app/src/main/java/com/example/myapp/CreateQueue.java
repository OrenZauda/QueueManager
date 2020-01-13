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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateQueue extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    RadioGroup radio_group;
    RadioButton radioButton;
    String personGivenName,personFamilyName ="";
    Button create_bt;
    ImageView back_bt;
    EditText queue_name, max_players;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListView mylist ;
    ArrayList<String> Collectionslist =new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createqueue);

        radio_group = findViewById(R.id.radio_group);
        back_bt = findViewById(R.id.back);
        create_bt =findViewById(R.id.btcreategroup);
        queue_name = findViewById(R.id.queue_name);
        max_players = findViewById(R.id.max_players);
        mylist = findViewById(R.id.mylist);
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



        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent todashboard = new Intent(CreateQueue.this, dashboard.class);
                startActivity(todashboard);
            }
        });


        create_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(queue_name.getText().toString().isEmpty()){
                    queue_name.setError("Please enter queue name");
                    queue_name.requestFocus();
                }
                if(max_players.getText().toString().isEmpty()){
                    max_players.setError("Please enter number");
                    max_players.requestFocus();
                }

                else if(max_players.getText().toString().isEmpty()&& queue_name.getText().toString().isEmpty()){
                    Toast.makeText(CreateQueue.this,"Fields are empty",Toast.LENGTH_LONG).show();

                }
                if (!queue_name.getText().toString().isEmpty()&&!max_players.getText().toString().isEmpty()){

                    int num= Integer.parseInt(max_players.getText().toString());
                    int selectedId = radio_group.getCheckedRadioButtonId();
                    // find the radiobutton by returned id
                    radioButton = findViewById(selectedId);

                    // Create a new user with a first and last queuename
                    Map<String, Object> data = new HashMap<>();
                    ArrayList <String> players = new ArrayList<String>();
                    data.put("arraylist", players);
                    data.put("criteria", radioButton.getText().toString());
                    data.put("max_players", num);

                    // Add a new document with a generated ID
                    db.collection(queue_name.getText().toString()).document(personGivenName+" "+personFamilyName).set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("my log","document created successfully");
                                    Toast.makeText(CreateQueue.this,"queue created successfuly",Toast.LENGTH_LONG).show();
                                    Intent toDashBoard = new Intent(CreateQueue.this,dashboard.class);
                                    startActivity(toDashBoard);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("my log","faild to create queue");
                            Toast.makeText(CreateQueue.this,"faild to create queue",Toast.LENGTH_LONG).show();

                        }
                    });

                    Map<String, Object> stam = new HashMap<>();
                    db.collection("collections list").document(queue_name.getText().toString()).set(stam);
                }
            }
        });
    }
}

