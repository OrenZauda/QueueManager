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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity{

    EditText email,psw;
    Button loginbt, signupbt;
    SignInButton googlesign;
    FirebaseAuth mFire ;
    private  FirebaseAuth.AuthStateListener mFireLis;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFire = FirebaseAuth.getInstance(); 
        email = findViewById(R.id.emailtext);
        psw = findViewById(R.id.passwordtext);

        loginbt = findViewById(R.id.loginbt);
        signupbt = findViewById(R.id.signupbt);

        googlesign = findViewById(R.id.sign_in_button);

        googlesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        loginbt.setOnClickListener(new View.OnClickListener() {
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
                else if(emailid.isEmpty() && pssw.isEmpty() ){
                    Toast.makeText(Login.this,"Fields are empty",Toast.LENGTH_SHORT).show();
                }
                else if (!(emailid.isEmpty() && pssw.isEmpty())){


                    mFire.signInWithEmailAndPassword(emailid,pssw).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(Login.this, "Login Error, Please login again", Toast.LENGTH_SHORT).show();

                            } else {
                                Intent intohome = new Intent(Login.this, SelectMode.class);
                                startActivity(intohome);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(Login.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
                }

            }
        });

         signupbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tosignup = new Intent(Login.this, SignUp.class);
                startActivity(tosignup);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            //updateUI(account);
            Intent tochoose = new Intent(this, SelectMode.class);
            startActivity(tochoose);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mFire.getCurrentUser();

    }
    @Override
    public void onBackPressed() {

      super.onBackPressed();
        finishAffinity();
    // Not calling **super**, disables back_ground button in current screen.
    }

}
