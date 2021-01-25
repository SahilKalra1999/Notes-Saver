package com.sahilkalra.notessaver.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.sahilkalra.notessaver.MainActivity;
import com.sahilkalra.notessaver.R;

import java.util.Objects;

public class Register extends AppCompatActivity {
    EditText rUserName, rUserEmail, rUserPass, rUserConfPass;
    Button syncAccount;
    TextView loginAct;
    ProgressBar progressBar;
    Toolbar toolbar;

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
       toolbar=findViewById(R.id.regToolbar);
        toolbar.setTitle("Create New Account");


        rUserName=findViewById(R.id.userName);
        rUserEmail=findViewById(R.id.userEmail);
        rUserPass=findViewById(R.id.password);

        rUserConfPass=findViewById(R.id.passwordConfirm);

        syncAccount=findViewById(R.id.createAccount);
        loginAct=findViewById(R.id.login);
        progressBar=findViewById(R.id.progressBar4);
        fAuth=FirebaseAuth.getInstance();
        loginAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });

        syncAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uUserName = rUserName.getText().toString();
                String uUserEmail = rUserEmail.getText().toString();
                String uUserPass = rUserPass.getText().toString();
                String uConfPass = rUserConfPass.getText().toString();

                if (!(uUserName.isEmpty() || uUserEmail.isEmpty() || uUserPass.isEmpty() || uConfPass.isEmpty())) {
                if (uUserPass.equals(uConfPass)) {
                    progressBar.setVisibility(View.VISIBLE);

                    AuthCredential credential = EmailAuthProvider.getCredential(uUserEmail, uUserPass);
                    Objects.requireNonNull(fAuth.getCurrentUser()).linkWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(Register.this, "Notes are Synced.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                            FirebaseUser usr = fAuth.getCurrentUser();
                            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(uUserName)
                                    .build();
                            usr.updateProfile(request);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Register.this, "Failed to Connect. Try Again", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                } else {
                    rUserConfPass.setError("Password Do Not Match");
                }
            }else{
                    Toast.makeText(Register.this, "All Fields Are Required..", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
        return super.onOptionsItemSelected(item);

    }
}