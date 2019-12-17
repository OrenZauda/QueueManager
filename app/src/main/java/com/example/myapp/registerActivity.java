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


public class registerActivity extends AppCompatActivity {

    public EditText email,psw;
    Button btsiup,btsignin;
    TextView tvsin;
    FirebaseAuth mFire ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        mFire = FirebaseAuth.getInstance();
        email = findViewById(R.id.emailtext);
        psw = findViewById(R.id.passwordtext);
        btsiup = findViewById(R.id.btsignup);
        tvsin = findViewById(R.id.singuptext);
        btsignin = findViewById(R.id.btsignin);
        btsiup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailid = email.getText().toString();
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
                    Toast.makeText(registerActivity.this,"Fields are empty",Toast.LENGTH_SHORT).show();
                }
                else if (!(emailid.isEmpty() && pssw.isEmpty())){
                            mFire.createUserWithEmailAndPassword(emailid,pssw).addOnCompleteListener(registerActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(registerActivity.this,"SignUp Unsuccessful,Please try again",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                startActivity(new Intent(registerActivity.this, ChooseAthorithyActivity.class));
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(registerActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(registerActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        
    }






}
