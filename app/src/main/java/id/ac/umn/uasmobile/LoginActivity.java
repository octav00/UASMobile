package id.ac.umn.uasmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://uasmobile-d872e-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText email = findViewById(R.id.l_email);
        final EditText password = findViewById(R.id.l_password);
        final TextView registerButton = findViewById(R.id.l_registerBtn);
        final AppCompatButton loginBtn = findViewById(R.id.l_loginBtn);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        // check if user already logged in
        if(!MemoryData.getData(this).isEmpty()){

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("mobile", MemoryData.getData(this));
            intent.putExtra("name", MemoryData.getName(this));
            intent.putExtra("email", "");
            startActivity(intent);
            finish();
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();

                final String mobileTxt = email.getText().toString();
                final String passwordTxt = password.getText().toString();

                if(mobileTxt.isEmpty() || passwordTxt.isEmpty()){
                    Toast.makeText(LoginActivity.this, "All Fields Required!!!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else{

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            progressDialog.dismiss();

                            if(snapshot.child("users").hasChild(mobileTxt)){
                                String emailTxt = snapshot.child("users").child(mobileTxt).child("email").getValue().toString();
                                String nameTxt = snapshot.child("users").child(mobileTxt).child("name").getValue().toString();

                                if(emailTxt.equals(passwordTxt)){
                                    // save mobile to memory
                                    MemoryData.saveData(mobileTxt, LoginActivity.this);

                                    // save name to memory
                                    MemoryData.saveName(nameTxt, LoginActivity.this);

                                    Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("mobile", mobileTxt);
                                    intent.putExtra("name", nameTxt);
                                    intent.putExtra("email", emailTxt);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "Email and Password not matched", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "Mobile not exists", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });

        // register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

//public class LoginActivity extends AppCompatActivity {
//
//    private EditText emailEditText;
//    private EditText passwordEditText;
//    private AppCompatButton loginButton;
//    private TextView registerButton;
//
//    private String email;
//    private String password;
//
//    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://uasmobile-d872e-default-rtdb.firebaseio.com/");
//
//    // If the user has already logged in, there is no need to log in repeatedly
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if(!MemoryData.getData(LoginActivity.this).equals("")){
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            finish();
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        emailEditText = findViewById(R.id.l_email);
//        passwordEditText = findViewById(R.id.l_password);
//        loginButton = findViewById(R.id.l_loginBtn);
//        registerButton = findViewById(R.id.l_registerBtn);
//
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                email = emailEditText.getText().toString();
//                password = passwordEditText.getText().toString();
//
//                if(email.equals("") || password.equals("")){
//                    Toast.makeText(LoginActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
//                }else{
//                    final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
//                    progressDialog.setMessage("Please wait...");
//                    progressDialog.show();
//
//                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            boolean found = false;
//                            for(DataSnapshot dataSnapshot : snapshot.child("users").getChildren()){
//                                String getEmail = dataSnapshot.child("email").getValue(String.class);
//                                String getPassword = dataSnapshot.child("password").getValue(String.class);
//                                String getName = dataSnapshot.child("name").getValue(String.class);
//                                if(getEmail.equals(email) && getPassword.equals(password)){
//                                    found = true;
//                                    MemoryData.saveData(getEmail, LoginActivity.this);
//                                    MemoryData.saveName(getName, LoginActivity.this);
//                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                    finish();
//                                }
//                            }
//                            if(!found){
//                                Toast.makeText(LoginActivity.this, "Email or password is incorrect", Toast.LENGTH_SHORT).show();
//                            }
//                            progressDialog.dismiss();
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                }
//            }
//        });
//
//        registerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
//            }
//        });
//    }
//}