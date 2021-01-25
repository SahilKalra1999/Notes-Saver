package com.sahilkalra.notessaver.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sahilkalra.notessaver.MainActivity;
import com.sahilkalra.notessaver.R;
import com.sahilkalra.notessaver.Splash;

public class Login extends AppCompatActivity {
    EditText lEmail, lPassword;
    Button loginNow;
    TextView forgetPass, createAcc;
    Toolbar toolbar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar=findViewById(R.id.loginToolbar);
        toolbar.setTitle("Login");
        lEmail=findViewById(R.id.email);
        lPassword=findViewById(R.id.lPassword);
        loginNow=findViewById(R.id.loginBtn);
        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        showWarning();

        spinner=findViewById(R.id.progressBar3);

        forgetPass=findViewById(R.id.forgotPassword);
        createAcc=findViewById(R.id.createNewAccount);
        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
                finish();
            }
        });
        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Login.this, "This feature is under Implementation..", Toast.LENGTH_SHORT).show();
            }
        });

        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail=lEmail.getText().toString();
                String mPass=lPassword.getText().toString();

                if (mEmail.isEmpty() || mPass.isEmpty()){
                    Toast.makeText(Login.this, "Both fields are Required..", Toast.LENGTH_SHORT).show();
                    return;
                }
                //delete notes first
                spinner.setVisibility(View.VISIBLE);
                if (fAuth.getCurrentUser().isAnonymous()){
                    FirebaseUser user=fAuth.getCurrentUser();
                    fStore.collection("notes").document(user.getUid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Login.this, "All Temp notes are Deleted.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //delete temp user
                    user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Login.this, "Temp User Deleted.", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                //Signing user with Email And Password
                fAuth.signInWithEmailAndPassword(mEmail,mPass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(Login.this, "Success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, "Login Failed. "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        spinner.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    private void showWarning() {
        AlertDialog.Builder warning=new AlertDialog.Builder(this)
                .setTitle("Are you Sure ?")
                .setMessage("Linking Existing Account will Delete The Temp Notes. Create New Account to Save Them.")
                .setPositiveButton("Save Notes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(), Register.class));
                        finish();
                    }
                }).setNegativeButton("Its Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                        }
                });
        warning.show();
    }


}