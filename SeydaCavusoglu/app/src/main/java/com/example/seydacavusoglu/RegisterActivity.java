package com.example.seydacavusoglu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener  {


    EditText editTextEmail;
    EditText editTextFullName;
    EditText editTextPassword;
    TextView textViewSignIn;
    Button buttonSignUp;
    CheckBox agreementCheckBox;
    FirebaseAuth mAuth;
    StorageReference storageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth=FirebaseAuth.getInstance();
        editTextEmail=(EditText) findViewById(R.id.editTextSignUpEmail);
        editTextFullName=(EditText) findViewById(R.id.editTextSignUpFullName);
        editTextPassword=(EditText) findViewById(R.id.editTextSignUpPassword);
        textViewSignIn=(TextView) findViewById(R.id.redirectToSignIn);
        buttonSignUp=(Button)findViewById(R.id.signUpButton);
        agreementCheckBox=(CheckBox)findViewById(R.id.agreementCheckBox);
        textViewSignIn.setOnClickListener(this);
        buttonSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {

            case R.id.redirectToSignIn:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.signUpButton:
                String fullName=editTextFullName.getText().toString().trim();
                String email=editTextEmail.getText().toString().trim();
                String password=editTextPassword.getText().toString().trim();
                if(fullName.isEmpty())
                {
                    editTextFullName.setError("Name is required");
                    editTextFullName.requestFocus();
                    return;
                }
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
                if(!agreementCheckBox.isChecked())
                {
                    agreementCheckBox.setError("You must accept the usage terms");
                    agreementCheckBox.requestFocus();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {


                        User user=new User(email,fullName);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {

                                        Intent loginIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                        loginIntent.putExtra("user_id", mAuth.getCurrentUser().getUid());
                                        startActivity(loginIntent);
                                        Toast.makeText(RegisterActivity.this,"User signed-up successfully", Toast.LENGTH_SHORT).show();

                                    }
                                    else
                                    {
                                        Toast.makeText(RegisterActivity.this,"User sign-up failed", Toast.LENGTH_SHORT).show();


                                    }
                                }
                            });
                        }
                        else
                        {

                            Toast.makeText(RegisterActivity.this,"User sign-up failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }

    }
}