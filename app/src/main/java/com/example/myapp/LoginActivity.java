package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

public class LoginActivity extends AppCompatActivity implements Serializable {

    public EditText email,psw;
    Button btsin,btsiup;
    TextView tvsiup;
    FirebaseAuth mFire ;
    private  FirebaseAuth.AuthStateListener mFireLis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFire = FirebaseAuth.getInstance();
        email = findViewById(R.id.emailtext);
        psw = findViewById(R.id.passwordtext);
        btsin = findViewById(R.id.btsignin);
        tvsiup = findViewById(R.id.singuptext);
        btsiup = findViewById(R.id.btsinup);

        btsin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailid = email.getText().toString();
                String pssw = psw.getText().toString();
                if(emailid.isEmpty()){
                    email.setError("Please enter email");
                    email.requestFocus();
                }
                if(pssw.isEmpty()){
                    psw.setError("Please enter your password");
                    psw.requestFocus();
                }
                else if(emailid.isEmpty() && pssw.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Fields are empty",Toast.LENGTH_SHORT).show();
                }
                else if (!(emailid.isEmpty() && pssw.isEmpty())){


                    mFire.signInWithEmailAndPassword(emailid,pssw).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login Error, Please login again", Toast.LENGTH_SHORT).show();

                            } else {
                                Intent intohome = new Intent(LoginActivity.this, ChooseAthorithyActivity.class);
                                intohome.putExtra("email",email.getText().toString());
                                startActivity(intohome);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(LoginActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
                }

            }
        });

        btsiup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intosiup = new Intent(LoginActivity.this, registerActivity.class);
                startActivity(intosiup);

            }
        });

        mFireLis = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser firebase = mFire.getCurrentUser();
                if(firebase != null ){
                    Toast.makeText(LoginActivity.this,"You are logged in!",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, ChooseAthorithyActivity.class);
                    i.putExtra("email",email.getText().toString());
                    startActivity(i);
                }
                else {
                    Toast.makeText(LoginActivity.this,"Please log in!",Toast.LENGTH_SHORT).show();

                }
            }
        };


    }

    @Override
    protected void onStart() {
        super.onStart();
        mFire.addAuthStateListener(mFireLis);
    }
}
