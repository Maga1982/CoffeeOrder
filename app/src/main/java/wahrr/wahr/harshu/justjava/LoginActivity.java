package wahrr.wahr.harshu.justjava;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private Button button_submit;
    private TextView textView;
    private EditText ed_email_login,ed_password_login;
    private FirebaseAuth login_auth;
    private String emailid,password,full_name,emailid_intent;
    private String TAG = "message";
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

         initialize();


        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(validate())

                   login_auth.signInWithEmailAndPassword(emailid, password);

                   // String uid  = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    //DatabaseReference myRef = database.getReference(login_auth.getCurrentUser().getUid());

                    //login or register screen
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                     full_name = getIntent().getStringExtra("name");
                     emailid_intent =  getIntent().getStringExtra("emailid");

                    intent.putExtra("full_name",full_name);
                intent.putExtra("emailid",emailid_intent);
                    startActivity(intent);









            }
        });


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegistrationActivity.class);
                startActivity(intent);
            }
        });




    }

    public boolean validate(){
        boolean result = false;
        emailid= ed_email_login.getText().toString().toLowerCase();
        password= ed_password_login.getText().toString().toLowerCase();
        String login_toast= getString(R.string.login_toast);
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(emailid.isEmpty() || password.isEmpty()){
            Toast.makeText(this,login_toast,Toast.LENGTH_SHORT).show();
            result=false;
        }
        else {
            if(emailid.matches(emailPattern)) {
                Toast.makeText(this, "Please enter Valid Email address", Toast.LENGTH_SHORT).show();
                return result=false;
            }else
            return result = true;
        }
        return result;
    }
    public void initialize(){
        button_submit = (Button) findViewById(R.id.buttonid);
        textView=(TextView) findViewById(R.id.text_view_login);
        ed_email_login = (EditText) findViewById(R.id.emailid_login);
        ed_password_login=(EditText) findViewById(R.id.passwordid_login);
        login_auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


    }

}
