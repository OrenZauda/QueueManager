package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUp extends AppCompatActivity {
    Button signupbt ;
    EditText email,psw,nick_name;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.emailtext);
        psw = findViewById(R.id.passwordtext);
        nick_name = findViewById(R.id.nick_name);
        signupbt =findViewById(R.id.signupbt);
        mAuth = FirebaseAuth.getInstance();


        signupbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailid = email.getText().toString();
                String pssw = psw.getText().toString();
                final String nickName = nick_name.getText().toString();
                if(emailid.isEmpty()){
                    email.setError("Please enter email");
                    email.requestFocus();
                }
                if(pssw.isEmpty()){
                    psw.setError("Please enter your password");
                    psw.requestFocus();
                }
                if(nickName.isEmpty()){
                    nick_name.setError("Please enter your nick name");
                    nick_name.requestFocus();
                }
                else if(emailid.isEmpty() && pssw.isEmpty() && nickName.isEmpty()){
                    Toast.makeText(SignUp.this,"Fields are empty",Toast.LENGTH_SHORT).show();
                }
                else if (!(emailid.isEmpty() && pssw.isEmpty() && nickName.isEmpty())){
                    mAuth.createUserWithEmailAndPassword(emailid,pssw).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(SignUp.this,"SignUp Unsuccessful,Please try again",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(nickName).build();

                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("my log", "User profile updated.");
                                                }
                                            }
                                        });
                                Intent toSelectMode = new Intent(SignUp.this, SelectMode.class);
                                startActivity(toSelectMode);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(SignUp.this,"Error Occurred!",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
