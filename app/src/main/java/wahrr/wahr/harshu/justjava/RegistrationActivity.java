package wahrr.wahr.harshu.justjava;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegistrationActivity extends AppCompatActivity {

    private Button button_submit;
    private TextView et;
    private EditText ed_email_login, ed_password_login, ed_name_login;
    private FirebaseAuth login_auth;
    private String emailid, password, full_name;
    public FirebaseDatabase database;
    public DatabaseReference databaseReference;


    @Override
    @SuppressLint("RestrictedApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initialize();

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate())
                    //authenticate
                {
                    login_auth.createUserWithEmailAndPassword(emailid, password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //User registered successfully
                                DatabaseReference myRef = database.getReference("Users");

                                myRef.child("Userid").setValue(login_auth.getCurrentUser().getUid());
                                myRef.child("Emailid").setValue(emailid);
                                myRef.child("Full Name").setValue(full_name);

                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                intent.putExtra("name", full_name);
                                intent.putExtra("emailid", emailid);

                                startActivity(intent);

                                Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.i("Response", "Failed to create user:" + task.getException().getMessage());
                                Toast.makeText(getApplicationContext(), "User already exist please Login", Toast.LENGTH_SHORT).show();


                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                        }
                    });


                }
            }
        });


        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void initialize() {
        button_submit = (Button) findViewById(R.id.buttonid);
        et = (TextView) findViewById(R.id.edit_text);
        ed_email_login = (EditText) findViewById(R.id.emailid_login);
        ed_password_login = (EditText) findViewById(R.id.passwordid_login);
        ed_name_login = (EditText) findViewById(R.id.fullname_login);
        login_auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

    }

    public boolean validate() {
        boolean result = false;
        full_name = ed_name_login.getText().toString().toLowerCase();
        emailid = ed_email_login.getText().toString().toLowerCase();
        password = ed_password_login.getText().toString().toLowerCase();
        String login_toast = getString(R.string.login_toast);
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (emailid.isEmpty() || password.isEmpty() || full_name.isEmpty() ) {
            Toast.makeText(this, login_toast, Toast.LENGTH_SHORT).show();
             result = false;
        }
        else {
            if(!emailid.matches(emailPattern)) {

                Toast.makeText(this, "Please enter Valid Email address", Toast.LENGTH_SHORT).show();
                return result = false;
            }else
            return result = true;
        }
        return result;
    }
}
