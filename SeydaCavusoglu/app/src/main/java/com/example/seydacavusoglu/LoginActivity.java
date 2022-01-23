package com.example.seydacavusoglu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    EditText editTextEmail;
    EditText editTextPassword;
    TextView textViewSignUp;
    Button buttonSignIn;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();
        editTextEmail=(EditText) findViewById(R.id.editTextEmail);
        editTextPassword=(EditText) findViewById(R.id.editTextPassword);
        textViewSignUp=(TextView) findViewById(R.id.redirectToSignUp);
        buttonSignIn=(Button) findViewById(R.id.signInButton);

        textViewSignUp.setOnClickListener(this);
        buttonSignIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.redirectToSignUp:

                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.signInButton:


                String email=editTextEmail.getText().toString().trim();
                String password=editTextPassword.getText().toString().trim();
                if(email.isEmpty())
                {
                    editTextEmail.setError("Email is required");
                    editTextEmail.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {

                    editTextEmail.setError("Email is not valid");
                    editTextEmail.requestFocus();
                    return;
                }
                if(password.isEmpty())
                {
                    editTextEmail.setError("Password is required");
                    editTextEmail.requestFocus();
                    return;
                }
                if(password.length()<6)
                {
                    editTextEmail.setError("Password must at least have 6 characters");
                    editTextEmail.requestFocus();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {


                                Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                                loginIntent.putExtra("user_id", mAuth.getCurrentUser().getUid());
                                startActivity(loginIntent);

                                Toast.makeText(LoginActivity.this,"User signed-in successfully", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {

                                Toast.makeText(LoginActivity.this,"User sign-in failed.", Toast.LENGTH_SHORT).show();
                            }
                    }
                });
                break;

        }

    }
}