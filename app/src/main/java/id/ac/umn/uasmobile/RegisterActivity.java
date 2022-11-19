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

public class RegisterActivity extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://uasmobile-d872e-default-rtdb.firebaseio.com/");
    TextView loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText name = findViewById(R.id.r_name);
        final EditText password = findViewById(R.id.r_password);
        final EditText email = findViewById(R.id.r_email);
        final AppCompatButton registerBtn = findViewById(R.id.r_registerBtn);
        loginBtn = findViewById(R.id.r_loginBtn);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        // check if user already logged in
//        if(!MemoryData.getData(this).isEmpty()){
//
//            Intent intent = new Intent(Register.this, MainActivity.class);
//            intent.putExtra("email", MemoryData.getData(this));
//            intent.putExtra("name", MemoryData.getName(this));
//            intent.putExtra("password", "");
//            startActivity(intent);
//            finish();
//        }

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();

                final String nameTxt = name.getText().toString();
                final String passwordTxt = password.getText().toString();
                final String emailTxt = email.getText().toString();

                if(nameTxt.isEmpty() || passwordTxt.isEmpty() || emailTxt.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "All Fields Required!!!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else{
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            progressDialog.dismiss();

                            if(snapshot.child("users").hasChild(passwordTxt)){
                                Toast.makeText(RegisterActivity.this, "Mobile already exists", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                databaseReference.child("users").child(passwordTxt).child("email").setValue(emailTxt);
                                databaseReference.child("users").child(passwordTxt).child("name").setValue(nameTxt);
                                databaseReference.child("users").child(passwordTxt).child("profile_pic").setValue("");

                                // save password to memory
                                MemoryData.saveData(passwordTxt, RegisterActivity.this);

                                // save name to memory
                                MemoryData.saveName(nameTxt, RegisterActivity.this);

                                Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                intent.putExtra("password", passwordTxt);
                                intent.putExtra("name", nameTxt);
                                intent.putExtra("email", emailTxt);
                                startActivity(intent);
                                finish();
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

        // login button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}