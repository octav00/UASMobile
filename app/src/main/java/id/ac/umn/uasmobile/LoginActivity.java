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

    EditText email, password;
    AppCompatButton loginBtn;
    TextView registerBtn;

    DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.l_email);
        password = findViewById(R.id.l_password);
        loginBtn = findViewById(R.id.l_loginBtn);
        registerBtn = findViewById(R.id.l_registerBtn);

        // firebase database
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        // login button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                final String emailTxt = email.getText().toString();
                final String passwordTxt = password.getText().toString();

                if(emailTxt.isEmpty() || passwordTxt.isEmpty()){
                    Toast.makeText(LoginActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else{
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            progressDialog.dismiss();

                            if(snapshot.child("users").hasChild(passwordTxt)){
                                if(snapshot.child("users").child(passwordTxt).child("email").getValue().equals(emailTxt)){
                                    // save password to memory
                                    MemoryData.saveData(passwordTxt, LoginActivity.this);

                                    // save name to memory
                                    MemoryData.saveName(snapshot.child("users").child(passwordTxt).child("name").getValue().toString(), LoginActivity.this);

                                    Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("password", passwordTxt);
                                    intent.putExtra("name", snapshot.child("users").child(passwordTxt).child("name").getValue().toString());
                                    intent.putExtra("email", emailTxt);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "Email or password is wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "Email or password is wrong", Toast.LENGTH_SHORT).show();
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
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}